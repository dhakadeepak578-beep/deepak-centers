package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 3D Full-HD Card for downloading and sharing the Deepak Center Mobile App.
 * Allows users to copy the shareable link, send it via WhatsApp, open directly in phone browser,
 * or follow APK download & installation instructions.
 */
@Composable
fun DownloadAppCard(
    modifier: Modifier = Modifier,
    appUrl: String = "https://ais-pre-uhphfe2akrwhutmf5c5vi3-636864101553.asia-southeast1.run.app"
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var isInstructionsVisible by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    listOf(Color(0xFF00C853), Color(0xFF0D47A1), Color(0xFFFFB703))
                ),
                shape = RoundedCornerShape(18.dp)
            )
            .testTag("download_app_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: App icon + Title + Help icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF00C853), Color(0xFF0D47A1))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download App",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "मोबाइल में ऐप डाउनलोड करें",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Download Deepak Center Mobile App",
                            fontSize = 11.sp,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = { isInstructionsVisible = !isInstructionsVisible },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "Installation instructions",
                        tint = Color(0xFF0D47A1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "इस लिंक के माध्यम से आप 'दीपक सेंटर्स ई-मित्र' ऐप को सीधे अपने मोबाइल फोन में डाउनलोड व खोल सकते हैं।",
                fontSize = 12.5.sp,
                color = Color(0xFF334155),
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Direct Link Container with Copy Button
            Surface(
                color = Color(0xFFF1F5F9),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = appUrl,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(appUrl))
                            Toast.makeText(context, "ऐप डाउनलोड लिंक कॉपी हो गया!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_copy_download_link")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Link",
                            tint = Color(0xFF0D47A1),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row: WhatsApp Share + Open in Browser
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // WhatsApp Share to self/family
                Button(
                    onClick = {
                        val shareMessage = "📱 *दीपक सेंटर्स ई-मित्र ऐप डाउनलोड करें*\n\n" +
                                "सिगडोला बड़ा (सीकर) के ई-मित्र केंद्र की सभी सेवाएं (जाति, मूल निवास, जन आधार, पेंशन, जमाबंदी) व ताज़ा सरकारी योजनाओं के लिए ऐप अभी अपने मोबाइल में डाउनलोड या खोलें:\n\n" +
                                "🔗 डाउनलोड लिंक: $appUrl\n\n" +
                                "🏢 दीपक सेंटर्स, मुख्य बस स्टैंड, सिगडोला बड़ा\n" +
                                "📞 हेल्पलाइन: 7878513433\n" +
                                "✉️ ईमेल: deepakcenters@gmail.com"

                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareMessage)
                        }
                        try {
                            context.startActivity(Intent.createChooser(intent, "ऐप डाउनलोड लिंक भेजें"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Share not supported", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("btn_share_app_whatsapp"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Share on WhatsApp",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "व्हाट्सएप पर भेजें",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Open in Mobile Browser / Direct Download
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Browser: $appUrl", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("btn_open_app_browser"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = "Open in Browser",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ब्राउज़र में खोलें",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Expandable Step-by-Step Installation Instructions
            AnimatedVisibility(visible = isInstructionsVisible) {
                Surface(
                    color = Color(0xFFEFF6FF),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "📖 मोबाइल में ऐप डाउनलोड व इंस्टॉल करने के 2 आसान तरीके:",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "1️⃣ *बिना डाउनलोड किए 1-क्लिक होम स्क्रीन पर जोड़ें (Instant Web App):*\n" +
                                    "• दिए गए लिंक को अपने फोन के Google Chrome में खोलें।\n" +
                                    "• Chrome में ऊपर दाईं ओर 3-डॉट्स (Menu) पर क्लिक करें।\n" +
                                    "• 'Install App' या 'Add to Home screen' (होम स्क्रीन पर जोड़ें) चुनें। यह ऐप आपके फोन में असली Android ऐप की तरह आइकन बन जाएगा।",
                            fontSize = 11.5.sp,
                            color = Color(0xFF1E293B),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "2️⃣ *APK फाइल डाउनलोड (Android APK Export):*\n" +
                                    "• आप AI Studio के प्रोजेक्ट सेटिंग्स (Project Menu) से 'Export APK / AAB' पर क्लिक करके सीधे .apk फाइल डाउनलोड कर अपने फोन में इंस्टॉल कर सकते हैं।",
                            fontSize = 11.5.sp,
                            color = Color(0xFF1E293B),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Quick toggle text for instructions
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isInstructionsVisible = !isInstructionsVisible },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isInstructionsVisible) "निर्देश छुपाएं ▲" else "📱 फोन में इंस्टॉल करने की विधि देखें ▼",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D47A1)
                )
            }
        }
    }
}
