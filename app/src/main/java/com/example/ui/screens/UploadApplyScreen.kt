package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmitraDataProvider
import com.example.model.KioskDetails
import com.example.viewmodel.UploadDocumentSlot

@Composable
fun UploadApplyScreen(
    formServiceId: String,
    onServiceChanged: (String) -> Unit,
    applicantName: String,
    onApplicantNameChanged: (String) -> Unit,
    mobile: String,
    onMobileChanged: (String) -> Unit,
    janAadhaar: String,
    onJanAadhaarChanged: (String) -> Unit,
    village: String,
    onVillageChanged: (String) -> Unit,
    documentSlots: List<UploadDocumentSlot>,
    onAttachDocument: (slotId: String, uri: Uri?, name: String, size: String) -> Unit,
    onRemoveDocument: (slotId: String) -> Unit,
    onSubmitForm: () -> Unit,
    isSubmitting: Boolean,
    latestSubmittedToken: String?,
    onDismissSuccessDialog: () -> Unit,
    onTrackToken: (String) -> Unit,
    validationError: String?,
    kiosk: KioskDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activePickingSlotId by remember { mutableStateOf<String?>(null) }

    // Modern Zero-Permission Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null && activePickingSlotId != null) {
            val slotId = activePickingSlotId!!
            val fileName = "doc_${slotId.replace("doc_", "")}_scan.jpg"
            onAttachDocument(slotId, uri, fileName, "1.4 MB")
        }
        activePickingSlotId = null
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            .run { PaddingValues(top = calculateTopPadding(), bottom = 90.dp) }
    ) {
        // Banner
        item {
            UploadSecurityBanner()
        }

        // Service Selection
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "1. सेवा का चयन करें (Select E-Mitra Service):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(EmitraDataProvider.services) { service ->
                            val isSelected = service.id == formServiceId
                            FilterChip(
                                selected = isSelected,
                                onClick = { onServiceChanged(service.id) },
                                label = {
                                    Text(
                                        text = service.titleHi.split("(")[0].trim(),
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0D47A1),
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("apply_select_${service.id}")
                            )
                        }
                    }
                }
            }
        }

        // Applicant Information Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "2. आवेदक की जानकारी (Applicant Information):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = applicantName,
                        onValueChange = onApplicantNameChanged,
                        label = { Text("आवेदक का पूरा नाम (Full Name) *", fontSize = 12.sp) },
                        placeholder = { Text("उदा. राजेश कुमार ढाका", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_applicant_name"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = mobile,
                        onValueChange = onMobileChanged,
                        label = { Text("मोबाइल नंबर (10 अंक) *", fontSize = 12.sp) },
                        placeholder = { Text("उदा. 9828012345", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_mobile_number"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = janAadhaar,
                        onValueChange = onJanAadhaarChanged,
                        label = { Text("जन आधार / आधार संख्या (वैकल्पिक)", fontSize = 12.sp) },
                        placeholder = { Text("उदा. 2148765432", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_janaadhaar"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = village,
                        onValueChange = onVillageChanged,
                        label = { Text("ग्राम / तहसील / पिनकोड", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_village"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Document Upload Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "3. सुरक्षित दस्तावेज़ अपलोड (Document Upload):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Text(
                            text = "SSL 256-Bit Encrypted",
                            fontSize = 10.sp,
                            color = Color(0xFF15803D),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "कैमरा या गैलरी से स्पष्ट फोटो अपलोड करें। यदि मूल कॉपी दीपक सेंटर पर लानी है तो सीधा सबमिट भी कर सकते हैं।",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        documentSlots.forEach { slot ->
                            DocumentSlotItem(
                                slot = slot,
                                onPickFromGallery = {
                                    activePickingSlotId = slot.id
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                onSampleAttach = {
                                    onAttachDocument(
                                        slot.id,
                                        Uri.parse("file://sample/${slot.id}.jpg"),
                                        "${slot.label.split("(")[0].trim()}_Scan.pdf",
                                        "1.2 MB"
                                    )
                                },
                                onRemove = { onRemoveDocument(slot.id) }
                            )
                        }
                    }
                }
            }
        }

        // Validation Error
        if (validationError != null) {
            item {
                Text(
                    text = "⚠ $validationError",
                    fontSize = 12.sp,
                    color = Color(0xFFDC2626),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        // Submit Button
        item {
            Button(
                onClick = onSubmitForm,
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_submit_application"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("आवेदन दर्ज हो रहा है...", fontSize = 14.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "सुरक्षित जमा करें (Generate Kiosk Token)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Success Confirmation Dialog
    if (latestSubmittedToken != null) {
        val selectedServiceTitle = EmitraDataProvider.services.find { it.id == formServiceId }?.titleHi ?: "ई-मित्र सेवा"

        AlertDialog(
            onDismissRequest = onDismissSuccessDialog,
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "आवेदन सफल!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "आपका आवेदन दीपक सेंटर, सिगडोला बड़ा पर सफलतापूर्वक दर्ज कर लिया गया है।",
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = Color(0xFFEFF6FF),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "आपका ट्रैकिंग टोकन नंबर:",
                                fontSize = 11.sp,
                                color = Color(0xFF1E40AF)
                            )
                            Text(
                                text = latestSubmittedToken,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0D47A1)
                            )
                            Text(
                                text = "स्थान: सिगडोला बड़ा (सीकर) 332312",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "इस टोकन से आप 'स्टेटस ट्रैक' पोर्टल पर किसी भी समय अपने आवेदन की रियल-टाइम स्थिति देख सकते हैं।",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 15.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val token = latestSubmittedToken
                        onDismissSuccessDialog()
                        onTrackToken(token)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("dialog_track_now_btn")
                ) {
                    Text("लाइव ट्रैक करें", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        val token = latestSubmittedToken
                        val msg = "नमस्ते दीपक जी, मैंने दीपक सेंटर ऐप से '$selectedServiceTitle' के लिए आवेदन किया है। टोकन: $token। कृपया रसीद चेक करें। (सिगडोला बड़ा 332312)"
                        com.example.util.WhatsAppLauncher.launchWhatsApp(context, msg)
                        onDismissSuccessDialog()
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("WhatsApp रसीद", color = Color(0xFF16A34A))
                }
            }
        )
    }
}

@Composable
fun UploadSecurityBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0D47A1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "सुरक्षित एवं गोपनीय दस्तावेज पोर्टल",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "आपके आधार व दस्तावेज केवल अधिकृत ई-मित्र आवेदन हेतु ही सुरक्षित उपयोग किए जाते हैं।",
                    fontSize = 11.sp,
                    color = Color(0xFF1E3A8A)
                )
            }
        }
    }
}

@Composable
fun DocumentSlotItem(
    slot: UploadDocumentSlot,
    onPickFromGallery: () -> Unit,
    onSampleAttach: () -> Unit,
    onRemove: () -> Unit
) {
    val isUploaded = slot.uri != null

    Surface(
        color = if (isUploaded) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isUploaded) Color(0xFF86EFAC) else Color(0xFFE2E8F0)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("doc_slot_${slot.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isUploaded) Icons.Default.CloudDone else Icons.Default.Description,
                        contentDescription = null,
                        tint = if (isUploaded) Color(0xFF16A34A) else Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = slot.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )
                        if (slot.isMandatory) {
                            Text(
                                text = "अनिवार्य दस्तावेज (*)",
                                fontSize = 10.sp,
                                color = Color(0xFFDC2626)
                            )
                        }
                    }
                }

                if (isUploaded) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            if (isUploaded) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "✓ संलग्न: ${slot.fileName ?: "दस्तावेज_स्कैन.jpg"}",
                        fontSize = 11.sp,
                        color = Color(0xFF16A34A),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = slot.fileSize ?: "1.4 MB",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onPickFromGallery,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("pick_${slot.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = null,
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("फोटो चुनें", fontSize = 11.sp, color = Color(0xFF0D47A1))
                    }

                    OutlinedButton(
                        onClick = onSampleAttach,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("quick_${slot.id}")
                    ) {
                        Text("क्विक अटैच", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }
            }
        }
    }
}
