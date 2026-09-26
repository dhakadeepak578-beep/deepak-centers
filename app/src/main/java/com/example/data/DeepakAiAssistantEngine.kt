package com.example.data

import com.example.model.EmitraService
import com.example.model.KioskDetails
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: String = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
    val suggestedActions: List<String> = emptyList()
)

object DeepakAiAssistantEngine {

    val PEACEFUL_THANK_YOU = "\n\n🙏 *दीपक सेंटर्स (सिगडोला बड़ा, सीकर) पर संपर्क करने के लिए आपका बहुत-बहुत धन्यवाद! आपका दिन शुभ एवं मंगलमय हो। पुनः अवश्य पधारें!*"

    fun getInitialGreeting(): List<ChatMessage> {
        return listOf(
            ChatMessage(
                text = "नमस्ते! 🙏 मैं दीपक सेंटर्स का AI डिजिटल सहायक हूँ।\n\nमैं ई-मित्र सेवाओं, आवश्यक दस्तावेजों, सरकारी फीस, आवेदन प्रक्रिया और केंद्र के समय की जानकारी देने के लिए 24x7 उपलब्ध हूँ।\n\nआप मुझसे क्या पूछना चाहते हैं?",
                isUser = false,
                suggestedActions = listOf(
                    "केंद्र का समय व अवकाश?",
                    "जाति प्रमाण पत्र के दस्तावेज?",
                    "मूल निवास कैसे बनवाएं?",
                    "जन आधार KYC कैसे करें?",
                    "पेंशन सत्यापन की फीस?"
                )
            )
        )
    }

