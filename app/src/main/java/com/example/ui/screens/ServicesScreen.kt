package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmitraDataProvider
import com.example.model.EmitraService
import com.example.model.ServiceCategory

@Composable
fun ServicesScreen(
    selectedCategory: ServiceCategory,
    onCategorySelected: (ServiceCategory) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onServiceSelected: (EmitraService) -> Unit,
    onApplyService: (EmitraService) -> Unit,
    onViewAllUpdates: () -> Unit = {},
    onApplyForServiceId: (String) -> Unit = {},
    onOpenChecklist: () -> Unit = {},
    onOpenChecklistForService: (EmitraService) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val allServices = EmitraDataProvider.services

    val filteredServices = allServices.filter { service ->
        val matchesCategory = selectedCategory == ServiceCategory.ALL || service.category == selectedCategory
        val matchesSearch = searchQuery.isBlank() ||
                service.titleHi.contains(searchQuery, ignoreCase = true) ||
                service.titleEn.contains(searchQuery, ignoreCase = true) ||
                service.department.contains(searchQuery, ignoreCase = true) ||
                service.requiredDocuments.any { it.contains(searchQuery, ignoreCase = true) }
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Highlight Banner
        item {
            HeroKioskCard(onOpenChecklist = onOpenChecklist)
        }

        // Dedicated Recent Scheme Updates Carousel
        if (searchQuery.isBlank()) {
            item {
                com.example.ui.components.RecentSchemeUpdatesCarousel(
                    updates = EmitraDataProvider.updates,
                    onUpdateClick = { update ->
                        val serviceId = update.relatedServiceId ?: "caste_obc"
                        val matchingService = allServices.find { it.id == serviceId }
                        if (matchingService != null) {
                            onServiceSelected(matchingService)
                        } else {
                            onViewAllUpdates()
                        }
                    },
                    onApplyForServiceId = { serviceId ->
                        val matchingService = allServices.find { it.id == serviceId }
                            ?: allServices.first()
                        onApplyService(matchingService)
                    },
                    onViewAllUpdates = onViewAllUpdates
                )
            }
        }

        // Search Input
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text(
                            "खोजें: जाति, जन आधार, राशन, पेंशन, नकल...",
                            fontSize = 13.sp,
                            color = Color(0xFF94A3B8)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF0D47A1)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF64748B)
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0D47A1),
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("service_search_input")
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ServiceCategory.values()) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelected(category) },
                        label = {
                            Text(
                                text = category.labelHi,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0D47A1),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF334155)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.testTag("category_${category.name.lowercase()}")
                    )
                }
            }
        }

        // Services Count and Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedCategory.labelHi} (${filteredServices.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = "सीकर ई-मित्र पोर्टल",
                    fontSize = 11.sp,
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Service List Items
        if (filteredServices.isEmpty()) {
            item {
                EmptyServiceState(query = searchQuery)
            }
        } else {
            items(filteredServices, key = { it.id }) { service ->
                ServiceItemCard(
                    service = service,
                    onViewDetails = { onServiceSelected(service) },
                    onApply = { onApplyService(service) },
                    onOpenChecklist = { onOpenChecklistForService(service) }
                )
            }
        }
    }
}

