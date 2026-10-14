package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AddOnItem
import com.example.model.PlatformAnalytics
import com.example.model.ServicePackage
import com.example.ui.theme.*
import com.example.ui.viewmodel.WebCraftViewModel

@Composable
fun KatalogPricingScreen(
    viewModel: WebCraftViewModel,
    modifier: Modifier = Modifier
) {
    val quotationState by viewModel.quotationState.collectAsState()
    val packages = viewModel.packages
    val addOns = viewModel.availableAddOns

    val totalCost = quotationState.calculateTotal(addOns)
    val totalDays = quotationState.calculateDays(addOns)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header
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
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CyanAccent.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Kalkulator Estimasi Biaya",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "Transparan & Instan",
                            fontSize = 11.sp,
                            color = TextSecondaryDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Katalog Paket Jasa Website & Docker AntiGravity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Pilih paket dasar sesuai kebutuhan bisnis Anda dan tambahkan fitur add-on untuk kalkulasi biaya dan durasi pengerjaan otomatis.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }
            }
        }

        // Section 1: Choose Base Package
        item {
            Text(
                text = "1. Pilih Paket Dasar Pembuatan Website",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(packages) { pkg ->
            val isSelected = quotationState.selectedPackage.id == pkg.id
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isSelected) ElectricBlueDark.copy(alpha = 0.25f) else NavySurface,
                border = androidx.compose.foundation.BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) CyanAccent else NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectPackage(pkg) }
                    .testTag("package_card_${pkg.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = pkg.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                if (pkg.recommended) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = AmberWarning
                                    ) {
                                        Text(
                                            text = "POPULER",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Estimasi: ${pkg.estimatedDays} hari kerja | Kuota: ${pkg.revisionQuota}x revisi",
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                        }

                        Text(
                            text = pkg.formattedPrice(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanAccent
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = pkg.description,
                        fontSize = 12.sp,
                        color = TextPrimaryDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        pkg.features.forEach { feat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(text = feat, fontSize = 11.sp, color = TextSecondaryDark)
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Choose Add-Ons
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "2. Pilih Fitur Tambahan (Add-On)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(addOns) { addon ->
            val isChecked = quotationState.selectedAddOnIds.contains(addon.id)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isChecked) NavySurfaceVariant else NavySurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isChecked) CyanAccent.copy(alpha = 0.7f) else NavyBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleAddOn(addon.id) }
                    .testTag("addon_item_${addon.id}")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleAddOn(addon.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = CyanAccent,
                                checkmarkColor = NavyDeep
                            )
                        )
                        Column {
                            Text(
                                text = addon.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = addon.description,
                                fontSize = 11.sp,
                                color = TextSecondaryDark
                            )
                            if (addon.extraDays > 0) {
                                Text(
                                    text = "+${addon.extraDays} hari pengerjaan",
                                    fontSize = 10.sp,
                                    color = AmberWarning
                                )
                            }
                        }
                    }

                    Text(
                        text = "+${addon.formattedPrice()}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanAccent
                    )
                }
            }
        }

        // Section 3: Summary & Order Placement
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF0F1E36),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Ringkasan Biaya & Pemesanan",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Paket Dipilih:", fontSize = 12.sp, color = TextSecondaryDark)
                        Text(quotationState.selectedPackage.name, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Add-on Aktif:", fontSize = 12.sp, color = TextSecondaryDark)
                        Text("${quotationState.selectedAddOnIds.size} Fitur Tambahan", fontSize = 12.sp, color = CyanAccent)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Estimasi Waktu:", fontSize = 12.sp, color = TextSecondaryDark)
                        Text("$totalDays Hari Kerja", fontSize = 12.sp, color = AmberWarning, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = NavyBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Nilai Kontrak:", fontSize = 11.sp, color = TextSecondaryDark)
                            Text("Split Billing: DP 50% & Termin 2 Pelunasan", fontSize = 9.sp, color = CyanAccent)
                        }
                        Text(
                            text = PlatformAnalytics.formatRupiah(totalCost),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = quotationState.clientNameInput,
                        onValueChange = { viewModel.updateQuotationInputs(it, quotationState.companyNameInput) },
                        label = { Text("Nama Penanggung Jawab Klien") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = quotationState.companyNameInput,
                        onValueChange = { viewModel.updateQuotationInputs(quotationState.clientNameInput, it) },
                        label = { Text("Nama Usaha / Perusahaan / Brand") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.checkoutQuotation() },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("checkout_order_button")
                    ) {
                        Icon(Icons.Default.ShoppingCartCheckout, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Konfirmasi Pesanan & Terbitkan DP Termin 1", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
