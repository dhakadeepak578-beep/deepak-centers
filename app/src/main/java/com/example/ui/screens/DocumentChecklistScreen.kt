package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmitraDataProvider
import com.example.model.EmitraService
import com.example.model.KioskDetails
import com.example.ui.components.DeepakCenterLogoBadge
import com.example.util.WhatsAppLauncher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentChecklistScreen(
    initialServiceId: String? = null,
    kiosk: KioskDetails = KioskDetails(),
    onApplyForService: (EmitraService) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allServices = EmitraDataProvider.services

    var selectedService by remember(initialServiceId) {
        mutableStateOf(
            allServices.find { it.id == initialServiceId } ?: allServices.first()
        )
    }

    // Checked documents map: (serviceId to Set of document indices/names)
    val checkedMap = remember { mutableStateMapOf<String, Boolean>() }

    var showServicePickerSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentDocs = selectedService.requiredDocuments
    val totalCount = currentDocs.size
    val checkedCount = currentDocs.count { doc ->
        checkedMap["${selectedService.id}_$doc"] == true
    }
    val progress = if (totalCount > 0) checkedCount.toFloat() / totalCount else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "progress")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Screen Header Card with Deepak Center Logo
        item {
            Surface(
                color = Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            DeepakCenterLogoBadge(
                                size = 52.dp,
                                elevation = 3.dp,
                                modifier = Modifier.testTag("checklist_logo_badge")
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "दस्तावेज़ चेकलिस्ट",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFE8F5E9))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "कियोस्क गाइड",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        )
                                    }
                                }

                                Text(
                                    text = "कियोस्क आने से पहले आवश्यक दस्तावेज़ तैयार करें",
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        // Share / Reset Action Buttons
                        Row {
                            IconButton(
                                onClick = {
                                    shareChecklist(context, selectedService, currentDocs, checkedMap)
                                },
                                modifier = Modifier.testTag("btn_share_checklist")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share Checklist",
                                    tint = Color(0xFF0D47A1),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            if (checkedCount > 0) {
                                IconButton(
                                    onClick = {
                                        currentDocs.forEach { doc ->
                                            checkedMap.remove("${selectedService.id}_$doc")
                                        }
                                    },
                                    modifier = Modifier.testTag("btn_reset_checklist")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Reset Checks",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Category/Service Quick Chips
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp)
            ) {
                Text(
                    text = "लोकप्रिय सेवाएं (त्वरित चयन):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val quickServices = listOf(
                        "caste_obc" to "जाति OBC",
                        "domicile_bonafide" to "मूल निवास",
                        "jan_aadhaar_kyc" to "जन आधार KYC",
                        "caste_ews" to "EWS प्रमाण",
                        "pension_social_security" to "पेंशन योजना",
                        "ration_card_service" to "राशन कार्ड",
                        "jamabandi_nakal" to "जमाबंदी नकल",
                        "police_character_cert" to "चरित्र प्रमाण"
                    )

                    items(quickServices) { (id, label) ->
                        val isSelected = selectedService.id == id
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val srv = allServices.find { it.id == id }
                                if (srv != null) selectedService = srv
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0D47A1),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Service Selection Card
        item {
            ElevatedCard(
                onClick = { showServicePickerSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("service_selector_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "चयनित सेवा (Service Selected):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "बदलें (Change)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )
                            Icon(
                                imageVector = Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = selectedService.titleHi,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Text(
                        text = selectedService.department,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = Color(0xFFFFFBEB),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "शुल्क: ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                                Text(
                                    text = selectedService.feeInfo,
                                    fontSize = 11.sp,
                                    color = Color(0xFF78350F),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Surface(
                            color = Color(0xFFF0FDF4),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "समय: ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF15803D)
                                )
                                Text(
                                    text = selectedService.processingDays,
                                    fontSize = 11.sp,
                                    color = Color(0xFF166534),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Preparation Progress Meter Card
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("preparation_meter_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = if (checkedCount == totalCount && totalCount > 0) Color(0xFFECFDF5) else Color.White
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (checkedCount == totalCount && totalCount > 0) Icons.Default.CheckCircle else Icons.Default.FactCheck,
                                contentDescription = null,
                                tint = if (checkedCount == totalCount && totalCount > 0) Color(0xFF10B981) else Color(0xFF0D47A1),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "आपकी तैयारी (Checklist Progress):",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        Text(
                            text = "$checkedCount / $totalCount तैयार (${(progress * 100).toInt()}%)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (checkedCount == totalCount && totalCount > 0) Color(0xFF059669) else Color(0xFF0D47A1)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (checkedCount == totalCount && totalCount > 0) Color(0xFF10B981) else Color(0xFF0D47A1),
                        trackColor = Color(0xFFE2E8F0),
                        strokeCap = StrokeCap.Round
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val statusMsg = when {
                        checkedCount == totalCount && totalCount > 0 -> "✅ आपके सभी आवश्यक दस्तावेज़ तैयार हैं! अब आप दीपक सेंटर (सिगडोला बड़ा) पधार सकते हैं।"
                        checkedCount > 0 -> "⚡ अच्छी तैयारी! शेष ${totalCount - checkedCount} दस्तावेज़ भी साथ ले लें ताकि कियोस्क पर समय न लगे।"
                        else -> "👉 नीचे दी गई सूची से जो दस्तावेज़ आपके पास तैयार हैं, उन पर टिक (✓) करें।"
                    }

                    Text(
                        text = statusMsg,
                        fontSize = 12.sp,
                        color = if (checkedCount == totalCount && totalCount > 0) Color(0xFF065F46) else Color(0xFF475569),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Section Title: Required Documents List
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "आवश्यक दस्तावेज़ों की सूची (${totalCount})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Text(
                    text = "टैप करके टिक करें",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Document Cards
        items(currentDocs) { doc ->
            val key = "${selectedService.id}_$doc"
            val isChecked = checkedMap[key] == true

            ChecklistDocumentItemCard(
                documentName = doc,
                isChecked = isChecked,
                serviceCategory = selectedService.category.labelHi,
                onToggleCheck = {
                    checkedMap[key] = !isChecked
                }
            )
        }

        // Helpful Kiosk Visit Tips Card
        item {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color(0xFFFFFBEB)),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFFFDE68A), Color(0xFFF59E0B))))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "दीपक सेंटर पधारने से पहले महत्वपूर्ण सलाह:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TipBulletPoint(text = "ओटीपी प्रमाणीकरण: आधार से लिंक मोबाइल नंबर अवश्य साथ लाएं।")
                    TipBulletPoint(text = "मूल प्रति (Originals): फोटोकॉपी के साथ मूल दस्तावेज स्कैनिंग हेतु लाएं।")
                    TipBulletPoint(text = "पासपोर्ट फोटो: आवेदन हेतु 6 माह से पुरानी फोटो न हो।")
                    TipBulletPoint(text = "समय: प्रातः 8:00 से रात्रि 8:30 बजे (सोम-शनि) | रविवार: 9:00 - 2:00")
                }
            }
        }

        // Bottom Action Buttons
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // WhatsApp Direct Inquiry Button
                Button(
                    onClick = {
                        val msg = "नमस्ते दीपक जी, मुझे '${selectedService.titleHi}' के आवश्यक दस्तावेजों के संबंध में पूछना है। मेरे पास $checkedCount/$totalCount दस्तावेज़ तैयार हैं। (सिगडोला बड़ा, सीकर 332312)"
                        WhatsAppLauncher.launchWhatsApp(context, msg)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_whatsapp_checklist"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "WhatsApp पर पूछें (7878513433)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Upload & Apply directly
                Button(
                    onClick = {
                        onApplyForService(selectedService)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_apply_from_checklist"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "दस्तावेज़ ऑनलाइन भेजें (Upload Now)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Modal BottomSheet for selecting another service
    if (showServicePickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showServicePickerSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "सेवा चुनें (Select Service):",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "जिस सेवा के दस्तावेज़ जांचने हैं, उस पर क्लिक करें",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(420.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allServices) { srv ->
                        val isCurrent = srv.id == selectedService.id
                        Surface(
                            onClick = {
                                selectedService = srv
                                showServicePickerSheet = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = if (isCurrent) CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF0D47A1), Color(0xFF1976D2)))) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = srv.titleHi,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) Color(0xFF0D47A1) else Color(0xFF0F172A)
                                    )
                                    Text(
                                        text = "${srv.requiredDocuments.size} आवश्यक दस्तावेज़ • ${srv.processingDays}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }

                                if (isCurrent) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF0D47A1),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ChecklistDocumentItemCard(
    documentName: String,
    isChecked: Boolean,
    serviceCategory: String,
    onToggleCheck: () -> Unit
) {
    val cardBg by animateColorAsState(
        targetValue = if (isChecked) Color(0xFFF0FDF4) else Color.White,
        label = "bg"
    )

    ElevatedCard(
        onClick = onToggleCheck,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("doc_item_${documentName.take(15)}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardBg),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = if (isChecked) 1.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onToggleCheck() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF16A34A),
                    uncheckedColor = Color(0xFF94A3B8)
                )
            )

            Spacer(modifier = Modifier.width(6.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = documentName,
                    fontSize = 13.5.sp,
                    fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Bold,
                    color = if (isChecked) Color(0xFF15803D) else Color(0xFF0F172A),
                    textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isChecked) Color(0xFFDCFCE7) else Color(0xFFF1F5F9))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isChecked) "✓ तैयार है" else "अनिवार्य प्रति",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) Color(0xFF166534) else Color(0xFF475569)
                        )
                    }

                    Text(
                        text = getDocTip(documentName),
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TipBulletPoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("• ", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309))
        Text(
            text = text,
            fontSize = 11.5.sp,
            color = Color(0xFF78350F),
            lineHeight = 16.sp
        )
    }
}

private fun getDocTip(docName: String): String {
    return when {
        docName.contains("आधार", ignoreCase = true) -> "ओटीपी / बायोमेट्रिक हेतु चालू फोन साथ रखें"
        docName.contains("फोटो", ignoreCase = true) -> "नवीनतम पासपोर्ट साइज रंगीन फोटो"
        docName.contains("आय", ignoreCase = true) -> "सक्षम अधिकारी द्वारा प्रमाणित 1 वर्षीय"
        docName.contains("1972", ignoreCase = true) -> "पुरानी रजिस्ट्री, पट्टा या राजस्व नकल"
        docName.contains("बिजली", ignoreCase = true) -> "नवीनतम 3 माह का बिल या रसीद"
        docName.contains("खाता", ignoreCase = true) || docName.contains("बैंक", ignoreCase = true) -> "स्पष्ट IFSC कोड व खाता संख्या वाली पासबुक"
        else -> "मूल प्रति व 1 साफ फोटोकॉपी साथ लाएं"
    }
}

private fun shareChecklist(
    context: Context,
    service: EmitraService,
    docs: List<String>,
    checkedMap: Map<String, Boolean>
) {
    val builder = StringBuilder()
    builder.append("📋 *दस्तावेज़ चेकलिस्ट - दीपक सेंटर (सिगडोला बड़ा, सीकर)*\n")
    builder.append("सेवा: *${service.titleHi}*\n")
    builder.append("सरकारी/कियोस्क शुल्क: ${service.feeInfo}\n")
    builder.append("कार्य दिवस: ${service.processingDays}\n\n")
    builder.append("*आवश्यक दस्तावेज़ सूची:*\n")

    docs.forEachIndexed { index, doc ->
        val isChecked = checkedMap["${service.id}_$doc"] == true
        val mark = if (isChecked) "✅ [तैयार]" else "⬜ [लाएं]"
        builder.append("${index + 1}. $mark $doc\n")
    }

    builder.append("\n📍 *दीपक सेंटर (ई-मित्र कियोस्क)*\n")
    builder.append("स्थान: सिगडोला बड़ा, जिला सीकर (राजस्थान) 332312\n")
    builder.append("हेल्पलाइन / WhatsApp: 7878513433\n")
    builder.append("समय: प्रातः 8:00 से रात्रि 8:30 बजे")

    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, builder.toString())
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "दस्तावेज़ चेकलिस्ट शेयर करें")
    try {
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "चेकलिस्ट शेयर नहीं हो सकी", Toast.LENGTH_SHORT).show()
    }
}
