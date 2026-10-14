package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Invoice
import com.example.model.Order
import com.example.model.TermType
import com.example.ui.theme.*

@Composable
fun PaymentModal(
    invoice: Invoice,
    onConfirmPayment: (invoiceId: String, channel: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedChannel by remember { mutableStateOf("QRIS Dinamis (BCA/Mandiri)") }

    val channels = listOf(
        "QRIS Dinamis (BCA/Mandiri)",
        "Virtual Account BCA (880192348912)",
        "Virtual Account Mandiri (893400192834)",
        "Virtual Account BNI (98801239824)"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NavySurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pembayaran Invoice",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Invoice Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NavySurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = invoice.invoiceNumber, fontSize = 12.sp, color = CyanAccent, fontWeight = FontWeight.SemiBold)
                            Text(text = invoice.termType.label, fontSize = 11.sp, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = invoice.formattedAmount(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (invoice.termType == TermType.TERM_1_DP) "Pelunasan DP 50% untuk memulai pengerjaan brief & development" else "Pelunasan 50% akhir untuk membuka kunci Credential Vault & BAST",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Pilih Kanal Pembayaran:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                channels.forEach { channel ->
                    val isSelected = selectedChannel == channel
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) ElectricBlueDark.copy(alpha = 0.4f) else NavySurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) CyanAccent else NavyBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedChannel = channel }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedChannel = channel },
                                colors = RadioButtonDefaults.colors(selectedColor = CyanAccent)
                            )
                            Text(text = channel, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Simulated QR / Webhook info
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E3A8A).copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Idempotent Webhook Callback: Sistem otomatis memverifikasi HMAC SHA-512 dan memperbarui status pesanan tanpa duplikasi.",
                            fontSize = 10.sp,
                            color = CyanAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onConfirmPayment(invoice.id, selectedChannel) },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_payment_button")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simulasi Pelunasan Sukses (Webhook)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BriefIntakeModal(
    order: Order,
    onSubmit: (brand: String, tagline: String, colors: String, audience: String, comp: String, logo: String, drive: String) -> Unit,
    onDismiss: () -> Unit
) {
    var brandName by remember { mutableStateOf(order.brief.brandName.ifBlank { order.companyName }) }
    var tagline by remember { mutableStateOf(order.brief.taglineDescription) }
    var colors by remember { mutableStateOf(order.brief.colorPalette) }
    var audience by remember { mutableStateOf(order.brief.targetAudience) }
    var competitors by remember { mutableStateOf(order.brief.competitorUrls) }
    var logoFile by remember { mutableStateOf(order.brief.logoFileName) }
    var driveUrl by remember { mutableStateOf(order.brief.supportingAssetsUrl) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NavySurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Formulir Brief Proyek",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Order: ${order.orderNumber}",
                            fontSize = 11.sp,
                            color = CyanAccent
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = brandName,
                    onValueChange = { brandName = it },
                    label = { Text("Nama Merek / Perusahaan (brand_name)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = tagline,
                    onValueChange = { tagline = it },
                    label = { Text("Slogan & Deskripsi Bisnis (tagline_description)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = colors,
                    onValueChange = { colors = it },
                    label = { Text("Preferensi Skema Warna (color_preferences)") },
                    placeholder = { Text("Misal: Navy Blue & Electric Cyan") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = audience,
                    onValueChange = { audience = it },
                    label = { Text("Target Audiens (target_audience)") },
                    placeholder = { Text("Misal: B2B Korporasi & UMKM") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = competitors,
                    onValueChange = { competitors = it },
                    label = { Text("Referensi Website Disukai (competitor_urls)") },
                    placeholder = { Text("https://contoh1.com, https://contoh2.com") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = logoFile,
                    onValueChange = { logoFile = it },
                    label = { Text("Nama Berkas Logo Vektor (SVG/PNG < 25MB)") },
                    trailingIcon = {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = CyanAccent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = driveUrl,
                    onValueChange = { driveUrl = it },
                    label = { Text("Tautan Folder Google Drive Aset Tambahan") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onSubmit(brandName, tagline, colors, audience, competitors, logoFile, driveUrl)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_brief_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Kirimkan Brief ke Tim PM & Dev", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StagingViewerModal(
    stagingUrl: String,
    onApproveStaging: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NavyDeep,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Browser bar simulation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavySurfaceVariant)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RoseError))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(AmberWarning))
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(EmeraldSuccess))
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NavyDeep,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(12.dp))
                            Text(
                                text = stagingUrl,
                                fontSize = 11.sp,
                                color = TextPrimaryDark,
                                maxLines = 1,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondaryDark, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Staging Simulated Website Content
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0F172A),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Web Header Mock
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "PT Nusa Kreatif Mandiri", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Home", fontSize = 11.sp, color = CyanAccent)
                                Text("Produk", fontSize = 11.sp, color = Color.LightGray)
                                Text("Kontak", fontSize = 11.sp, color = Color.LightGray)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Hero Section Mock
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF1E293B),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Surface(shape = RoundedCornerShape(4.dp), color = CyanAccent.copy(alpha = 0.2f)) {
                                    Text("Dockerized Laravel 11 Engine", fontSize = 10.sp, color = CyanAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Solusi Fabrikasi Presisi & Inovasi Manufaktur", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Mitra strategis perakitan industri berstandar mutu internasional ISO 9001.", fontSize = 12.sp, color = Color(0xFF94A3B8))
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {},
                                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Konsultasikan Proyek", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Features row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E293B),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Icon(Icons.Default.Speed, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                    Text("Kinerja Tinggi", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Text("TTFB 345ms", fontSize = 10.sp, color = Color.Gray)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E293B),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(16.dp))
                                    Text("SSL & HTTPS", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                    Text("TLS 1.3 Active", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sign-off Actions
                Button(
                    onClick = onApproveStaging,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("approve_staging_button")
                ) {
                    Icon(Icons.Default.Verified, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Setujui Staging & UAT (Tanda Tangan Digital)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RevisionModal(
    remainingQuota: Int,
    onSubmit: (title: String, desc: String, screenshot: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var screenshot by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NavySurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pengajuan Tiket Revisi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quota Warning or Info
                if (remainingQuota > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ElectricBlueDark.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = CyanAccent)
                            Text(
                                text = "Sisa kuota revisi paket: $remainingQuota kali lagi.",
                                fontSize = 12.sp,
                                color = CyanAccent,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RoseError.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoseError),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = RoseError)
                            Text(
                                text = "Kuota revisi paket Anda telah habis (0). Pengajuan ini akan menerbitkan Add-on Revisi sebesar Rp 200.000,- / siklus.",
                                fontSize = 11.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul Permintaan Revisi") },
                    placeholder = { Text("Misal: Ubah nomor WA footer & warna tombol") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detail Perubahan (Maksimal 5 poin)") },
                    minLines = 3,
                    placeholder = { Text("1. Bagian kontak mohon ganti alamat...\n2. Logo klien partner tolong ditambah...") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = screenshot,
                    onValueChange = { screenshot = it },
                    label = { Text("Catatan / Nama Screenshot Pendukung") },
                    placeholder = { Text("screenshot-footer.png atau link drive") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank() && description.isNotBlank()) {
                            onSubmit(title, description, screenshot)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                    shape = RoundedCornerShape(10.dp),
                    enabled = title.isNotBlank() && description.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_revision_button")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (remainingQuota > 0) "Kirimkan Tiket Revisi" else "Terbitkan Add-on Revisi (Rp 200rb)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TwoFactorAuthModal(
    onVerify: (String) -> Boolean,
    onCancel: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NavySurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CyanAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(32.dp))
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Otentikasi Dua Faktor (2FA)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Tindakan ini memerlukan verifikasi kode keamanan TOTP demi melindungi data sensitif Anda.",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = TextSecondaryDark,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NavySurfaceVariant,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Kode Demo: 123456",
                        fontSize = 11.sp,
                        color = CyanAccent,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        if (it.length <= 6) {
                            code = it
                            isError = false
                            if (it.length == 6) {
                                val success = onVerify(it)
                                if (!success) isError = true
                            }
                        }
                    },
                    placeholder = { Text("123456", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    isError = isError,
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 24.sp,
                        letterSpacing = 6.sp,
                        color = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("two_factor_code_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanAccent,
                        unfocusedBorderColor = NavyBorder,
                        errorBorderColor = RoseError
                    )
                )

                if (isError) {
                    Text("Kode tidak valid. Coba 123456", fontSize = 11.sp, color = RoseError, modifier = Modifier.padding(top = 4.dp))
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondaryDark)
                    ) {
                        Text("Batal")
                    }

                    Button(
                        onClick = {
                            val success = onVerify(code)
                            if (!success) isError = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("verify_2fa_button")
                    ) {
                        Text("Verifikasi", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BastModal(
    order: Order,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = NavyDeep,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Berita Acara Serah Terima (BAST)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Certificate Container
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NavySurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("DOKUMEN RESMI SERAH TERIMA WEBSITE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                        Text("Nomor: BAST/WEBCRAFT/${order.orderNumber}", fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = TextSecondaryDark)

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = NavyBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Pihak Pertama (Penyedia Jasa): WebCraft Agency Studio", fontSize = 11.sp, color = Color.White)
                            Text("Pihak Kedua (Klien): ${order.clientName} (${order.companyName})", fontSize = 11.sp, color = Color.White)
                            Text("Paket Proyek: ${order.servicePackage.name}", fontSize = 11.sp, color = Color.White)
                            Text("Total Nilai Kontrak: ${order.formattedTotalAmount()} (LUNAS)", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                            Text("Masa Garansi Bug-Fix & Maintenance: 60 Hari Kalender", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // QR Verification Stamp
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.QrCode2, contentDescription = "QR BAST", tint = Color.Black, modifier = Modifier.size(70.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Verifikasi Digital SHA-256 Valid", fontSize = 10.sp, color = TextSecondaryDark)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Unduh Berkas BAST Resmi (PDF)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
