package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Dedicated 3D UI component to display an 'Application Status Tracking' form
 * where users can input their tracking ID/Token number or mobile number to query service status.
 */
@Composable
fun ApplicationStatusTrackingForm(
    trackingId: String,
    onTrackingIdChange: (String) -> Unit,
    onSubmitQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onSampleSelect: ((String) -> Unit)? = null
) {
    val clipboardManager = LocalClipboardManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isHelpVisible by remember { mutableStateOf(false) }

    val sampleTokens = listOf(
        "DC-2026-4819" to "जाति प्रमाण",
        "DC-2026-5921" to "पेंशन",
        "DC-2026-6104" to "जन आधार",
        "DC-2026-7230" to "छात्रवृत्ति"
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color(0xFF0D47A1), Color(0xFF1E88E5), Color(0xFFFFB703))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .testTag("application_status_tracking_form"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Icon + Title + Help icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrackChanges,
                            contentDescription = "Tracking Form",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "आवेदन स्थिति ट्रैकिंग",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Application Status Tracking Form",
                            fontSize = 10.5.sp,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                IconButton(
                    onClick = { isHelpVisible = !isHelpVisible },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Where to find Tracking ID",
                        tint = if (isHelpVisible) Color(0xFF0D47A1) else Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Expandable Help Box
            AnimatedVisibility(visible = isHelpVisible) {
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💡 ट्रैकिंग आईडी कहाँ मिलेगी?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "• ई-मित्र रसीद पर दर्ज टोकन नंबर (जैसे DC-2026-XXXX)\n• आवेदन करते समय आपके मोबाइल पर प्राप्त 10 अंकों का नंबर या पंजीकृत फोन नंबर दर्ज करें।",
                            fontSize = 11.sp,
                            color = Color(0xFF334155),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Input Field: Tracking ID
            OutlinedTextField(
                value = trackingId,
                onValueChange = onTrackingIdChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tracking_id_input"),
                placeholder = {
                    Text(
                        text = "टोकन नंबर (उदा. DC-2026-4819) या मोबाइल",
                        fontSize = 12.5.sp,
                        color = Color(0xFF94A3B8)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Tag,
                        contentDescription = "Token Tag",
                        tint = Color(0xFF0D47A1),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (trackingId.isNotEmpty()) {
                            IconButton(
                                onClick = { onTrackingIdChange("") },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Input",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Paste button
                        IconButton(
                            onClick = {
                                val clip = clipboardManager.getText()?.text
                                if (!clip.isNullOrBlank()) {
                                    onTrackingIdChange(clip.trim())
                                }
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = "Paste Token",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        if (trackingId.isNotBlank()) {
                            onSubmitQuery(trackingId.trim())
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0D47A1),
                    unfocusedBorderColor = Color(0xFFCBD5E1),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                )
            )

            // Optional Error message
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFDC2626),
                    fontSize = 11.5.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action: Query / Track Button
            Button(
                onClick = {
                    keyboardController?.hide()
                    if (trackingId.isNotBlank()) {
                        onSubmitQuery(trackingId.trim())
                    }
                },
                enabled = trackingId.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_submit_tracking_id"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0D47A1),
                    disabledContainerColor = Color(0xFFCBD5E1)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Status",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isLoading) "स्थिति जांची जा रही है..." else "स्थिति जांचें (Track Status)",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Quick Samples Row
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "त्वरित परीक्षण टोकन (टैप करें):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B)
                )

                Text(
                    text = "नमूना रसीद",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(sampleTokens) { (tok, label) ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (trackingId.equals(tok, ignoreCase = true)) Color(0xFFDBEAFE) else Color(0xFFF1F5F9),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(
                                if (trackingId.equals(tok, ignoreCase = true)) {
                                    listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                                } else {
                                    listOf(Color(0xFFE2E8F0), Color(0xFFCBD5E1))
                                }
                            )
                        ),
                        modifier = Modifier
                            .clickable {
                                onTrackingIdChange(tok)
                                onSampleSelect?.invoke(tok) ?: onSubmitQuery(tok)
                            }
                            .testTag("sample_token_$tok")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tok,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "• $label",
                                fontSize = 10.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }
    }
}
