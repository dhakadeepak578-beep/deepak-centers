package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppLauncher {
    const val CONTACT_NUMBER = "7878513433"
    const val PHONE_WITH_COUNTRY = "917878513433"

    fun launchWhatsApp(context: Context, inquiryMessage: String? = null) {
        val message = inquiryMessage ?: "नमस्ते दीपक जी, मुझे दीपक सेंटर (सिगडोला बड़ा, सीकर - 332312) की ई-मित्र सेवाओं के बारे में जानकारी चाहिए।"
        val encodedMsg = Uri.encode(message)

        // Try direct WhatsApp app intent
        val directIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("whatsapp://send?phone=$PHONE_WITH_COUNTRY&text=$encodedMsg")
            setPackage("com.whatsapp")
        }

        try {
            context.startActivity(directIntent)
        } catch (e: Exception) {
            // Fallback 1: Try WhatsApp Business
            val bizIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("whatsapp://send?phone=$PHONE_WITH_COUNTRY&text=$encodedMsg")
                setPackage("com.whatsapp.w4b")
            }
            try {
                context.startActivity(bizIntent)
            } catch (e2: Exception) {
                // Fallback 2: Universal api.whatsapp.com / wa.me link for browser or system resolver
                val universalIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=$PHONE_WITH_COUNTRY&text=$encodedMsg")
                )
                try {
                    context.startActivity(universalIntent)
                } catch (e3: Exception) {
                    Toast.makeText(
                        context,
                        "WhatsApp: +91 $CONTACT_NUMBER (कॉल करें)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun getServiceInquiryMessage(serviceTitle: String): String {
        return "नमस्ते दीपक जी, मुझे '$serviceTitle' के बारे में जानकारी व आवश्यक दस्तावेजों की सहायता चाहिए। (दीपक सेंटर, सिगडोला बड़ा, सीकर - 332312)"
    }

    fun getStatusTrackingMessage(token: String, serviceTitle: String, applicant: String): String {
        return "नमस्ते दीपक जी, मेरे आवेदन टोकन '$token' ($serviceTitle - $applicant) की वर्तमान स्थिति क्या है? कृपया बताएं। (सिगडोला बड़ा, सीकर 332312)"
    }
}
