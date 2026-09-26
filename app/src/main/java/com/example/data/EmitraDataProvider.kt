package com.example.data

import com.example.model.EmitraService
import com.example.model.RecentUpdate
import com.example.model.ServiceCategory
import com.example.model.UpdateBadge

object EmitraDataProvider {

    val services: List<EmitraService> = listOf(
        EmitraService(
            id = "caste_obc",
            titleHi = "जाति प्रमाण पत्र (OBC / अन्य पिछड़ा वर्ग)",
            titleEn = "Caste Certificate - OBC Category",
            category = ServiceCategory.CERTIFICATES,
            department = "राजस्व विभाग (Revenue Department, Rajasthan)",
            feeInfo = "₹50 सरकारी टोकन + ₹50 कियोस्क शुल्क",
            processingDays = "5 - 7 कार्य दिवस",
            requiredDocuments = listOf(
                "प्रार्थी का आधार कार्ड",
                "जन आधार कार्ड (Jan Aadhaar)",
                "पिता का जाति प्रमाण पत्र / पुरानी रजिस्ट्री या 1972 का रिकॉर्ड",
                "प्रार्थी का मूल निवास प्रमाण पत्र",
                "1 वर्ष का आय प्रमाण पत्र (Income Certificate)",
                "नवीनतम पासपोर्ट साइज फोटो",
                "स्व-घोषणा पत्र (दीपक सेंटर पर उपलब्ध)"
            ),
            eligibility = "राजस्थान का मूल निवासी एवं संबंधित आरक्षित वर्ग का नागरिक।",
            instructions = "फॉर्म ऑनलाइन सबमिट होने के बाद संबंधित पटवारी व तहसीलदार द्वारा जांच कर डिजिटल हस्ताक्षर जारी किए जाते हैं।",
            isPopular = true
        ),
        EmitraService(
            id = "caste_sc_st",
            titleHi = "जाति प्रमाण पत्र (SC / ST - अनुसूचित जाति / जनजाति)",
            titleEn = "Caste Certificate - SC / ST Category",
            category = ServiceCategory.CERTIFICATES,
            department = "राजस्व विभाग (Revenue Department, Rajasthan)",
            feeInfo = "₹50 सरकारी टोकन + ₹50 कियोस्क शुल्क",
            processingDays = "3 - 5 कार्य दिवस",
            requiredDocuments = listOf(
                "प्रार्थी का आधार कार्ड",
                "जन आधार कार्ड",
                "पिता / दादा का जाति का पुराना रिकॉर्ड या भूमि जमाबंदी",
                "पासपोर्ट साइज रंगीन फोटो",
                "राशन कार्ड प्रति"
            ),
            eligibility = "राजस्थान राज्य की अनुसूचित जाति / जनजाति का स्थाई नागरिक।",
            instructions = "एससी/एसटी प्रमाण पत्र डिजिटल रूप से आजीवन मान्य होता है।",
            isPopular = true
        ),
        EmitraService(
            id = "caste_ews",
            titleHi = "ईडब्ल्यूएस प्रमाण पत्र (EWS Certificate - सामान्य वर्ग)",
            titleEn = "Economically Weaker Section (EWS) Certificate",
            category = ServiceCategory.CERTIFICATES,
            department = "राजस्व विभाग (Revenue Department)",
            feeInfo = "₹50 सरकारी शुल्क + ₹50 कियोस्क शुल्क",
            processingDays = "5 - 7 कार्य दिवस",
            requiredDocuments = listOf(
                "प्रार्थी एवं माता-पिता का आधार कार्ड",
                "जन आधार कार्ड",
                "पटवारी द्वारा सत्यापित 4 पेज का आय प्रमाण पत्र (वार्षिक आय 8 लाख से कम)",
                "भूमि रिकॉर्ड (जमाबंदी) या आवासीय भूखंड का पट्टा",
                "राशन कार्ड व पासपोर्ट फोटो"
            ),
            eligibility = "सामान्य वर्ग के गैर-आरक्षित परिवार जिनकी कुल वार्षिक आय 8 लाख से कम हो।",
            instructions = "ईडब्ल्यूएस प्रमाण पत्र 3 वित्तीय वर्ष तक वैध शपथ पत्र के साथ मान्य रहता है।",
            isPopular = true
        ),
        EmitraService(
            id = "domicile_bonafide",
            titleHi = "मूल निवास प्रमाण पत्र (Bonafide Domicile)",
            titleEn = "Bonafide Resident / Domicile Certificate",
            category = ServiceCategory.CERTIFICATES,
            department = "राजस्व विभाग (Revenue Department, Rajasthan)",
            feeInfo = "₹50 सरकारी शुल्क + ₹50 कियोस्क शुल्क",
            processingDays = "4 - 6 कार्य दिवस",
            requiredDocuments = listOf(
                "प्रार्थी का आधार कार्ड",
                "जन आधार कार्ड",
                "10 वर्ष पुराना निवास साक्ष्य (पुराना बिजली बिल / मतदाता सूची / भूमि पट्टा)",
                "शैक्षणिक योग्यता अंकतालिका (10वीं/12वीं)",
                "पासपोर्ट साइज फोटो"
            ),
            eligibility = "राजस्थान में कम से कम 10 वर्षों से निरंतर निवास कर रहे नागरिक।",
            instructions = "डिजिटल हस्ताक्षरित मूल निवास प्रमाण पत्र आजीवन मान्य होता है।",
            isPopular = true
        ),
        EmitraService(
            id = "jan_aadhaar_kyc",
            titleHi = "जन आधार ई-केवाईसी (Jan Aadhaar Mandatory E-KYC)",
            titleEn = "Jan Aadhaar Biometric / OTP E-KYC",
            category = ServiceCategory.JAN_AADHAAR,
            department = "आयोजना (जन आधार) प्राधिकरण, DOIT&C",
            feeInfo = "₹0 सरकारी शुल्क + ₹30 कियोस्क सेवा शुल्क",
            processingDays = "तत्काल (Instant)",
            requiredDocuments = listOf(
                "जन आधार कार्ड संख्या (Jan Aadhaar ID / Slip)",
                "परिवार के सभी सदस्यों (5 वर्ष से अधिक) का आधार कार्ड",
                "आधार से लिंक मोबाइल नंबर (ओटीपी हेतु) या बायोमेट्रिक फिंगरप्रिंट"
            ),
            eligibility = "राजस्थान के समस्त जन आधार कार्ड धारक परिवार।",
            instructions = "ई-केवाईसी न होने पर राशन, छात्रवृत्ति व पेंशन आदि सरकारी लाभ स्वतः बंद हो सकते हैं। दीपक सेंटर पर फिंगरप्रिंट से तत्काल ई-केवाईसी कराएं।",
            isPopular = true
        ),
        EmitraService(
            id = "jan_aadhaar_addition",
            titleHi = "जन आधार सदस्य जोड़ना / संशोधन (Jan Aadhaar Add/Edit)",
            titleEn = "Jan Aadhaar Member Addition & Correction",
            category = ServiceCategory.JAN_AADHAAR,
            department = "आयोजना (जन आधार) प्राधिकरण",
            feeInfo = "₹50 सरकारी टोकन + ₹40 कियोस्क शुल्क",
            processingDays = "7 - 10 कार्य दिवस",
            requiredDocuments = listOf(
                "मूल जन आधार कार्ड",
                "नए सदस्य का जन्म प्रमाण पत्र या विवाह प्रमाण पत्र",
                "नए सदस्य का आधार कार्ड",
                "महिला मुखिया की बैंक पासबुक"
            ),
            eligibility = "परिवार में नया जन्म, विवाह या पते/नाम में संशोधन हेतु।",
            instructions = "कियोस्क द्वारा आवेदन के बाद सांख्यिकी अधिकारी / बीडीओ कार्यालय से सत्यापन होता है।",
            isPopular = true
        ),
        EmitraService(
            id = "pension_annual_kyc",
            titleHi = "सामाजिक सुरक्षा पेंशन वार्षिक सत्यापन (Pension Annual KYC)",
            titleEn = "Social Security Pension Annual Life Certificate Verification",
            category = ServiceCategory.PENSIONS_SCHEMES,
            department = "सामाजिक न्याय एवं अधिकारिता विभाग (SJE)",
            feeInfo = "निःशुल्क सरकारी + ₹30 कियोस्क बायोमेट्रिक शुल्क",
            processingDays = "तत्काल (Instant)",
            requiredDocuments = listOf(
                "पेंशन पीपीओ (PPO) नंबर",
                "जन आधार कार्ड",
                "पेंशनर का आधार कार्ड",
                "बायोमेट्रिक फिंगरप्रिंट या आइरिस/फेस स्कैन"
            ),
            eligibility = "वृद्धावस्था, एकल नारी / विधवा, एवं दिव्यांग पेंशन के समस्त लाभार्थी।",
            instructions = "प्रतिवर्ष नवंबर-दिसंबर में भौतिक सत्यापन अनिवार्य है। यदि बायोमेट्रिक नहीं आता तो दीपक सेंटर पर फेस रिकग्निशन से सत्यापन उपलब्ध है।",
            isPopular = true
        ),
        EmitraService(
            id = "pm_kisan_ekyc",
            titleHi = "पीएम किसान सम्मान निधि (PM-Kisan E-KYC & Land Seeding)",
            titleEn = "PM-Kisan Samman Nidhi E-KYC & DBT",
            category = ServiceCategory.PENSIONS_SCHEMES,
            department = "कृषि एवं किसान कल्याण मंत्रालय",
            feeInfo = "₹15 पोर्टल टोकन + ₹35 कियोस्क सेवा शुल्क",
            processingDays = "तत्काल (Instant)",
            requiredDocuments = listOf(
                "किसान का आधार कार्ड",
                "पीएम किसान रजिस्ट्रेशन नंबर / मोबाइल",
                "जमाबंदी नकल (खसरा नंबर सहित)",
                "बैंक खाता पासबुक (NPCI / DBT सक्रिय)"
            ),
            eligibility = "भूमिधारी पात्र कृषक परिवार।",
            instructions = "किस्त खाते में प्राप्त करने हेतु आधार-एनपीसीआई लिंकिंग एवं लैंड सीडिंग दीपक सेंटर पर जांची जाती है।",
            isPopular = true
        ),
        EmitraService(
            id = "shramik_card",
            titleHi = "श्रमिक कार्ड / लेबर कार्ड (Rajasthan Labour Card)",
            titleEn = "BOCW Worker Welfare Board Labour Card",
            category = ServiceCategory.PENSIONS_SCHEMES,
            department = "श्रम कल्याण मंडल (Labour Department Rajasthan)",
            feeInfo = "₹95 सरकारी शुल्क + ₹50 कियोस्क शुल्क",
            processingDays = "15 - 20 कार्य दिवस",
            requiredDocuments = listOf(
                "90 दिन निर्माण कार्य का प्रमाण पत्र (नरेगा जॉब कार्ड या पंजीकृत ठेकेदार डायरी)",
                "जन आधार कार्ड",
                "आधार कार्ड",
                "बैंक पासबुक",
                "पासपोर्ट फोटो"
            ),
            eligibility = "18 से 60 वर्ष आयु वर्ग के निर्माण श्रमिक, मिस्त्री, प्लंबर, इलेक्ट्रीशियन, नरेगा मजदूर।",
            instructions = "कार्ड बनने के बाद बच्चों की छात्रवृत्ति (₹8,000-₹35,000), प्रसूति हितलाभ व आवास अनुदान हेतु पात्र होते हैं।",
            isPopular = false
        ),
        EmitraService(
            id = "jamabandi_nakal",
            titleHi = "जमाबंदी नकल व नामांतरण (Bhulekh Jamabandi & Mutation)",
            titleEn = "Land Record Jamabandi Nakal & Mutation",
            category = ServiceCategory.REVENUE_LAND,
            department = "राजस्व मंडल (Board of Revenue, Ajmer)",
            feeInfo = "₹20 सरकारी नकल शुल्क + ₹20 प्रिंटिंग शुल्क",
            processingDays = "तत्काल (Instant Print)",
            requiredDocuments = listOf(
                "खाता संख्या या खसरा संख्या या काश्तकार का नाम",
                "जिला: सीकर, तहसील व ग्राम: सिगडोला बड़ा या संबंधित क्षेत्र"
            ),
            eligibility = "कोई भी काश्तकार या भूमि स्वामी।",
            instructions = "डिजिटल हस्ताक्षरित आधिकारिक जमाबंदी नकल बैंक लोन, रजिस्ट्री व सरकारी योजनाओं हेतु पूर्णतः वैध है।",
            isPopular = true
        ),
        EmitraService(
            id = "pan_card_service",
            titleHi = "नया पैन कार्ड / संशोधन (UTIITSL / NSDL PAN Card)",
            titleEn = "New PAN Card & PAN Correction",
            category = ServiceCategory.CERTIFICATES,
            department = "आयकर विभाग (Income Tax Department, India)",
            feeInfo = "₹107 सरकारी शुल्क + ₹50 कियोस्क शुल्क",
            processingDays = "3 - 5 दिन में ई-पैन, 10-12 दिन में घर पर डिलीवरी",
            requiredDocuments = listOf(
                "आधार कार्ड (Aadhaar Card)",
                "2 नवीनतम पासपोर्ट साइज फोटो",
                "हस्ताक्षर नमूना (Signature Slip)"
            ),
            eligibility = "कोई भी भारतीय नागरिक।",
            instructions = "दीपक सेंटर पर बायोमेट्रिक या फोटो अपलोड दोनों माध्यम से तुरंत पैन कार्ड फॉर्म भरा जाता है।",
            isPopular = true
        ),
        EmitraService(
            id = "bijli_pani_bills",
            titleHi = "बिजली व पानी बिल भुगतान (JVVNL Electricity & PHED Water)",
            titleEn = "Electricity & Water Utility Bill Payments",
            category = ServiceCategory.BILLS_BANKING,
            department = "जयपुर डिस्कॉम (JVVNL) एवं जन स्वास्थ्य अभियांत्रिकी (PHED)",
            feeInfo = "निःशुल्क सेवा (0 Extra Charge on Bills)",
            processingDays = "तत्काल रसीद जारी (Instant Receipt)",
            requiredDocuments = listOf(
                "बिजली बिल का K-Number (के-संख्या)",
                "पानी बिल का उपभोक्ता आईडी / मीटर नंबर"
            ),
            eligibility = "समस्त उपभोक्ता।",
            instructions = "तय तारीख से पहले बिल भरने पर छूट प्राप्त करें। तुरंत कम्प्यूटरीकृत वैध ई-मित्र रसीद दी जाती है।",
            isPopular = true
        ),
        EmitraService(
            id = "aeps_banking",
            titleHi = "AEPS आधार माइक्रो एटीएम व मनी ट्रांसफर (Cash Withdrawal)",
            titleEn = "Aadhaar Enabled Payment System (AEPS Banking)",
            category = ServiceCategory.BILLS_BANKING,
            department = "NPCI & बैंकिंग बिज़नेस कॉरेस्पोंडेंट (BC)",
            feeInfo = "बैंक नियमानुसार",
            processingDays = "तत्काल नकद (Instant Cash)",
            requiredDocuments = listOf(
                "आधार कार्ड नंबर",
                "बैंक का नाम",
                "ग्राहक का फिंगरप्रिंट (अंगूठा)"
            ),
            eligibility = "जिस ग्राहक का बैंक खाता आधार से लिंक है।",
            instructions = "सीकर जिले के किसी भी बैंक खाते से नकद निकासी, बैलेंस पूछताछ व मिनी स्टेटमेंट की सुविधा।",
            isPopular = true
        ),
        EmitraService(
            id = "scholarship_postmatric",
            titleHi = "उत्तर मैट्रिक छात्रवृत्ति (Post Matric Scholarship - SJE)",
            titleEn = "Rajasthan Uttar Matric Scholarship Online Application",
            category = ServiceCategory.EMPLOYMENT_EXAMS,
            department = "सामाजिक न्याय एवं अधिकारिता विभाग (SJE Rajasthan)",
            feeInfo = "₹0 सरकारी पोर्टल + ₹60 कियोस्क फॉर्म शुल्क",
            processingDays = "कॉलेज व विभाग सत्यापन अनुसार",
            requiredDocuments = listOf(
                "जन आधार कार्ड व आधार कार्ड",
                "10वीं, 12वीं या पिछली उत्तीर्ण कक्षा की मूल अंकतालिका",
                "कॉलेज / आईटीआई की चालू वर्ष की फीस रसीद",
                "मूल निवास व जाति प्रमाण पत्र",
                "सत्यापित आय प्रमाण पत्र"
            ),
            eligibility = "SC, ST, OBC, SBC, EBC एवं अल्पसंख्यक छात्र-छात्राएं जो मान्यता प्राप्त कॉलेज/संस्थान में नियमित अध्ययनरत हैं।",
            instructions = "फॉर्म भरने से पहले जन आधार में बैंक खाता व आय विवरण अपडेट होना आवश्यक है।",
            isPopular = true
        ),
        EmitraService(
            id = "govt_job_form",
            titleHi = "सरकारी नौकरी ऑनलाइन फॉर्म (RSMSSB, RPSC, CET, Police, Army)",
            titleEn = "Government Job & Competitive Exam Form Fillup",
            category = ServiceCategory.EMPLOYMENT_EXAMS,
            department = "विभिन्न भर्ती आयोग (RSMSSB / RPSC / SSC / Railway / Army)",
            feeInfo = "भर्ती विज्ञप्ति अनुसार शुल्क + ₹50 कियोस्क शुल्क",
            processingDays = "तत्काल प्रिंट रसीद (Instant Confirmation Slip)",
            requiredDocuments = listOf(
                "एसएसओ आईडी (SSO ID) व वन टाइम रजिस्ट्रेशन (OTR) विवरण",
                "शैक्षणिक योग्यता अंकतालिकाएं",
                "जाति व मूल निवास प्रमाण पत्र",
                "पासपोर्ट फोटो व हस्ताक्षर"
            ),
            eligibility = "संबंधित भर्ती परीक्षा की अधिसूचना अनुसार।",
            instructions = "दीपक सेंटर पर त्रुटिहीन व सावधानीपूर्वक ऑनलाइन फॉर्म भरने की पूरी जिम्मेदारी ली जाती है।",
            isPopular = true
        ),
        EmitraService(
            id = "police_character",
            titleHi = "चरित्र प्रमाण पत्र / पुलिस सत्यापन (Police Clearance - PCC)",
            titleEn = "Police Character Verification Certificate",
            category = ServiceCategory.CERTIFICATES,
            department = "राजस्थान पुलिस विभाग",
            feeInfo = "₹243 सरकारी शुल्क + ₹50 कियोस्क शुल्क",
            processingDays = "7 - 10 कार्य दिवस",
            requiredDocuments = listOf(
                "प्रार्थी का आधार कार्ड",
                "जन आधार कार्ड",
                "पहचान पत्र (वोटर आईडी / ड्राइविंग लाइसेंस)",
                "पासपोर्ट फोटो",
                "क्षेत्र के 2 प्रतिष्ठित व्यक्तियों के गवाह फॉर्म"
            ),
            eligibility = "नौकरी, पासपोर्ट, सिक्योरिटी या ठेकेदारी हेतु चरित्र प्रमाण चाहने वाले नागरिक।",
            instructions = "संबंधित स्थानीय पुलिस थाना (धोद / सीकर) द्वारा मौका सत्यापन के बाद एसपी कार्यालय से जारी।",
            isPopular = false
        )
    )

