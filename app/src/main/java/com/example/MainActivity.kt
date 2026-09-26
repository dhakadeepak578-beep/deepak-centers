package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.EmitraDataProvider
import com.example.ui.components.App3DBackgroundContainer
import com.example.ui.components.AppBottomNavigation
import com.example.ui.components.FarewellExitDialog
import com.example.ui.components.TopHeaderBar
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.CenterInfoScreen
import com.example.ui.screens.DocumentChecklistScreen
import com.example.ui.screens.ServiceDetailSheet
import com.example.ui.screens.ServicesScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.screens.UpdatesScreen
import com.example.ui.screens.UploadApplyScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.FarewellSpeechHelper
import com.example.viewmodel.AppTab
import com.example.viewmodel.EmitraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FarewellSpeechHelper.init(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                DeepakCenterApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FarewellSpeechHelper.shutdown()
    }
}

@Composable
fun DeepakCenterApp(viewModel: EmitraViewModel = viewModel()) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val serviceSearchQuery by viewModel.serviceSearchQuery.collectAsStateWithLifecycle()
    val selectedServiceDetail by viewModel.selectedServiceDetail.collectAsStateWithLifecycle()

    val allApplications by viewModel.allApplications.collectAsStateWithLifecycle()
    val trackingSearchInput by viewModel.trackingSearchInput.collectAsStateWithLifecycle()
    val activeTrackedApplication by viewModel.activeTrackedApplication.collectAsStateWithLifecycle()
    val trackingSearchError by viewModel.trackingSearchError.collectAsStateWithLifecycle()

    val formServiceId by viewModel.formServiceId.collectAsStateWithLifecycle()
    val formApplicantName by viewModel.formApplicantName.collectAsStateWithLifecycle()
    val formMobile by viewModel.formMobile.collectAsStateWithLifecycle()
    val formJanAadhaar by viewModel.formJanAadhaar.collectAsStateWithLifecycle()
    val formVillage by viewModel.formVillage.collectAsStateWithLifecycle()
    val documentSlots by viewModel.documentSlots.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val latestSubmittedToken by viewModel.latestSubmittedToken.collectAsStateWithLifecycle()
    val formValidationError by viewModel.formValidationError.collectAsStateWithLifecycle()
    val checklistServiceId by viewModel.checklistServiceId.collectAsStateWithLifecycle()

    val govNewsFeedItems by viewModel.govNewsFeedItems.collectAsStateWithLifecycle()
    val isGovNewsRefreshing by viewModel.isGovNewsRefreshing.collectAsStateWithLifecycle()
    val govNewsLastSync by viewModel.govNewsLastSync.collectAsStateWithLifecycle()

    var showFarewellDialog by remember { mutableStateOf(false) }

    // Handle Android system Back button with peaceful Indian cultural farewell
    BackHandler(enabled = true) {
        if (showFarewellDialog) {
            showFarewellDialog = false
        } else if (selectedServiceDetail != null) {
            viewModel.showServiceDetail(null)
        } else if (currentTab != AppTab.SERVICES) {
            viewModel.selectTab(AppTab.SERVICES)
        } else {
            // User is on home screen and pressed back -> Play peaceful Hindi farewell & show dialog
            showFarewellDialog = true
            FarewellSpeechHelper.speakFarewell(context)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopHeaderBar(
                kiosk = viewModel.kioskDetails,
                onTabSelect = { viewModel.selectTab(it) }
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        App3DBackgroundContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            showWatermark = true
        ) {
            when (currentTab) {
                AppTab.SERVICES -> {
                    ServicesScreen(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { viewModel.setCategory(it) },
                        searchQuery = serviceSearchQuery,
                        onSearchQueryChanged = { viewModel.setServiceSearchQuery(it) },
                        onServiceSelected = { viewModel.showServiceDetail(it) },
                        onApplyService = { viewModel.startApplicationForService(it) },
                        onViewAllUpdates = { viewModel.selectTab(AppTab.UPDATES) },
                        onOpenChecklist = { viewModel.selectTab(AppTab.CHECKLIST) },
                        onOpenChecklistForService = { viewModel.openChecklistForService(it.id) }
                    )
                }

                AppTab.AI_ASSISTANT -> {
                    AiAssistantScreen(kiosk = viewModel.kioskDetails)
                }

                AppTab.CHECKLIST -> {
                    DocumentChecklistScreen(
                        initialServiceId = checklistServiceId,
                        kiosk = viewModel.kioskDetails,
                        onApplyForService = { service ->
                            viewModel.startApplicationForService(service)
                        }
                    )
                }

                AppTab.UPDATES -> {
                    UpdatesScreen(
                        kiosk = viewModel.kioskDetails,
                        onApplyForServiceId = { serviceId ->
                            val srv = EmitraDataProvider.services.find { it.id == serviceId }
                                ?: EmitraDataProvider.services.first()
                            viewModel.startApplicationForService(srv)
                        },
                        govNewsItems = govNewsFeedItems,
                        isRefreshingGovNews = isGovNewsRefreshing,
                        lastSyncTimeGovNews = govNewsLastSync,
                        onRefreshGovNews = { viewModel.refreshGovNews() }
                    )
                }

                AppTab.TRACKING -> {
                    val timelineSteps = if (activeTrackedApplication != null) {
                        viewModel.getTimelineStepsForApplication(activeTrackedApplication!!)
                    } else {
                        emptyList()
                    }

                    TrackingScreen(
                        searchInput = trackingSearchInput,
                        onSearchInputChanged = { viewModel.setTrackingSearchInput(it) },
                        onSearchClicked = { query -> viewModel.searchTracking(query) },
                        trackedApplication = activeTrackedApplication,
                        allApplications = allApplications,
                        onSelectApplication = { viewModel.selectTrackedItem(it) },
                        timelineSteps = timelineSteps,
                        errorMessage = trackingSearchError,
                        kiosk = viewModel.kioskDetails
                    )
                }

                AppTab.APPLY_UPLOAD -> {
                    UploadApplyScreen(
                        formServiceId = formServiceId,
                        onServiceChanged = { viewModel.updateFormService(it) },
                        applicantName = formApplicantName,
                        onApplicantNameChanged = { viewModel.updateFormApplicantName(it) },
                        mobile = formMobile,
                        onMobileChanged = { viewModel.updateFormMobile(it) },
                        janAadhaar = formJanAadhaar,
                        onJanAadhaarChanged = { viewModel.updateFormJanAadhaar(it) },
                        village = formVillage,
                        onVillageChanged = { viewModel.updateFormVillage(it) },
                        documentSlots = documentSlots,
                        onAttachDocument = { slotId, uri, name, size ->
                            viewModel.attachDocument(slotId, uri, name, size)
                        },
                        onRemoveDocument = { slotId ->
                            viewModel.removeDocument(slotId)
                        },
                        onSubmitForm = {
                            viewModel.submitApplication { token ->
                                // Callback handled by state
                            }
                        },
                        isSubmitting = isSubmitting,
                        latestSubmittedToken = latestSubmittedToken,
                        onDismissSuccessDialog = { viewModel.dismissSuccessDialog() },
                        onTrackToken = { token ->
                            viewModel.searchTracking(token)
                            viewModel.selectTab(AppTab.TRACKING)
                        },
                        validationError = formValidationError,
                        kiosk = viewModel.kioskDetails
                    )
                }

                AppTab.CENTER_INFO -> {
                    CenterInfoScreen(kiosk = viewModel.kioskDetails)
                }
            }

            // Bottom sheet for service details
            if (selectedServiceDetail != null) {
                ServiceDetailSheet(
                    service = selectedServiceDetail,
                    kiosk = viewModel.kioskDetails,
                    onDismiss = { viewModel.showServiceDetail(null) },
                    onProceedToApply = { srv -> viewModel.startApplicationForService(srv) },
                    onOpenChecklist = { srv -> viewModel.openChecklistForService(srv.id) }
                )
            }

            // Peaceful Indian Cultural Farewell Dialog
            if (showFarewellDialog) {
                FarewellExitDialog(
                    onConfirmExit = {
                        (context as? Activity)?.finish()
                    },
                    onDismiss = {
                        showFarewellDialog = false
                    },
                    onReplayAudio = {
                        FarewellSpeechHelper.speakFarewell(context)
                    }
                )
            }
        }
    }
}
