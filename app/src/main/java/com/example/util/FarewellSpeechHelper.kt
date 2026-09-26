package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object FarewellSpeechHelper {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    fun init(context: Context) {
        if (tts != null) return
        try {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val hindiLocale = Locale("hi", "IN")
                    val result = tts?.setLanguage(hindiLocale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        // Fallback to default if Hindi not directly installed
                        tts?.language = Locale.getDefault()
                    }
                    tts?.setPitch(1.0f)
                    tts?.setSpeechRate(0.88f) // Peaceful, gentle pace
                    isInitialized = true
                }
            }
        } catch (e: Exception) {
            Log.e("FarewellTTS", "TTS init error: ${e.message}")
        }
    }

    fun speakFarewell(
        context: Context,
        message: String = "दीपक सेंटर्स सिगडोला बड़ा में पधारने के लिए आपका सहृदय धन्यवाद। आपका दिन शुभ एवं मंगलमय हो, पुनः अवश्य पधारें! नमस्कार।"
    ) {
        try {
            if (tts == null) {
                init(context)
            }
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "farewell_utterance")
        } catch (e: Exception) {
            Log.e("FarewellTTS", "TTS speak error: ${e.message}")
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isInitialized = false
        } catch (e: Exception) {
            Log.e("FarewellTTS", "TTS shutdown error: ${e.message}")
        }
    }
}
