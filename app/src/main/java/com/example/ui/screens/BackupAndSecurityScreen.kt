package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.BackupRecord
import com.example.ui.theme.*
import com.example.ui.viewmodel.WebCraftViewModel

@Composable
fun BackupAndSecurityScreen(
    viewModel: WebCraftViewModel,
    modifier: Modifier = Modifier
) {
    val backups by viewModel.backupRecords.collectAsState()
    val telegramConfig by viewModel.telegramConfig.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: Automated Scheduled Backup System
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyanAccent.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CloudSync, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Sistem Backup Otomatis Terjadwal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Snapshot berkala setiap 24 jam • Retensi 30 hari", fontSize = 11.sp, color = TextSecondaryDark)
                            }
                        }

                        Button(
                            onClick = { viewModel.runScheduledBackup() },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("run_backup_now_button")
                        ) {
                            Icon(Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Backup Sekarang", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Backup Policy Info
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NavySurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Cron: 0 2 * * * (02:00 WIB Setiap Hari)", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = CyanAccent)
                                Text("Metode: PostgreSQL pg_dump & gzip kompresi terenkripsi", fontSize = 10.sp, color = TextSecondaryDark)
                            }
                            Surface(shape = RoundedCornerShape(4.dp), color = EmeraldSuccess.copy(alpha = 0.2f)) {
                                Text("SCHEDULED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Riwayat Snapshot Backup:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Spacer(modifier = Modifier.height(6.dp))

                    backups.forEach { bk ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NavyDeep,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(bk.fileName, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Text("${bk.timestamp} • ${bk.sizeMb} MB • Retensi: ${bk.retentionDays} hari", fontSize = 10.sp, color = TextSecondaryDark)
                                }
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Two-Factor Authentication (2FA) & End-to-End Encryption
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = NavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(VioletPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = VioletPurple, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text("Otentikasi Dua Faktor (2FA) & Keamanan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Enkripsi End-to-End (E2E) pada data sensitif", fontSize = 11.sp, color = TextSecondaryDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2FA Card
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NavySurfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Status 2FA TOTP: ", fontSize = 12.sp, color = Color.White)
                                    Text("AKTIF", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                                }
                                Text("Wajib untuk: Buka Vault Kredensial, Restart Pod, & Ekspor Finansial", fontSize = 10.sp, color = TextSecondaryDark)
                            }
                            Switch(
                                checked = currentUser.twoFactorEnabled,
                                onCheckedChange = {},
                                colors = SwitchDefaults.colors(checkedThumbColor = CyanAccent, checkedTrackColor = ElectricBlueDark)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Security Compliance Matrix
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            Text("Enkripsi Data At-Rest: AES-256-CBC dengan HMAC SHA-256", fontSize = 11.sp, color = TextPrimaryDark)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            Text("Enkripsi In-Transit: TLS 1.3 HTTPS & Strict Transport Security", fontSize = 11.sp, color = TextPrimaryDark)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            Text("Proteksi SQL Injection: Prepared Statements via Laravel Eloquent ORM", fontSize = 11.sp, color = TextPrimaryDark)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            Text("Sanitasi File Upload: MIME Verification (Menolak .exe, .php, .sh)", fontSize = 11.sp, color = TextPrimaryDark)
                        }
                    }
                }
            }
        }

        // Section 3: Telegram Error Alert Integration
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0088CC).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Color(0xFF0088CC), modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("Notifikasi Error via Telegram", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Bot Webhook peringatan insiden sistem instan", fontSize = 11.sp, color = TextSecondaryDark)
                            }
                        }

                        Button(
                            onClick = { viewModel.triggerTelegramTestAlert() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0088CC), contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("test_telegram_alert_button")
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Kirim Uji Coba", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = NavyDeep,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Bot Token: ${telegramConfig.botToken}", fontSize = 10.sp, color = TextSecondaryDark, fontFamily = FontFamily.Monospace)
                            Text("Target Chat: ${telegramConfig.chatId}", fontSize = 10.sp, color = CyanAccent, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Status Pengiriman Terakhir: ${telegramConfig.lastDispatchedAlert}", fontSize = 10.sp, color = EmeraldSuccess)
                        }
                    }
                }
            }
        }
    }
}
