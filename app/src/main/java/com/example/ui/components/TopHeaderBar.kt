package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.KioskDetails
import com.example.viewmodel.AppTab

@Composable
fun TopHeaderBar(
    kiosk: KioskDetails,
    onTabSelect: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF002F6C),
                            Color(0xFF0D47A1)
                        )
                    )
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Official Deepak Centers Circular Emblem Logo
                        DeepakCenterLogoBadge(
                            size = 46.dp,
                            elevation = 3.dp,
                            modifier = Modifier
                                .testTag("header_deepak_center_logo")
                                .clickable { onTabSelect(AppTab.CENTER_INFO) }
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = kiosk.name,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Kiosk",
                                    tint = Color(0xFFFFB703),
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                            Text(
                                text = "ई-मित्र • ${kiosk.village}, ${kiosk.district.split(" ")[0]}",
                                fontSize = 12.sp,
                                color = Color(0xFFB0BEC5),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Direct Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // WhatsApp Direct Inquiry
                        FilledTonalIconButton(
                            onClick = {
                                com.example.util.WhatsAppLauncher.launchWhatsApp(context)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("top_whatsapp_button"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = Color(0xFF25D366),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "WhatsApp Deepak Center",
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Call Operator
                        FilledTonalIconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:7878513433")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Call: 7878513433", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("top_call_button"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = Color(0x33FFFFFF),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Deepak Center",
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        // Download / Share App Action
                        FilledTonalIconButton(
                            onClick = {
                                val appUrl = "https://ais-pre-uhphfe2akrwhutmf5c5vi3-636864101553.asia-southeast1.run.app"
                                val shareText = "📱 *दीपक सेंटर्स ई-मित्र ऐप डाउनलोड करें*\n\nसिगडोला बड़ा (सीकर) की सभी ई-मित्र सेवाएं व सरकारी योजनाओं के लिए ऐप अभी अपने फोन में खोलें या डाउनलोड करें:\n🔗 $appUrl\n\nदीपक सेंटर्स, सिगडोला बड़ा 📞 7878513433 ✉️ deepakcenters@gmail.com"
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                try {
                                    context.startActivity(Intent.createChooser(intent, "ऐप डाउनलोड लिंक शेयर करें"))
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Link: $appUrl", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("top_download_button"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = Color(0xFF00C853),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download Deepak Centers App",
                                modifier = Modifier.size(19.dp)
                            )
                        }

                        // Info / Center Details
                        FilledTonalIconButton(
                            onClick = { onTabSelect(AppTab.CENTER_INFO) },
                            modifier = Modifier
                                .size(40.dp)
                                .testTag("top_info_button"),
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = Color(0xFFFFB703),
                                contentColor = Color(0xFF002F6C)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Kiosk Details",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Sub-strip with Address & Contact Number
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x22FFFFFF))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFCC80),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "सिगडोला बड़ा (सीकर)",
                            fontSize = 11.sp,
                            color = Color(0xFFECEFF1),
                            maxLines = 1
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:7878513433"))
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Call: 7878513433", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Helpline",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
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
