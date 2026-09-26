package com.example.model

/**
 * Represents a live news update or government scheme announcement
 * automatically fetched or synced from official government websites.
 */
data class GovSchemeNewsItem(
    val id: String,
    val title: String,
    val summary: String,
    val sourceName: String,
    val sourceUrl: String,
    val publishDate: String,
    val category: GovNewsCategory,
    val isLiveFromWeb: Boolean = true,
    val relatedServiceId: String? = null,
    val isUrgent: Boolean = false
)

enum class GovNewsCategory(val labelHi: String, val badgeColorHex: Long) {
    ALL("सभी अपडेट", 0xFF0D47A1),
    EMITRA_CIRCULAR("ई-मित्र आदेश", 0xFF00897B),
    SCHEME_LAUNCH("नई योजना", 0xFF1E88E5),
    DEADLINE("अंतिम तिथि", 0xFFD32F2F),
    FARMER_REVENUE("किसान व राजस्व", 0xFF388E3C),
    SCHOLARSHIP_EDUCATION("शिक्षा व छात्रवृत्ति", 0xFF7B1FA2),
    HEALTH_PENSION("स्वास्थ्य व पेंशन", 0xFFE65100)
}
