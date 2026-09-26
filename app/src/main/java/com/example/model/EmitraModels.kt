package com.example.model

enum class ServiceCategory(val labelHi: String, val labelEn: String) {
    ALL("सभी सेवाएं", "All Services"),
    CERTIFICATES("प्रमाण पत्र", "Certificates"),
    JAN_AADHAAR("जन आधार / आधार", "Jan Aadhaar / Aadhaar"),
    PENSIONS_SCHEMES("पेंशन व योजनाएं", "Pensions & Schemes"),
    REVENUE_LAND("जमीन व राजस्व", "Land & Revenue"),
    BILLS_BANKING("बिल व बैंकिंग", "Bills & Banking"),
    EMPLOYMENT_EXAMS("नौकरी व छात्रवृत्ति", "Jobs & Scholarships")
}

data class EmitraService(
    val id: String,
    val titleHi: String,
    val titleEn: String,
    val category: ServiceCategory,
    val department: String,
    val feeInfo: String,
    val processingDays: String,
    val requiredDocuments: List<String>,
    val eligibility: String,
    val instructions: String,
    val isPopular: Boolean = false
)

enum class UpdateBadge(val text: String, val colorHex: Long) {
    URGENT("अंतिम तिथि नज़दीक", 0xFFD32F2F),
    NEW("नया अपडेट", 0xFF1976D2),
    LAST_DATE("लास्ट डेट", 0xFFE65100),
    EXTENDED("तारीख बढ़ी", 0xFF388E3C),
    IMPORTANT("ज़रूरी सूचना", 0xFF7B1FA2)
}

data class RecentUpdate(
    val id: String,
    val title: String,
    val badge: UpdateBadge,
    val department: String,
    val date: String,
    val summary: String,
    val fullDetails: String,
    val relatedServiceId: String? = null
)

enum class ApplicationStatus(val label: String, val stepIndex: Int) {
    SUBMITTED("दस्तावेज़ प्राप्त (Received at Kiosk)", 1),
    VERIFICATION_IN_PROGRESS("दस्तावेज़ सत्यापन (Verification)", 2),
    SUBMITTED_TO_PORTAL("SSO ई-मित्र पोर्टल पर दर्ज (Submitted to SSO)", 3),
    AT_TEHSILDAR_OFFICE("तहसीलदार / एसडीएम जांच (Tehsildar Scrutiny)", 4),
    APPROVED("स्वीकृत व डिजिटल हस्ताक्षर (Approved)", 5),
    READY_FOR_PRINT("प्रिंट व प्रमाण पत्र तैयार (Ready to Collect)", 6),
    REJECTED("संशोधन अपेक्षित (Correction Needed)", 0)
}

data class ApplicationTimelineStep(
    val title: String,
    val description: String,
    val date: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class KioskDetails(
    val name: String = "Deepak Centers",
    val title: String = "ई-मित्र एवं डिजिटल सेवा केंद्र",
    val operatorName: String = "Deepak Dhaka",
    val village: String = "Sigdola Bara (सिगडोला बड़ा)",
    val district: String = "Sikar (सीकर), Rajasthan",
    val pincode: String = "332312",
    val fullAddress: String = "दीपक सेंटर, मुख्य बस स्टैंड के पास, ग्राम सिगडोला बड़ा, जिला सीकर (राजस्थान) 332312",
    val kioskCode: String = "RAJ-SKR-332312-0042",
    val contactPhone: String = "+91 78785 13433",
    val whatsappNumber: String = "917878513433",
    val email: String = "deepakcenters@gmail.com",
    val timings: String = "सोमवार से शुक्रवार: प्रातः 9:00 AM से सायं 5:00 PM",
    val holidays: String = "शनिवार एवं रविवार: पूर्ण अवकाश (Closed on Sat & Sun)",
    val mapLocationQuery: String = "Sigdola Bara Sikar Rajasthan 332312"
)
