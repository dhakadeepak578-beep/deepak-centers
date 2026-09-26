package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GovNewsCategory
import com.example.model.GovSchemeNewsItem

/**
 * Feed component to display latest government scheme updates and news
 * automatically fetched and synced from official government websites.
 */
@Composable
fun GovSchemeNewsFeedComponent(
    feedItems: List<GovSchemeNewsItem>,
    isRefreshing: Boolean,
    lastSyncTime: String,
    onRefresh: () -> Unit,
    onApplyForService: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(GovNewsCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems = feedItems.filter { item ->
        val matchesCategory = selectedCategory == GovNewsCategory.ALL || item.category == selectedCategory
        val matchesSearch = if (searchQuery.isBlank()) true else {
            item.title.contains(searchQuery, ignoreCase = true) ||
                    item.summary.contains(searchQuery, ignoreCase = true) ||
                    item.sourceName.contains(searchQuery, ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("gov_scheme_news_feed_component")
    ) {
        // Top 3D Header Card with Live Status & Refresh
        LiveFeedHeaderCard(
            isRefreshing = isRefreshing,
            lastSyncTime = lastSyncTime,
            itemCount = filteredItems.size,
            onRefresh = onRefresh
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search in Feed
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("योजना या समाचार खोजें (उदा. आयुष्मान, छात्रवृत्ति)", fontSize = 12.sp) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF0D47A1),
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("feed_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0D47A1),
                unfocusedBorderColor = Color(0xFFCBD5E1),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(GovNewsCategory.values()) { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat.labelHi, fontSize = 11.5.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(cat.badgeColorHex),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White,
                        labelColor = Color(0xFF334155)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("chip_category_${cat.name}")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // List of Feed Items
        if (filteredItems.isEmpty()) {
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "कोई समाचार या योजना अपडेट नहीं मिला",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "कृपया अन्य फ़िल्टर चुनें या रिफ्रेश करें।",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredItems.forEach { item ->
                    GovNewsFeedCard(
                        item = item,
                        onApply = { serviceId ->
                            onApplyForService(serviceId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LiveFeedHeaderCard(
    isRefreshing: Boolean,
    lastSyncTime: String,
    itemCount: Int,
    onRefresh: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .border(
                1.dp,
                Brush.horizontalGradient(listOf(Color(0xFF0D47A1), Color(0xFF00897B), Color(0xFFFFB703))),
                RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pulsing Live Indicator
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF10B981).copy(alpha = pulseAlpha))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ऑटो-सिंक लाइव फ़ीड",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "वेबसाइट्स से लाइव",
                            fontSize = 9.5.sp,
                            color = Color(0xFF1D4ED8),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Refresh Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF1F5F9))
                        .clickable(enabled = !isRefreshing) { onRefresh() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("feed_refresh_btn")
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF0D47A1)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("सिंक हो रहा...", fontSize = 11.sp, color = Color(0xFF0D47A1))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ताज़ा करें", fontSize = 11.sp, color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "सरकारी योजनाएं व ताज़ा ई-मित्र समाचार ($itemCount अपडेट्स)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌐 स्रोत: rajasthan.gov.in • emitra.rajasthan.gov.in • dipr",
                    fontSize = 10.5.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "सिंक: $lastSyncTime",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

@Composable
fun GovNewsFeedCard(
    item: GovSchemeNewsItem,
    onApply: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (item.isUrgent) Color(0xFFFFCDD2) else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("gov_feed_card_${item.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (item.isUrgent) Color(0xFFFFFBFB) else Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Source & Category Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Category Badge
                    Surface(
                        color = Color(item.category.badgeColorHex),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.category.labelHi,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                        )
                    }

                    if (item.isUrgent) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFD32F2F),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "जरूरी",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Date
                Text(
                    text = item.publishDate,
                    fontSize = 10.5.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Headline
            Text(
                text = item.title,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Summary (with expand/collapse)
            Text(
                text = item.summary,
                fontSize = 12.5.sp,
                color = Color(0xFF334155),
                lineHeight = 17.5.sp,
                maxLines = if (isExpanded) 12 else 3,
                overflow = TextOverflow.Ellipsis
            )

            // Source Website Tag
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 8.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Website",
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.sourceName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0369A1),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = if (isExpanded) "कम दिखाएं ▲" else "विस्तार से पढ़ें ▼",
                    fontSize = 11.sp,
                    color = Color(0xFF0D47A1),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions: Open Official Website + WhatsApp Share + Apply via E-Mitra
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Open Official Website
                OutlinedButton(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.sourceUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "वेबसाइट: ${item.sourceUrl}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1.1f)
                        .height(38.dp)
                        .testTag("btn_view_website_${item.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0284C7))
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("वेबसाइट लिंक", fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }

                // WhatsApp Share
                OutlinedButton(
                    onClick = {
                        val shareText = "📢 *${item.title}*\n\n${item.summary}\n\n🌐 स्रोत: ${item.sourceName}\nअधिक जानकारी हेतु संपर्क: दीपक सेंटर्स, सिगडोला बड़ा (सीकर) 📱 7878513433"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        try {
                            context.startActivity(Intent.createChooser(intent, "शेयर करें"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "शेयर नहीं हो सका", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(0.9f)
                        .height(38.dp)
                        .testTag("btn_share_${item.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF16A34A))
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("शेयर", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Apply on E-Mitra
                Button(
                    onClick = {
                        val srvId = item.relatedServiceId ?: "caste_obc"
                        onApply(srvId)
                    },
                    modifier = Modifier
                        .weight(1.1f)
                        .height(38.dp)
                        .testTag("btn_feed_apply_${item.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("आवेदन करें", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1)
                }
            }
        }
    }
}
