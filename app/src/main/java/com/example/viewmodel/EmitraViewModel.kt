package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ApplicationEntity
import com.example.data.ApplicationRepository
import com.example.data.EmitraDataProvider
import com.example.data.GovNewsFeedRepository
import com.example.model.ApplicationStatus
import com.example.model.ApplicationTimelineStep
import com.example.model.EmitraService
import com.example.model.GovSchemeNewsItem
import com.example.model.KioskDetails
import com.example.model.RecentUpdate
import com.example.model.ServiceCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class AppTab(val titleHi: String, val titleEn: String) {
    SERVICES("सेवाएं", "Services"),
    AI_ASSISTANT("दीपक AI", "Deepak AI"),
    CHECKLIST("चेकलिस्ट", "Checklist"),
    UPDATES("ताज़ा अपडेट", "Updates"),
    TRACKING("स्टेटस ट्रैक", "Track"),
    APPLY_UPLOAD("दस्तावेज़ भेजें", "Upload & Apply"),
    CENTER_INFO("दीपक सेंटर", "Center Info")
}

data class UploadDocumentSlot(
    val id: String,
    val label: String,
    val isMandatory: Boolean,
    val uri: String? = null,
    val fileName: String? = null,
    val fileSize: String? = null
)

class EmitraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ApplicationRepository
    val kioskDetails = KioskDetails()

    init {
        val dao = AppDatabase.getInstance(application).applicationDao()
        repository = ApplicationRepository(dao)
        viewModelScope.launch {
            repository.ensureInitialData()
        }
        viewModelScope.launch {
            GovNewsFeedRepository.refreshGovNewsFromWeb()
        }
    }

    val allApplications: StateFlow<List<ApplicationEntity>> = repository.allApplications
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Live Government Scheme News & Updates (Auto-synced from Web)
    val govNewsFeedItems: StateFlow<List<GovSchemeNewsItem>> = GovNewsFeedRepository.feedItems
    val isGovNewsRefreshing: StateFlow<Boolean> = GovNewsFeedRepository.isRefreshing
    val govNewsLastSync: StateFlow<String> = GovNewsFeedRepository.lastSyncTime

    fun refreshGovNews() {
        viewModelScope.launch {
            GovNewsFeedRepository.refreshGovNewsFromWeb()
        }
    }

    // Navigation Tab
    private val _currentTab = MutableStateFlow(AppTab.SERVICES)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Services Screen State
    private val _selectedCategory = MutableStateFlow(ServiceCategory.ALL)
    val selectedCategory: StateFlow<ServiceCategory> = _selectedCategory.asStateFlow()

    private val _serviceSearchQuery = MutableStateFlow("")
    val serviceSearchQuery: StateFlow<String> = _serviceSearchQuery.asStateFlow()

    private val _selectedServiceDetail = MutableStateFlow<EmitraService?>(null)
    val selectedServiceDetail: StateFlow<EmitraService?> = _selectedServiceDetail.asStateFlow()

    // Updates Screen State
    private val _selectedUpdateDetail = MutableStateFlow<RecentUpdate?>(null)
    val selectedUpdateDetail: StateFlow<RecentUpdate?> = _selectedUpdateDetail.asStateFlow()

    // Checklist State
    private val _checklistServiceId = MutableStateFlow<String?>("caste_obc")
    val checklistServiceId: StateFlow<String?> = _checklistServiceId.asStateFlow()

    fun openChecklistForService(serviceId: String) {
        _checklistServiceId.value = serviceId
        selectTab(AppTab.CHECKLIST)
    }

    // Tracking Screen State
    private val _trackingSearchInput = MutableStateFlow("")
    val trackingSearchInput: StateFlow<String> = _trackingSearchInput.asStateFlow()

    private val _activeTrackedApplication = MutableStateFlow<ApplicationEntity?>(null)
    val activeTrackedApplication: StateFlow<ApplicationEntity?> = _activeTrackedApplication.asStateFlow()

    private val _trackingSearchError = MutableStateFlow<String?>(null)
    val trackingSearchError: StateFlow<String?> = _trackingSearchError.asStateFlow()

    // Document Upload & Apply Form State
    private val _formServiceId = MutableStateFlow("caste_obc")
    val formServiceId: StateFlow<String> = _formServiceId.asStateFlow()

    private val _formApplicantName = MutableStateFlow("")
    val formApplicantName: StateFlow<String> = _formApplicantName.asStateFlow()

    private val _formMobile = MutableStateFlow("")
    val formMobile: StateFlow<String> = _formMobile.asStateFlow()

    private val _formJanAadhaar = MutableStateFlow("")
    val formJanAadhaar: StateFlow<String> = _formJanAadhaar.asStateFlow()

    private val _formVillage = MutableStateFlow("Sigdola Bara, Sikar 332312")
    val formVillage: StateFlow<String> = _formVillage.asStateFlow()

    private val _formNotes = MutableStateFlow("")
    val formNotes: StateFlow<String> = _formNotes.asStateFlow()

    private val _documentSlots = MutableStateFlow(getDefaultDocumentSlots())
    val documentSlots: StateFlow<List<UploadDocumentSlot>> = _documentSlots.asStateFlow()

    private val _isSubmitting = MutableStateFlow(false)
    val isSubmitting: StateFlow<Boolean> = _isSubmitting.asStateFlow()

    private val _latestSubmittedToken = MutableStateFlow<String?>(null)
    val latestSubmittedToken: StateFlow<String?> = _latestSubmittedToken.asStateFlow()

    private val _formValidationError = MutableStateFlow<String?>(null)
    val formValidationError: StateFlow<String?> = _formValidationError.asStateFlow()

    private fun getDefaultDocumentSlots(): List<UploadDocumentSlot> {
        return listOf(
            UploadDocumentSlot(
                id = "doc_aadhaar",
                label = "आधार कार्ड (Aadhaar Card - Front & Back)",
                isMandatory = true
            ),
            UploadDocumentSlot(
                id = "doc_jan_aadhaar",
                label = "जन आधार कार्ड (Jan Aadhaar Card)",
                isMandatory = true
            ),
            UploadDocumentSlot(
                id = "doc_photo",
                label = "पासपोर्ट साइज रंगीन फोटो (Passport Photo)",
                isMandatory = true
            ),
            UploadDocumentSlot(
                id = "doc_proof",
                label = "सहायक प्रमाण (जमाबंदी / अंकतालिका / आय प्रमाण)",
                isMandatory = false
            ),
            UploadDocumentSlot(
                id = "doc_sign",
                label = "हस्ताक्षर पर्ची (Signature Specimen)",
                isMandatory = false
            )
        )
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setCategory(category: ServiceCategory) {
        _selectedCategory.value = category
    }

    fun setServiceSearchQuery(query: String) {
        _serviceSearchQuery.value = query
    }

    fun showServiceDetail(service: EmitraService?) {
        _selectedServiceDetail.value = service
    }

    fun showUpdateDetail(update: RecentUpdate?) {
        _selectedUpdateDetail.value = update
    }

    fun startApplicationForService(service: EmitraService) {
        _formServiceId.value = service.id
        _selectedServiceDetail.value = null
        _currentTab.value = AppTab.APPLY_UPLOAD
    }

    fun setTrackingSearchInput(input: String) {
        _trackingSearchInput.value = input
        _trackingSearchError.value = null
    }

    fun searchTracking(targetQuery: String? = null) {
        val query = (targetQuery ?: _trackingSearchInput.value).trim()
        if (query.isEmpty()) {
            _trackingSearchError.value = "कृपया टोकन नंबर या 10 अंकों का मोबाइल नंबर दर्ज करें"
            return
        }
        viewModelScope.launch {
            val list = allApplications.value
            val match = list.firstOrNull {
                it.tokenNumber.equals(query, ignoreCase = true) ||
                        it.mobileNumber.contains(query) ||
                        it.applicantName.contains(query, ignoreCase = true)
            }
            if (match != null) {
                _activeTrackedApplication.value = match
                _trackingSearchError.value = null
            } else {
                _activeTrackedApplication.value = null
                _trackingSearchError.value = "कोई रिकॉर्ड नहीं मिला: '$query'। कृपया टोकन नंबर जांचें या दीपक सेंटर से संपर्क करें।"
            }
        }
    }

    fun selectTrackedItem(item: ApplicationEntity) {
        _activeTrackedApplication.value = item
        _trackingSearchInput.value = item.tokenNumber
        _trackingSearchError.value = null
        _currentTab.value = AppTab.TRACKING
    }

    // Form Handlers
    fun updateFormApplicantName(name: String) { _formApplicantName.value = name }
    fun updateFormMobile(mobile: String) { _formMobile.value = mobile }
    fun updateFormJanAadhaar(id: String) { _formJanAadhaar.value = id }
    fun updateFormVillage(village: String) { _formVillage.value = village }
    fun updateFormNotes(notes: String) { _formNotes.value = notes }
    fun updateFormService(serviceId: String) { _formServiceId.value = serviceId }

    fun attachDocument(slotId: String, uri: Uri?, fileName: String, fileSize: String) {
        _documentSlots.value = _documentSlots.value.map { slot ->
            if (slot.id == slotId) {
                slot.copy(
                    uri = uri?.toString() ?: "content://local_staged/$slotId",
                    fileName = fileName,
                    fileSize = fileSize
                )
            } else slot
        }
    }

    fun removeDocument(slotId: String) {
        _documentSlots.value = _documentSlots.value.map { slot ->
            if (slot.id == slotId) {
                slot.copy(uri = null, fileName = null, fileSize = null)
            } else slot
        }
    }

    fun submitApplication(onSuccess: (String) -> Unit) {
        _formValidationError.value = null
        val name = _formApplicantName.value.trim()
        val mobile = _formMobile.value.trim()

        if (name.isEmpty()) {
            _formValidationError.value = "कृपया आवेदक का पूरा नाम दर्ज करें"
            return
        }
        if (mobile.length < 10) {
            _formValidationError.value = "कृपया 10 अंकों का वैध मोबाइल नंबर दर्ज करें"
            return
        }

        val hasMandatoryDocs = _documentSlots.value
            .filter { it.isMandatory }
            .any { it.uri != null }

        // We allow submission even if one doc is attached, or simulated stage
        val attachedDocs = _documentSlots.value.filter { it.uri != null }
        val docNames = if (attachedDocs.isNotEmpty()) {
            attachedDocs.joinToString(", ") { it.fileName ?: it.label }
        } else {
            "Aadhaar Card (Offline Submission Stage at Kiosk)"
        }

        val selectedService = EmitraDataProvider.services.find { it.id == _formServiceId.value }
            ?: EmitraDataProvider.services.first()

        val generatedToken = "DC-2026-" + (1000 + Random.nextInt(9000))
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("hi", "IN"))
        val estDate = dateFormat.format(Date(now + (5 * 24 * 60 * 60 * 1000L)))

        _isSubmitting.value = true
        viewModelScope.launch {
            val newApp = ApplicationEntity(
                tokenNumber = generatedToken,
                applicantName = name,
                mobileNumber = mobile,
                janAadhaarNumber = _formJanAadhaar.value.trim(),
                serviceId = selectedService.id,
                serviceTitle = selectedService.titleHi,
                status = "SUBMITTED",
                submissionTimestamp = now,
                estimatedDate = estDate,
                remarks = "आवेदन व दस्तावेज दीपक सेंटर, सिगडोला बड़ा पर सफलतापूर्वक दर्ज। SSO पोर्टल पर प्रक्रिया जारी है।",
                village = _formVillage.value.trim(),
                documentNames = docNames,
                documentUris = attachedDocs.mapNotNull { it.uri }.joinToString(",")
            )

            repository.insertApplication(newApp)
            _isSubmitting.value = false
            _latestSubmittedToken.value = generatedToken
            _activeTrackedApplication.value = newApp
            _trackingSearchInput.value = generatedToken

            // Reset form fields
            _formApplicantName.value = ""
            _formMobile.value = ""
            _formJanAadhaar.value = ""
            _documentSlots.value = getDefaultDocumentSlots()

            onSuccess(generatedToken)
        }
    }

    fun dismissSuccessDialog() {
        _latestSubmittedToken.value = null
    }

    fun getTimelineStepsForApplication(app: ApplicationEntity): List<ApplicationTimelineStep> {
        val submissionDate = SimpleDateFormat("dd MMM, hh:mm a", Locale.ENGLISH)
            .format(Date(app.submissionTimestamp))
        val currentStatus = try {
            ApplicationStatus.valueOf(app.status)
        } catch (e: Exception) {
            ApplicationStatus.SUBMITTED
        }
        val currentStep = currentStatus.stepIndex

        return listOf(
            ApplicationTimelineStep(
                title = "1. दस्तावेज प्राप्त (Received at Deepak Center)",
                description = "दीपक सेंटर, सिगडोला बड़ा पर आवेदक से दस्तावेज व फोटो प्राप्त कर प्रारंभिक जांच की गई।",
                date = submissionDate,
                isCompleted = currentStep >= 1,
                isCurrent = currentStep == 1
            ),
            ApplicationTimelineStep(
                title = "2. ई-मित्र SSO पोर्टल पर फॉर्म प्रविष्टि",
                description = "राजस्थान सरकार ई-मित्र पोर्टल पर ऑनलाइन फॉर्म फीडिंग एवं निर्धारित टोकन शुल्क काटा गया।",
                date = if (currentStep >= 2) "पूर्ण (Processed)" else "प्रतीक्षित",
                isCompleted = currentStep >= 2,
                isCurrent = currentStep == 2
            ),
            ApplicationTimelineStep(
                title = "3. संबंधित विभाग / तहसीलदार स्तर पर जांच",
                description = "सीकर जिला/तहसील कार्यालय या संबंधित अधिकारी द्वारा पात्रता व दस्तावेजों का सत्यापन।",
                date = if (currentStep >= 3) "जांच जारी (In Review)" else "प्रतीक्षित",
                isCompleted = currentStep >= 3,
                isCurrent = currentStep == 4 // AT_TEHSILDAR_OFFICE
            ),
            ApplicationTimelineStep(
                title = "4. डिजिटल हस्ताक्षर व अनुमोदन (Approved)",
                description = "प्राधिकृत अधिकारी द्वारा डिजिटल हस्ताक्षर (DSC) संलग्न कर प्रमाण पत्र स्वीकृत किया गया।",
                date = if (currentStep >= 5) "स्वीकृत (Approved)" else "प्रतीक्षित",
                isCompleted = currentStep >= 5,
                isCurrent = currentStep == 5
            ),
            ApplicationTimelineStep(
                title = "5. प्रिंट व वितरण हेतु तैयार (Ready at Kiosk)",
                description = "मूल वाटरमार्क युक्त ई-मित्र स्टेशनरी पर प्रिंट उपलब्ध। दीपक सेंटर से प्राप्त करें।",
                date = if (currentStep >= 6) app.estimatedDate else "प्रतीक्षित",
                isCompleted = currentStep >= 6,
                isCurrent = currentStep == 6
            )
        )
    }
}
