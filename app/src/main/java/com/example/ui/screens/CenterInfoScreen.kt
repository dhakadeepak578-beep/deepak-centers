package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VerifiedUser
import com.example.ui.components.AiAutoUpdateCard
import com.example.ui.components.DownloadAppCard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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

@Composable
fun CenterInfoScreen(
    kiosk: KioskDetails,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            .run { PaddingValues(top = calculateTopPadding(), bottom = 90.dp) }
    ) {
        // Hero Center Profile
        item {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    // Header Accent Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF002F6C),
                                        Color(0xFF0D47A1),
                                        Color(0xFF1565C0)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            com.example.ui.components.DeepakCenterLogoBadge(
                                size = 64.dp,
                                elevation = 4.dp,
                                modifier = Modifier.testTag("center_info_deepak_logo")
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = kiosk.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "ई-मित्र एवं डिजिटल सेवा केंद्र",
                                    fontSize = 12.sp,
                                    color = Color(0xFFFFD54F),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "कियोस्क कोड: ${kiosk.kioskCode}",
                                    fontSize = 11.sp,
                                    color = Color(0xFFE2E8F0)
                                )
                            }
                        }
                    }

                    // Details Section
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(
                            icon = Icons.Default.LocationOn,
                            label = "पता:",
                            value = kiosk.fullAddress
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        InfoRow(
                            icon = Icons.Default.Person,
                            label = "संचालक:",
                            value = "${kiosk.operatorName} (दीपक ढाका)"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        InfoRow(
                            icon = Icons.Default.Call,
                            label = "संपर्क नंबर / मोबाइल:",
                            value = "7878513433 (+91 78785 13433)"
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        InfoRow(
                            icon = Icons.Default.AccessTime,
                            label = "समय:",
                            value = kiosk.timings
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        InfoRow(
                            icon = Icons.Default.EventBusy,
                            label = "अवकाश (Holiday):",
                            value = kiosk.holidays
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "ईमेल:",
                            value = kiosk.email
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Contact Action Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${kiosk.contactPhone.replace(" ", "")}"))
                                    try {
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Phone: ${kiosk.contactPhone}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("center_call_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("कॉल करें", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    com.example.util.WhatsAppLauncher.launchWhatsApp(
                                        context,
                                        "नमस्ते दीपक जी, मैं सिगडोला बड़ा केंद्र के ई-मित्र सेवा के संबंध में पूछताछ करना चाहता हूँ।"
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("center_whatsapp_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:${kiosk.email}")
                                        putExtra(Intent.EXTRA_SUBJECT, "दीपक सेंटर्स ई-मित्र सेवा पूछताछ")
                                    }
                                    try {
                                        context.startActivity(emailIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Email: ${kiosk.email}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("center_email_btn"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ईमेल", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Available In-Kiosk Facilities
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
                    Text(
                        text = "केंद्र पर उपलब्ध अतिरिक्त सुविधाएं (Kiosk Facilities):",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val facilities = listOf(
                        "रंगीन व ब्लैक/व्हाइट फोटोकॉपी व प्रिंटिंग (Color Xerox)",
                        "दस्तावेज लेमिनेशन एवं पासपोर्ट साइज तत्काल फोटो",
                        "उच्च गुणवत्ता स्कैनर (High-Speed Document Scanner)",
                        "AEPS आधार माइक्रो एटीएम - किसी भी बैंक से नकद निकासी",
                        "फास्टैग रिचार्ज, मोबाइल व डीटीएच रिचार्ज",
                        "कॉलेज व भर्ती परीक्षाओं के ऑनलाइन फॉर्म फीडिंग"
                    )

                    facilities.forEach { facility ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = facility,
                                fontSize = 12.sp,
                                color = Color(0xFF334155)
                            )
                        }
                    }
                }
            }
        }

        // Mobile App Download Card
        item {
            DownloadAppCard(modifier = Modifier.padding(bottom = 12.dp))
        }

        // Citizen Charter / Transparent Rate List
        item {
            AiAutoUpdateCard(modifier = Modifier.padding(bottom = 12.dp))
        }

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
                            text = "नागरिक अधिकार पत्र (पारदर्शी दर सूची):",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "कोई अतिरिक्त शुल्क नहीं",
                            fontSize = 10.sp,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val rates = listOf(
                        Triple("जाति / मूल निवास प्रमाण पत्र", "₹50 (Govt)", "₹50 (Kiosk)"),
                        Triple("जन आधार ई-केवाईसी", "निःशुल्क (₹0)", "₹30 (Kiosk)"),
                        Triple("पेंशन वार्षिक भौतिक सत्यापन", "निःशुल्क (₹0)", "₹30 (Kiosk)"),
                        Triple("जमाबंदी नकल प्रिंट", "₹20 (Govt)", "₹20 (Print)"),
                        Triple("बिजली / पानी बिल भुगतान", "निःशुल्क (₹0)", "निःशुल्क (₹0)"),
                        Triple("पैन कार्ड नया / संशोधन", "₹107 (Govt)", "₹50 (Kiosk)")
                    )

                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("सेवा", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                            Text("सरकारी शुल्क + कियोस्क सेवा", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
                        }
                    }

                    rates.forEach { (srv, govt, kioskFee) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = srv, fontSize = 12.sp, color = Color(0xFF1E293B), modifier = Modifier.weight(1f))
                            Text(text = "$govt + $kioskFee", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0D47A1))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF0D47A1),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            Text(text = value, fontSize = 12.sp, color = Color(0xFF1E293B), fontWeight = FontWeight.Medium)
        }
    }
}
