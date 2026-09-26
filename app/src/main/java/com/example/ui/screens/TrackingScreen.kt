package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ApplicationEntity
import com.example.model.ApplicationStatus
import com.example.model.ApplicationTimelineStep
import com.example.model.KioskDetails
import com.example.ui.components.ApplicationStatusTrackingForm
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TrackingScreen(
    searchInput: String,
    onSearchInputChanged: (String) -> Unit,
    onSearchClicked: (String?) -> Unit,
    trackedApplication: ApplicationEntity?,
    allApplications: List<ApplicationEntity>,
    onSelectApplication: (ApplicationEntity) -> Unit,
    timelineSteps: List<ApplicationTimelineStep>,
    errorMessage: String?,
    kiosk: KioskDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            .run { PaddingValues(top = calculateTopPadding(), bottom = 90.dp) }
    ) {
        // Portal Header
        item {
            TrackingPortalBanner()
        }

        // Application Status Tracking Form UI Component
        item {
            ApplicationStatusTrackingForm(
                trackingId = searchInput,
                onTrackingIdChange = onSearchInputChanged,
                onSubmitQuery = { token ->
                    onSearchInputChanged(token)
                    onSearchClicked(token)
                },
                onSampleSelect = { token ->
                    onSearchInputChanged(token)
                    onSearchClicked(token)
                },
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        // Error message if not found
        if (errorMessage != null) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = errorMessage,
                            fontSize = 12.sp,
                            color = Color(0xFFC62828),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Active Tracked Result Card
        if (trackedApplication != null) {
            item {
                ActiveTrackingResultCard(
                    app = trackedApplication,
                    timelineSteps = timelineSteps,
                    kiosk = kiosk,
                    onCopyToken = { token ->
                        clipboardManager.setText(AnnotatedString(token))
                        Toast.makeText(context, "टोकन कॉपी किया गया: $token", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // Past Application History Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "सिस्टम में सुरक्षित आवेदन (${allApplications.size}):",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = "लोकल डेटाबेस सेव",
                    fontSize = 11.sp,
                    color = Color(0xFF16A34A),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        items(allApplications, key = { it.tokenNumber }) { itemApp ->
            SavedApplicationItem(
                app = itemApp,
                isSelected = trackedApplication?.tokenNumber == itemApp.tokenNumber,
                onClick = { onSelectApplication(itemApp) }
            )
        }
    }
}

@Composable
fun TrackingPortalBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF004D40),
                            Color(0xFF00796B),
                            Color(0xFF0284C7)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0x33FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.TrackChanges,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "दीपक सेंटर आवेदन ट्रैकिंग पोर्टल",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "अपने ई-मित्र आवेदन की रियल-टाइम स्थिति व डिजिटल हस्ताक्षर की जांच करें।",
                        fontSize = 11.sp,
                        color = Color(0xFFE0F2F1)
                    )
                }
            }
        }
    }
}

@Composable
fun ActiveTrackingResultCard(
    app: ApplicationEntity,
    timelineSteps: List<ApplicationTimelineStep>,
    kiosk: KioskDetails,
    onCopyToken: (String) -> Unit
) {
    val context = LocalContext.current
    val currentStatus = try {
        ApplicationStatus.valueOf(app.status)
    } catch (e: Exception) {
        ApplicationStatus.SUBMITTED
    }

    val (badgeBg, badgeText) = when (currentStatus) {
        ApplicationStatus.APPROVED, ApplicationStatus.READY_FOR_PRINT -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        ApplicationStatus.AT_TEHSILDAR_OFFICE -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        ApplicationStatus.SUBMITTED_TO_PORTAL -> Color(0xFFE3F2FD) to Color(0xFF0D47A1)
        else -> Color(0xFFF1F5F9) to Color(0xFF334155)
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("active_tracking_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Token and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("आवेदन टोकन संख्या:", fontSize = 11.sp, color = Color(0xFF64748B))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onCopyToken(app.tokenNumber) }
                    ) {
                        Text(
                            text = app.tokenNumber,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("(कॉपी)", fontSize = 11.sp, color = Color(0xFF0284C7))
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = currentStatus.label.split("(")[0].trim(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeText
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details Grid
            Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    DetailRow(label = "सेवा का नाम:", value = app.serviceTitle)
                    DetailRow(label = "आवेदक का नाम:", value = app.applicantName)
                    DetailRow(label = "मोबाइल नंबर:", value = app.mobileNumber.take(5) + "*****")
                    DetailRow(label = "पता / ग्राम:", value = app.village)
                    DetailRow(label = "अनुमानित पूर्णता:", value = app.estimatedDate)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stepper / Timeline Section
            Text(
                text = "प्रक्रिया चरण (Application Progress Timeline):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                timelineSteps.forEachIndexed { index, step ->
                    TimelineStepRow(
                        step = step,
                        isLast = index == timelineSteps.size - 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Remarks Box
            if (app.remarks.isNotBlank()) {
                Surface(
                    color = Color(0xFFFFFDE7),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "दीपक सेंटर / अधिकारी टिप्पणी:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFF57F17)
                        )
                        Text(
                            text = app.remarks,
                            fontSize = 12.sp,
                            color = Color(0xFF37474F),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val shareText = "दीपक सेंटर ई-मित्र रसीद विवरण:\n" +
                                "टोकन: ${app.tokenNumber}\n" +
                                "सेवा: ${app.serviceTitle}\n" +
                                "आवेदक: ${app.applicantName}\n" +
                                "स्थिति: ${currentStatus.label}\n" +
                                "स्थान: सिगडोला बड़ा, सीकर 332312"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "रसीद साझा करें"))
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("साझा करें", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        com.example.util.WhatsAppLauncher.launchWhatsApp(
                            context,
                            com.example.util.WhatsAppLauncher.getStatusTrackingMessage(
                                app.tokenNumber,
                                app.serviceTitle,
                                app.applicantName
                            )
                        )
                    },
                    modifier = Modifier.weight(1.3f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("WhatsApp पर पूछें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B))
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E293B),
            maxLines = 1
        )
    }
}

@Composable
fun TimelineStepRow(
    step: ApplicationTimelineStep,
    isLast: Boolean
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Icon and connecting vertical line
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            step.isCompleted -> Color(0xFF16A34A)
                            step.isCurrent -> Color(0xFF0D47A1)
                            else -> Color(0xFFCBD5E1)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (step.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                } else if (step.isCurrent) {
                    Icon(
                        imageVector = Icons.Default.HourglassTop,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(38.dp)
                        .background(
                            if (step.isCompleted) Color(0xFF16A34A) else Color(0xFFE2E8F0)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Step text
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (!isLast) 12.dp else 0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = step.title,
                    fontSize = 12.sp,
                    fontWeight = if (step.isCurrent || step.isCompleted) FontWeight.Bold else FontWeight.Medium,
                    color = if (step.isCurrent) Color(0xFF0D47A1) else Color(0xFF1E293B)
                )
                Text(
                    text = step.date,
                    fontSize = 10.sp,
                    color = Color(0xFF64748B)
                )
            }

            Text(
                text = step.description,
                fontSize = 11.sp,
                color = Color(0xFF475569),
                lineHeight = 15.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun SavedApplicationItem(
    app: ApplicationEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .testTag("saved_app_${app.tokenNumber}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.tokenNumber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
                Text(
                    text = "${app.applicantName} • ${app.serviceTitle}",
                    fontSize = 11.sp,
                    color = Color(0xFF334155),
                    maxLines = 1
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = app.status.replace("_", " "),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A)
                )
            }
        }
    }
}