    val updates: List<RecentUpdate> = listOf(
        RecentUpdate(
            id = "up_jan_aadhaar_kyc",
            title = "जन आधार ई-केवाईसी (E-KYC) अंतिम चेतावनी - तुरंत पूर्ण कराएं",
            badge = UpdateBadge.URGENT,
            department = "आयोजना विभाग, राजस्थान सरकार",
            date = "25 सितंबर 2026",
            summary = "परिवार के सभी 5 वर्ष से अधिक उम्र के सदस्यों का ई-केवाईसी आवश्यक है। बिना ई-केवाईसी राशन व पेंशन रुक सकती है।",
            fullDetails = "राजस्थान सरकार द्वारा जन आधार कार्ड में सभी सदस्यों की बायोमेट्रिक या आधार ओटीपी के माध्यम से ई-केवाईसी पूर्ण करना अनिवार्य कर दिया गया है। जिन परिवारों की ई-केवाईसी पूर्ण नहीं होगी, उनके गेहूं आवंटन, सामाजिक सुरक्षा पेंशन तथा छात्रवृत्ति की प्रक्रिया अवरुद्ध हो सकती है। दीपक सेंटर, सिगडोला बड़ा पर आकर तुरंत अपनी ई-केवाईसी करवाएं।",
            relatedServiceId = "jan_aadhaar_kyc"
        ),
        RecentUpdate(
            id = "up_pm_kisan",
            title = "पीएम किसान सम्मान निधि 19वीं किस्त - लैंड सीडिंग व DBT अनिवार्य",
            badge = UpdateBadge.NEW,
            department = "कृषि मंत्रालय, भारत सरकार",
            date = "23 सितंबर 2026",
            summary = "जिन किसानों का बैंक DBT और लैंड सीडिंग बाकी है, वे दीपक सेंटर पर आकर तुरंत स्टेटस चेक करवाएं।",
            fullDetails = "आगामी 19वीं किस्त का पैसा सीधे बैंक खाते में पाने के लिए किसान भाइयों को अपने बैंक खाते में आधार एनपीसीआई (DBT) लिंक कराना तथा पोर्टल पर लैंड सीडिंग (भूमि सत्यापन) पूरा करना अनिवार्य है। दीपक सेंटर पर आकर आप अपनी पात्रता व ई-केवाईसी की पुष्टि कर सकते हैं।",
            relatedServiceId = "pm_kisan_ekyc"
        ),
        RecentUpdate(
            id = "up_scholarship_extend",
            title = "उत्तर मैट्रिक छात्रवृत्ति 2026-27 आवेदन तिथि 15 अक्टूबर तक बढ़ाई गई",
            badge = UpdateBadge.EXTENDED,
            department = "सामाजिक न्याय एवं अधिकारिता विभाग (SJE)",
            date = "21 सितंबर 2026",
            summary = "कक्षा 11, 12, आईटीआई, पॉलिटेक्निक और कॉलेज छात्र-छात्राओं हेतु छात्रवृत्ति पोर्टल पुनः सक्रिय।",
            fullDetails = "शैक्षणिक सत्र 2026-27 के लिए उत्तर मैट्रिक छात्रवृत्ति योजना की अंतिम तिथि बढ़ा दी गई है। सभी एससी, एसटी, ओबीसी, ईबीसी वर्ग के पात्र विद्यार्थी अपनी कॉलेज फीस रसीद, आय प्रमाण पत्र व जन आधार के साथ दीपक सेंटर पर आकर आवेदन करवाएं।",
            relatedServiceId = "scholarship_postmatric"
        ),
        RecentUpdate(
            id = "up_cet_police",
            title = "राजस्थान पुलिस कांस्टेबल व CET 2026 ऑनलाइन आवेदन प्रक्रिया शुरू",
            badge = UpdateBadge.NEW,
            department = "कर्मचारी चयन बोर्ड (RSMSSB)",
            date = "19 सितंबर 2026",
            summary = "समान पात्रता परीक्षा (CET Senior Secondary / Graduation) एवं राजस्थान पुलिस के ऑनलाइन फॉर्म प्रारंभ।",
            fullDetails = "राजस्थान कर्मचारी चयन बोर्ड द्वारा नई रिक्तियों हेतु आवेदन आमंत्रित किए गए हैं। अभ्यर्थी वन टाइम रजिस्ट्रेशन (OTR) शुल्क जमा कर फॉर्म भर सकते हैं। दीपक सेंटर सिगडोला बड़ा पर उच्च गुणवत्ता स्कैनिंग व त्रुटिरहित फॉर्म भरने की सुविधा उपलब्ध है।",
            relatedServiceId = "govt_job_form"
        ),
        RecentUpdate(
            id = "up_pension_verify",
            title = "सामाजिक सुरक्षा पेंशनर्स हेतु वार्षिक भौतिक सत्यापन अभियान",
            badge = UpdateBadge.IMPORTANT,
            department = "सामाजिक सुरक्षा पेंशन विभाग राजस्थान",
            date = "15 सितंबर 2026",
            summary = "सभी वृद्धजन, एकल नारी व दिव्यांग पेंशनर अपना वार्षिक सत्यापन बायोमेट्रिक या फेस रिकग्निशन से कराएं।",
            fullDetails = "पेंशन निरंतर चालू रखने हेतु प्रत्येक पेंशनर का वार्षिक भौतिक सत्यापन आवश्यक है। वृद्धजनों को चलने-फिरने में असुविधा होने पर फेस ऑथेंटिकेशन या मोबाइल ऐप द्वारा सत्यापन की विशेष व्यवस्था दीपक सेंटर पर मौजूद है।",
            relatedServiceId = "pension_annual_kyc"
        ),
        RecentUpdate(
            id = "up_nfsa_ration",
            title = "राशन कार्ड में नए गेहूं आवंटन हेतु ई-केवाईसी व सदस्य नाम जांच",
            badge = UpdateBadge.URGENT,
            department = "खाद्य एवं नागरिक आपूर्ति विभाग",
            date = "12 सितंबर 2026",
            summary = "राष्ट्रीय खाद्य सुरक्षा योजना (NFSA) के लाभार्थियों को उचित मूल्य की दुकान या ई-मित्र पर सत्यापन जरूरी।",
            fullDetails = "यदि आपके परिवार के किसी सदस्य का नाम जन आधार या राशन कार्ड में कटा हुआ है या गेहूं नहीं मिल रहा है, तो तुरंत अपने आधार कार्ड की प्रति लेकर दीपक सेंटर पर संपर्क करें।",
            relatedServiceId = "jan_aadhaar_addition"
        )
    )
}
