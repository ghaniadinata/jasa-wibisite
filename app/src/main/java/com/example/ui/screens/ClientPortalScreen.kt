package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.model.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.WebCraftViewModel

@Composable
fun ClientPortalScreen(
    viewModel: WebCraftViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.orders.collectAsState()
    val selectedOrderId by viewModel.selectedOrderId.collectAsState()
    val activeOrder = orders.find { it.id == selectedOrderId } ?: orders.firstOrNull()

    val milestones by viewModel.milestones.collectAsState()
    val revisionTickets by viewModel.revisionTickets.collectAsState()
    val invoices by viewModel.invoices.collectAsState()
    val credentials by viewModel.credentials.collectAsState()
    val revealedPasswords by viewModel.revealedPasswordIds.collectAsState()

    val isVaultUnlocked = viewModel.isVaultUnlocked()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (activeOrder == null) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NavySurface,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Belum ada pesanan aktif. Silakan pilih paket di menu Katalog.", color = TextSecondaryDark, modifier = Modifier.padding(16.dp))
                }
            }
            return@LazyColumn
        }

        // Active Order Overview Card
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = activeOrder.orderNumber,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CyanAccent,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Text(
                                text = "${activeOrder.companyName} • ${activeOrder.servicePackage.name}",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when (activeOrder.status) {
                                OrderStatus.COMPLETED -> EmeraldSuccess.copy(alpha = 0.2f)
                                OrderStatus.WAITING_DP, OrderStatus.WAITING_SETTLEMENT -> AmberWarning.copy(alpha = 0.2f)
                                else -> ElectricBlueDark.copy(alpha = 0.4f)
                            }
                        ) {
                            Text(
                                text = activeOrder.status.label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = when (activeOrder.status) {
                                    OrderStatus.COMPLETED -> EmeraldSuccess
                                    OrderStatus.WAITING_DP, OrderStatus.WAITING_SETTLEMENT -> AmberWarning
                                    else -> CyanAccent
                                },
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Nilai Kontrak", fontSize = 10.sp, color = TextSecondaryDark)
                            Text(activeOrder.formattedTotalAmount(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column {
                            Text("Target Selesai", fontSize = 10.sp, color = TextSecondaryDark)
                            Text(activeOrder.targetCompletionDate, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                        Column {
                            Text("Sisa Kuota Revisi", fontSize = 10.sp, color = TextSecondaryDark)
                            Text("${activeOrder.remainingRevisionQuota} / ${activeOrder.revisionQuotaTotal}x", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                        }
                    }
                }
            }
        }

        // Section: 5-Stage Project Milestone Timeline
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tahapan Proyek (5-Stage Milestone)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Sign-Off Digital",
                    fontSize = 11.sp,
                    color = CyanAccent
                )
            }
        }

        items(milestones) { milestone ->
            val isApproved = milestone.status == MilestoneStatus.APPROVED
            val isInReview = milestone.status == MilestoneStatus.IN_REVIEW
            val isActive = milestone.status == MilestoneStatus.ACTIVE

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when {
                    isApproved -> NavySurfaceVariant.copy(alpha = 0.6f)
                    isInReview -> ElectricBlueDark.copy(alpha = 0.25f)
                    else -> NavySurface
                },
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when {
                        isApproved -> EmeraldSuccess.copy(alpha = 0.5f)
                        isInReview -> CyanAccent
                        isActive -> ElectricBlue
                        else -> NavyBorder
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
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
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isApproved -> EmeraldSuccess
                                            isInReview -> CyanAccent
                                            isActive -> ElectricBlue
                                            else -> NavyBorder
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isApproved) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(16.dp))
                                } else {
                                    Text(
                                        text = "${milestone.stageNumber}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NavyDeep
                                    )
                                }
                            }

                            Text(
                                text = milestone.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                isApproved -> EmeraldSuccess.copy(alpha = 0.2f)
                                isInReview -> AmberWarning.copy(alpha = 0.2f)
                                isActive -> ElectricBlue.copy(alpha = 0.2f)
                                else -> NavyBorder.copy(alpha = 0.4f)
                            }
                        ) {
                            Text(
                                text = when (milestone.status) {
                                    MilestoneStatus.APPROVED -> "APPROVED"
                                    MilestoneStatus.IN_REVIEW -> "SIAP REVIEW"
                                    MilestoneStatus.ACTIVE -> "DIKERJAKAN"
                                    MilestoneStatus.LOCKED -> "TERKUNCI"
                                },
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isApproved -> EmeraldSuccess
                                    isInReview -> AmberWarning
                                    isActive -> CyanAccent
                                    else -> TextSecondaryDark
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = milestone.description,
                        fontSize = 11.sp,
                        color = TextSecondaryDark
                    )

                    if (isApproved && milestone.approvedAt != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "✓ Disetujui secara digital pada ${milestone.approvedAt} (IP: ${milestone.approvedByIp})",
                            fontSize = 10.sp,
                            color = EmeraldSuccess,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Actions if in review (such as Stage 4 Staging Demo)
                    if (isInReview || (milestone.stageNumber == 4 && activeOrder.status == OrderStatus.STAGING_REVIEW)) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { viewModel.openStagingViewer() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = CyanAccent),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("open_staging_preview")
                            ) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Buka Demo Staging", fontSize = 11.sp)
                            }

                            Button(
                                onClick = { viewModel.approveMilestone(milestone.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f).testTag("approve_milestone_${milestone.id}")
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Setujui Fase Ini", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Section: Structured Brief
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = NavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Formulir Brief Proyek Terstruktur",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(onClick = { viewModel.openBriefModal() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Brief", tint = CyanAccent)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Nama Merek: ${activeOrder.brief.brandName}", fontSize = 12.sp, color = TextPrimaryDark)
                    Text("Slogan/Deskripsi: ${activeOrder.brief.taglineDescription}", fontSize = 11.sp, color = TextSecondaryDark)
                    Text("Palet Warna: ${activeOrder.brief.colorPalette}", fontSize = 11.sp, color = CyanAccent)
                    Text("Berkas Logo: ${activeOrder.brief.logoFileName}", fontSize = 11.sp, color = TextSecondaryDark)
                    Text("Status Brief: ${activeOrder.brief.status} (${activeOrder.brief.submittedAt})", fontSize = 10.sp, color = EmeraldSuccess)
                }
            }
        }

        // Section: Invoicing & Split Billing
        item {
            Text(
                text = "Manajemen Tagihan & Invoicing Termin",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(invoices) { inv ->
            val isPaid = inv.status == InvoiceStatus.PAID
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isPaid) NavySurfaceVariant.copy(alpha = 0.5f) else Color(0xFF1E293B),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isPaid) EmeraldSuccess.copy(alpha = 0.5f) else AmberWarning
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = inv.invoiceNumber, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = FontFamily.Monospace)
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (isPaid) EmeraldSuccess.copy(alpha = 0.2f) else AmberWarning.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (isPaid) "LUNAS" else "BELUM DIBAYAR",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPaid) EmeraldSuccess else AmberWarning,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(text = inv.termType.label, fontSize = 11.sp, color = TextSecondaryDark)
                        Text(text = inv.formattedAmount(), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = if (isPaid) EmeraldSuccess else CyanAccent)
                        if (isPaid && inv.paymentChannel != null) {
                            Text("Dibayar via ${inv.paymentChannel} (${inv.paidAt})", fontSize = 10.sp, color = TextSecondaryDark)
                        }
                    }

                    if (!isPaid) {
                        Button(
                            onClick = { viewModel.openPaymentModal(inv) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("pay_invoice_${inv.id}")
                        ) {
                            Text("Bayar (VA/QRIS)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Revision Ticketing
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = NavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Manajemen Tiket Revisi Berkuota", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Kuota tersisa: ${activeOrder.remainingRevisionQuota}x siklus", fontSize = 11.sp, color = AmberWarning)
                        }
                        Button(
                            onClick = { viewModel.openRevisionModal() },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("open_revision_button")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tiket Revisi", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (revisionTickets.isEmpty()) {
                        Text("Belum ada tiket revisi aktif.", fontSize = 11.sp, color = TextSecondaryDark)
                    } else {
                        revisionTickets.forEach { tkt ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = NavySurfaceVariant,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("#${tkt.id}: ${tkt.title}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                        Text(tkt.description, fontSize = 10.sp, color = TextSecondaryDark, maxLines = 1)
                                        Text(tkt.screenshotNote, fontSize = 9.sp, color = CyanAccent)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (tkt.status == "RESOLVED") EmeraldSuccess.copy(alpha = 0.2f) else AmberWarning.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = tkt.status,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (tkt.status == "RESOLVED") EmeraldSuccess else AmberWarning,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Encrypted Credential Vault & Handover (Gated by Term 2 Paid status!)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (isVaultUnlocked) NavySurface else Color(0xFF1E152A),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isVaultUnlocked) EmeraldSuccess.copy(alpha = 0.6f) else RoseError.copy(alpha = 0.5f)
                ),
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
                            Icon(
                                imageVector = if (isVaultUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isVaultUnlocked) EmeraldSuccess else RoseError
                            )
                            Text(
                                text = "Secure Credential Vault (AES-256)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        if (isVaultUnlocked) {
                            Button(
                                onClick = { viewModel.openBastModal() },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("open_bast_button")
                            ) {
                                Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("BAST Resmi", fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isVaultUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = RoseError.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.SecurityUpdateWarning, contentDescription = null, tint = RoseError, modifier = Modifier.size(24.dp))
                                Text(
                                    text = "Akses Kredensial Terkunci: Sesuai klausul kontrak, akun hosting, domain, dan admin web dienkripsi simetris AES-256 dan hanya dibuka setelah Termin 2 (Pelunasan) LUNAS.",
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Kredensial website telah dibuka karena Pelunasan Termin 2 LUNAS. Klik ikon mata untuk mendekripsi dengan verifikasi 2FA.",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        credentials.forEach { cred ->
                            val isPasswordRevealed = revealedPasswords.contains(cred.id)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = NavySurfaceVariant,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = cred.serviceLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                        IconButton(
                                            onClick = { viewModel.togglePasswordVisibility(cred.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isPasswordRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = "Toggle Password",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Text("URL: ${cred.loginUrl}", fontSize = 11.sp, color = TextSecondaryDark, fontFamily = FontFamily.Monospace)
                                    Text("Username: ${cred.username}", fontSize = 11.sp, color = TextPrimaryDark)
                                    Text(
                                        text = if (isPasswordRevealed) "Password: ${cred.plaintextPassword}" else "Password (AES-256): ${cred.encryptedPasswordCipher.take(20)}...",
                                        fontSize = 11.sp,
                                        color = if (isPasswordRevealed) EmeraldSuccess else TextSecondaryDark,
                                        fontWeight = if (isPasswordRevealed) FontWeight.Bold else FontWeight.Normal,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