@Composable
fun HeroKioskCard(onOpenChecklist: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0D47A1),
                            Color(0xFF1E3A8A),
                            Color(0xFF0284C7)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFFFB703))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "अधिकृत केंद्र",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF002F6C)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "पिन: 332312",
                                fontSize = 11.sp,
                                color = Color(0xFFE2E8F0),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "दीपक सेंटर - सिगडोला बड़ा (सीकर)",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "समस्त राजस्थान ई-मित्र, जन आधार, पेंशन व राजस्व सेवाएं एक ही छत के नीचे उपलब्ध।",
                            fontSize = 12.sp,
                            color = Color(0xFFE0E7FF),
                            lineHeight = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Official Deepak Centers Seal
                    com.example.ui.components.DeepakCenterLogoBadge(
                        size = 58.dp,
                        elevation = 4.dp,
                        modifier = Modifier.testTag("hero_deepak_logo")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    HighlightFeatureBadge(title = "तुरंत रसीद", subtitle = "सरकारी टोकन")
                    HighlightFeatureBadge(title = "दस्तावेज़ अपलोड", subtitle = "मोबाइल से भेजें")
                    HighlightFeatureBadge(title = "लाइव ट्रैकिंग", subtitle = "स्टेटस जांचें")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Timing & Holiday Status Bar
                Surface(
                    color = Color(0x33000000),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏰ 9:00 AM - 5:00 PM (सोम-शुक्र)",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD54F)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• शनि-रवि अवकाश",
                            fontSize = 11.sp,
                            color = Color(0xFFFFA726)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Document Checklist Quick Banner
                Surface(
                    color = Color(0x33FFFFFF),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenChecklist() }
                        .testTag("banner_document_checklist")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFB703)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FactCheck,
                                    contentDescription = null,
                                    tint = Color(0xFF002F6C),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "कियोस्क आने से पहले दस्तावेज़ चेकलिस्ट देखें",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "जांचें →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD54F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val context = LocalContext.current
                Surface(
                    color = Color(0x33000000),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            com.example.util.WhatsAppLauncher.launchWhatsApp(context)
                        }
                        .testTag("banner_whatsapp_inquiry")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF25D366)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "किसी भी सेवा हेतु WhatsApp पूछताछ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = "7878513433",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD54F)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HighlightFeatureBadge(title: String, subtitle: String) {
    Surface(
        color = Color(0x22FFFFFF),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD54F)
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun ServiceItemCard(
    service: EmitraService,
    onViewDetails: () -> Unit,
    onApply: () -> Unit,
    onOpenChecklist: () -> Unit = {}
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("service_card_${service.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Title & Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.titleHi,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = service.titleEn,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                }

                if (service.isPopular) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFF3E0))
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "★ लोकप्रिय",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Department
            Text(
                text = service.department,
                fontSize = 11.sp,
                color = Color(0xFF0284C7),
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badges row: Fee, Time, Docs Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Fee Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalAtm,
                        contentDescription = null,
                        tint = Color(0xFF15803D),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = service.feeInfo.split("+")[0].trim(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B)
                    )
                }

                // Processing Time Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF0369A1),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = service.processingDays,
                        fontSize = 11.sp,
                        color = Color(0xFF1E293B)
                    )
                }

                // Required Docs / Checklist Chip
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFEFF6FF))
                        .clickable { onOpenChecklist() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FactCheck,
                        contentDescription = "Checklist",
                        tint = Color(0xFF1D4ED8),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${service.requiredDocuments.size} दस्तावेज़ (चेक करें)",
                        fontSize = 11.sp,
                        color = Color(0xFF1D4ED8),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("btn_details_${service.id}"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = "दस्तावेज़ सूची",
                        fontSize = 12.sp,
                        color = Color(0xFF0D47A1)
                    )
                }

                // Direct WhatsApp for this service
                val itemContext = LocalContext.current
                IconButton(
                    onClick = {
                        com.example.util.WhatsAppLauncher.launchWhatsApp(
                            itemContext,
                            com.example.util.WhatsAppLauncher.getServiceInquiryMessage(service.titleHi)
                        )
                    },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE8F5E9))
                        .testTag("whatsapp_service_${service.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "WhatsApp Inquiry",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(17.dp)
                    )
                }

                Button(
                    onClick = onApply,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(38.dp)
                        .testTag("btn_apply_${service.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "दस्तावेज़ भेजें",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyServiceState(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(54.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "कोई सेवा नहीं मिली",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF334155)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "'$query' से संबंधित कोई सेवा नहीं मिली। कृपया अन्य शब्द खोजें या दीपक सेंटर पर संपर्क करें।",
            fontSize = 13.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
