package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlatformAnalytics
import com.example.ui.theme.*
import com.example.ui.viewmodel.WebCraftViewModel

@Composable
fun AnalyticsFinanceScreen(
    viewModel: WebCraftViewModel,
    modifier: Modifier = Modifier
) {
    val analytics by viewModel.analytics.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Real-Time Activity Header
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = NavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSuccess)
                                )
                                Text("Aktivitas Pengguna Real-Time", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Text("Pemantauan traffic peramban & latensi respons TTFB", fontSize = 11.sp, color = TextSecondaryDark)
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = CyanAccent.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "LIVE TELEMETRY",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4 Stat Cards
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavySurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Pengunjung Aktif", fontSize = 10.sp, color = TextSecondaryDark)
                                Text("${analytics.activeVisitors}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                                Text("Online sekarang", fontSize = 9.sp, color = TextSecondaryDark)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavySurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Rata-rata TTFB", fontSize = 10.sp, color = TextSecondaryDark)
                                Text("${analytics.avgTtfbMs} ms", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                Text("Target: < 600 ms", fontSize = 9.sp, color = TextSecondaryDark)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavySurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Total Requests", fontSize = 10.sp, color = TextSecondaryDark)
                                Text("${analytics.todayRequests}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Hari ini", fontSize = 9.sp, color = TextSecondaryDark)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavySurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Error Rate (5xx)", fontSize = 10.sp, color = TextSecondaryDark)
                                Text("${analytics.errorRatePercent}%", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                                Text("Uptime: ${analytics.uptimePercent}%", fontSize = 9.sp, color = TextSecondaryDark)
                            }
                        }
                    }
                }
            }
        }

        // Financial & Project Profitability Accounting Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0D1D30),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Laporan Profitabilitas Finansial Agency", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Formula: Laba Kotor = Total Pembayaran - COGS Modal", fontSize = 11.sp, color = TextSecondaryDark)
                        }

                        IconButton(
                            onClick = {
                                viewModel.requestTwoFactorAuth {
                                    // Export simulated
                                }
                            },
                            modifier = Modifier.testTag("export_finance_button")
                        ) {
                            Icon(Icons.Default.Download, contentDescription = "Ekspor Laporan", tint = CyanAccent)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NavySurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Pembayaran Masuk (Gross):", fontSize = 12.sp, color = TextSecondaryDark)
                                Text(analytics.formattedGross(), fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Beban Modal Langsung (COGS):", fontSize = 12.sp, color = TextSecondaryDark)
                                Text("- ${analytics.formattedCogs()}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = RoseError)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = NavyBorder)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Laba Kotor Proyek (Net Profit):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                                    Text("Profit Margin: 80.6%", fontSize = 10.sp, color = CyanAccent)
                                }
                                Text(
                                    text = analytics.formattedNetProfit(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Rincian Beban Modal Vendor (COGS Terinci):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextSecondaryDark)
                    Spacer(modifier = Modifier.height(6.dp))

                    val cogsList = listOf(
                        Pair("Registrasi Domain (.com, .id via Registrar API)", "Rp 2.150.000"),
                        Pair("Sewa Server Cloud NVMe & AntiGravity Docker Nodes", "Rp 4.500.000"),
                        Pair("Lisensi Plugin, Template, & SSL Terkelola", "Rp 1.750.000"),
                        Pair("Fee Integrasi Webhook Payment Gateway (MDR)", "Rp 1.000.000")
                    )

                    cogsList.forEach { (item, cost) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "• $item", fontSize = 10.sp, color = TextPrimaryDark, modifier = Modifier.weight(1f))
                            Text(text = cost, fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