    fun answerQuery(query: String, kiosk: KioskDetails = KioskDetails()): ChatMessage {
        val q = query.trim().lowercase(Locale.getDefault())

        val replyText = when {
            // Timing / Hours / Holidays
            q.contains("समय") || q.contains("time") || q.contains("timing") || q.contains("कब खुल") || q.contains("छुट्टी") || q.contains("holiday") || q.contains("रविवार") || q.contains("शनिवार") -> {
                """
                ⏰ *दीपक सेंटर्स कार्य समय व अवकाश:*
                • *खुला:* ${kiosk.timings}
                • *अवकाश:* ${kiosk.holidays}
                • *स्थान:* ${kiosk.fullAddress}
                
                📌 *सलाह:* अवकाश के दिनों में भी आप इस ऐप के 'दस्तावेज़ भेजें' टैब या WhatsApp (7878513433) से ऑनलाइन आवेदन भेज सकते हैं।
                """.trimIndent()
            }

            // Contact / Phone / Email / Operator
            q.contains("फोन") || q.contains("phone") || q.contains("contact") || q.contains("नंबर") || q.contains("संपर्क") || q.contains("ईमेल") || q.contains("email") || q.contains("दीपक") -> {
                """
                📞 *दीपक सेंटर्स संपर्क सूत्र:*
                • *संचालक:* ${kiosk.operatorName}
                • *मोबाइल / WhatsApp:* ${kiosk.contactPhone}
                • *ईमेल:* ${kiosk.email}
                • *कियोस्क कोड:* ${kiosk.kioskCode}
                • *पता:* सिगडोला बड़ा, जिला सीकर (राजस्थान) 332312
                """.trimIndent()
            }

            // Caste Certificate
            q.contains("जाति") || q.contains("caste") || q.contains("obc") || q.contains("sc") || q.contains("st") || q.contains("ews") -> {
                """
                📜 *जाति प्रमाण पत्र (Caste Certificate) हेतु जानकारी:*
                • *आवश्यक दस्तावेज़:* 
                  1. जन आधार कार्ड व आधार कार्ड
                  2. पिता या परिवार का 1972/पुराना राजस्व रिकॉर्ड या जमाबंदी
                  3. सक्षम अधिकारी से सत्यापित 1-पेज आय प्रमाण पत्र
                  4. स्वयं की नवीनतम पासपोर्ट साइज फोटो
                • *सरकारी व कियोस्क शुल्क:* मात्र ₹50
                • *तैयारी समय:* 3 से 7 कार्य दिवस
                """.trimIndent()
            }

            // Domicile / Bonafide
            q.contains("मूल निवास") || q.contains("domicile") || q.contains("bonafide") -> {
                """
                🏡 *डिजिटल मूल निवास प्रमाण पत्र (Bonafide Certificate):*
                • *आवश्यक दस्तावेज़:* 
                  1. जन आधार कार्ड (महिला/पुरुष)
                  2. राजस्थान में 10 वर्ष से निवास का प्रमाण (बिजली बिल / वोटर आईडी / जमाबंदी)
                  3. शैक्षणिक अंकतालिका (10वीं या स्कूल प्रमाण)
                  4. पासपोर्ट साइज फोटो
                • *शुल्क:* ₹50
                • *समय:* 4 से 7 कार्य दिवस
                """.trimIndent()
            }

            // Jan Aadhaar KYC
            q.contains("जन आधार") || q.contains("jan aadhaar") || q.contains("kyc") || q.contains("ई-केवाईसी") -> {
                """
                🆔 *जन आधार ई-केवाईसी (Jan Aadhaar e-KYC):*
                • राज्य सरकार के निर्देशानुसार परिवार के सभी 5 वर्ष से अधिक उम्र के सदस्यों का ई-केवाईसी अनिवार्य है।
                • *आवश्यक:* जन आधार नंबर, सभी सदस्यों के आधार नंबर, आधार लिंक चालू मोबाइल (ओटीपी हेतु)।
                • बायोमेट्रिक फिंगरप्रिंट की सुविधा हमारे केंद्र सिगडोला बड़ा पर उपलब्ध है।
                """.trimIndent()
            }

            // Pension
            q.contains("पेंशन") || q.contains("pension") || q.contains("सत्यापन") -> {
                """
                👴 *सामाजिक सुरक्षा पेंशन व वार्षिक भौतिक सत्यापन:*
                • वृद्धावस्था, एकल नारी, विधवा एवं दिव्यांग पेंशन धारकों का वार्षिक बायोमेट्रिक/फेस सत्यापन केंद्र पर तुरंत किया जाता है।
                • *आवश्यक दस्तावेज़:* PPO नंबर, जन आधार कार्ड, आधार कार्ड, बैंक पासबुक।
                • *शुल्क:* मात्र ₹50
                """.trimIndent()
            }

            // Ration Card
            q.contains("राशन") || q.contains("ration") -> {
                """
                🌾 *राशन कार्ड सेवाएं (नाम जोड़ना / नया / संशोधन):*
                • *दस्तावेज़:* पुराना राशन कार्ड, सभी सदस्यों के आधार कार्ड, जन आधार कार्ड, बिजली बिल, मुखिया की फोटो।
                • *समय:* 7 से 15 कार्य दिवस।
                """.trimIndent()
            }

            // Land / Jamabandi
            q.contains("जमीन") || q.contains("जमाबंदी") || q.contains("नकल") || q.contains("खसरा") || q.contains("land") -> {
                """
                🚜 *जमाबंदी नकल एवं नामांतरण (ई-धरती):*
                • सिगडोला बड़ा, धोद, सीकर व संपूर्ण राजस्थान की प्रमाणित जमाबंदी नकल 2 मिनट में प्रिंट प्राप्त करें।
                • *आवश्यक:* खाता संख्या / खसरा नंबर अथवा काश्तकार का नाम।
                • *कियोस्क शुल्क:* ₹20 प्रति नकल
                """.trimIndent()
            }

            // Tracking
            q.contains("स्टेटस") || q.contains("track") || q.contains("ट्रेक") || q.contains("स्थिति") -> {
                """
                🔍 *आवेदन स्टेटस ट्रैक करना:*
                • आप ऐप के 'स्टेटस ट्रैक' टैब में जाकर अपना 10 अंकों का टोकन नंबर (जैसे RAJ-2026-XXXX) दर्ज करके तुरंत लाइव स्थिति देख सकते हैं।
                • सीधे जानकारी के लिए WhatsApp: 7878513433 पर भी टोकन भेज सकते हैं।
                """.trimIndent()
            }

            // App Download / Link / Install
            q.contains("डाउनलोड") || q.contains("download") || q.contains("install") || q.contains("एप") || q.contains("ऐप") || q.contains("लिंक") || q.contains("link") -> {
                """
                📱 *दीपक सेंटर्स ऐप डाउनलोड व इंस्टॉल करने की जानकारी:*
                • *डायरेक्ट लिंक:* https://ais-pre-uhphfe2akrwhutmf5c5vi3-636864101553.asia-southeast1.run.app
                
                • *फोन में कैसे चलाएं:*
                  1. ऊपर दिए लिंक को अपने फोन के Chrome ब्राउज़र में खोलें।
                  2. 3 डॉट्स मेनू पर टैप करके 'Install App' या 'Add to Home screen' चुनें।
                  3. ऐप की 'केंद्र विवरण' स्क्रीन में डाउनलोड कार्ड से लिंक कॉपी या WhatsApp पर सीधे भेज सकते हैं।
                • *हेल्पलाइन:* 7878513433 | deepakcenters@gmail.com
                """.trimIndent()
            }

            // General greeting or fallback
            else -> {
                """
                दीपक सेंटर्स पर ई-मित्र, जाति, मूल निवास, जन आधार ई-KYC, पेंशन सत्यापन, बिजली बिल, जमीन जमाबंदी, राशन कार्ड व सभी ऑनलाइन फॉर्म की सुविधा उपलब्ध है।
                
                • *केंद्र समय:* ${kiosk.timings}
                • *अवकाश:* ${kiosk.holidays}
                • *स्थान:* सिगडोला बड़ा, सीकर (332312)
                • *WhatsApp:* 7878513433
                """.trimIndent()
            }
        }

        return ChatMessage(
            text = replyText + PEACEFUL_THANK_YOU,
            isUser = false,
            suggestedActions = listOf(
                "जाति प्रमाण पत्र",
                "मूल निवास",
                "केंद्र समय",
                "WhatsApp संपर्क",
                "दस्तावेज़ चेकलिस्ट"
            )
        )
    }
}
