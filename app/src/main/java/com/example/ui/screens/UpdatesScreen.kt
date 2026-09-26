package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmitraDataProvider
import com.example.model.GovSchemeNewsItem
import com.example.model.KioskDetails
import com.example.model.RecentUpdate
import com.example.model.UpdateBadge
import com.example.ui.components.GovSchemeNewsFeedComponent

@Composable
fun UpdatesScreen(
    kiosk: KioskDetails,
    onApplyForServiceId: (String) -> Unit,
    govNewsItems: List<GovSchemeNewsItem> = emptyList(),
    isRefreshingGovNews: Boolean = false,
    lastSyncTimeGovNews: String = "",
    onRefreshGovNews: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedSection by remember { mutableStateOf(0) } // 0 = Live Web Feed, 1 = Kiosk Circulars
    val localKioskUpdates = EmitraDataProvider.updates
    var selectedBadgeFilter by remember { mutableStateOf<UpdateBadge?>(null) }

    val filteredKioskUpdates = localKioskUpdates.filter { update ->
        selectedBadgeFilter == null || update.badge == selectedBadgeFilter
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            .run { PaddingValues(top = calculateTopPadding(), bottom = 95.dp) }
    ) {
        // Top Portal Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFF9800)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "ई-मित्र ताज़ा सूचनाएं व सरकारी योजनाएं",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = "सरकारी पोर्टल्स से स्वतः प्राप्त ताज़ा समाचार, आदेश व अंतिम तिथियां",
                            fontSize = 11.5.sp,
                            color = Color(0xFF5D4037)
                        )
                    }
                }
            }
        }

        // Sub-tabs: Live Website Feed vs Kiosk Circulars
        item {
            TabRow(
                selectedTabIndex = selectedSection,
                containerColor = Color.White,
                contentColor = Color(0xFF0D47A1),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedSection]),
                        color = Color(0xFF0D47A1),
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .padding(bottom = 12.dp)
            ) {
                Tab(
                    selected = selectedSection == 0,
                    onClick = { selectedSection = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "लाइव वेब फ़ीड (${govNewsItems.size})",
                                fontWeight = if (selectedSection == 0) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.5.sp
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_live_gov_feed")
                )

                Tab(
                    selected = selectedSection == 1,
                    onClick = { selectedSection = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "कियोस्क सूचनाएं",
                                fontWeight = if (selectedSection == 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.5.sp
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_kiosk_notices")
                )
            }
        }

        // Content
        if (selectedSection == 0) {
            // Live Government Scheme & News Feed Component (Auto-fetched from other websites)
            item {
                GovSchemeNewsFeedComponent(
                    feedItems = govNewsItems,
                    isRefreshing = isRefreshingGovNews,
                    lastSyncTime = lastSyncTimeGovNews,
                    onRefresh = onRefreshGovNews,
                    onApplyForService = onApplyForServiceId
                )
            }
        } else {
            // Local Kiosk Circulars Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedBadgeFilter == null,
                        onClick = { selectedBadgeFilter = null },
                        label = { Text("सभी (${localKioskUpdates.size})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0D47A1),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_kiosk_all")
                    )

                    FilterChip(
                        selected = selectedBadgeFilter == UpdateBadge.URGENT,
                        onClick = {
                            selectedBadgeFilter = if (selectedBadgeFilter == UpdateBadge.URGENT) null else UpdateBadge.URGENT
                        },
                        label = { Text("अंतिम तिथि", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFD32F2F),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_kiosk_urgent")
                    )

                    FilterChip(
                        selected = selectedBadgeFilter == UpdateBadge.NEW,
                        onClick = {
                            selectedBadgeFilter = if (selectedBadgeFilter == UpdateBadge.NEW) null else UpdateBadge.NEW
                        },
                        label = { Text("नई योजनाएं", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1976D2),
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_kiosk_new")
                    )
                }
            }

            // Kiosk Circular Cards
            items(filteredKioskUpdates, key = { it.id }) { update ->
                UpdateCardItem(
                    update = update,
                    kiosk = kiosk,
                    onApply = {
                        val serviceId = update.relatedServiceId ?: "caste_obc"
                        onApplyForServiceId(serviceId)
                    }
                )
            }
        }
    }
}

@Composable
fun UpdateCardItem(
    update: RecentUpdate,
    kiosk: KioskDetails,
    onApply: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .testTag("update_card_${update.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Badge & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(update.badge.colorHex))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = update.badge.text,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = update.date,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = update.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Summary
            Text(
                text = update.summary,
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )

            // Department tag
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "🏛️ विभाग: ${update.department}",
                    fontSize = 11.sp,
                    color = Color(0xFF475569),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            // Expandable details
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "📋 पूर्ण विवरण:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = update.fullDetails,
                                fontSize = 11.5.sp,
                                color = Color(0xFF334155),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Expand toggle
                Text(
                    text = if (isExpanded) "कम विवरण देखें" else "विस्तार से जानें",
                    fontSize = 12.sp,
                    color = Color(0xFF0D47A1),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { isExpanded = !isExpanded }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // WhatsApp share button
                    OutlinedButton(
                        onClick = {
                            val shareText = "📢 *दीपक सेंटर्स ई-मित्र सूचना*\n\n*${update.title}*\n${update.summary}\n\nविभाग: ${update.department}\nस्थान: दीपक सेंटर्स, सिगडोला बड़ा (सीकर)\nसंपर्क: 7878513433"
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            try {
                                context.startActivity(Intent.createChooser(intent, "सूचना शेयर करें"))
                            } catch (e: Exception) {
                                Toast.makeText(context, "Share not supported", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("btn_share_${update.id}"),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Share",
                            modifier = Modifier.size(14.dp),
                            tint = Color(0xFF25D366)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("शेयर", fontSize = 11.sp, color = Color(0xFF25D366))
                    }

                    // Apply / Upload action
                    Button(
                        onClick = onApply,
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("btn_apply_update_${update.id}"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.UploadFile,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "आवेदन करें",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
