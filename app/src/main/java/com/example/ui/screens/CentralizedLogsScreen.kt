package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LogEntry
import com.example.model.LogLevel
import com.example.ui.theme.*
import com.example.ui.viewmodel.WebCraftViewModel

@Composable
fun CentralizedLogsScreen(
    viewModel: WebCraftViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.systemLogs.collectAsState()
    val selectedFilter by viewModel.selectedLogLevelFilter.collectAsState()
    val searchQuery by viewModel.logSearchQuery.collectAsState()

    val filteredLogs = logs.filter { log ->
        val matchesLevel = selectedFilter == null || log.level == selectedFilter
        val matchesQuery = searchQuery.isBlank() ||
                log.message.contains(searchQuery, ignoreCase = true) ||
                log.service.contains(searchQuery, ignoreCase = true)
        matchesLevel && matchesQuery
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
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
                            Text("Manajemen Log Terpusat", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Pusat debugging laravel.log, nginx, & postgres real-time", fontSize = 11.sp, color = TextSecondaryDark)
                        }

                        Button(
                            onClick = {
                                viewModel.requestTwoFactorAuth {
                                    viewModel.triggerTelegramTestAlert()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RoseError, contentColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("simulate_error_button")
                        ) {
                            Icon(Icons.Default.BugReport, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Simulasi Error", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setLogSearchQuery(it) },
                        placeholder = { Text("Cari keyword error, HTTP code, service...") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondaryDark)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.setLogSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondaryDark)
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("log_search_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filter Chips Row
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedFilter == null,
                                onClick = { viewModel.setLogLevelFilter(null) },
                                label = { Text("SEMUA (${logs.size})", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanAccent,
                                    selectedLabelColor = NavyDeep
                                )
                            )
                        }

                        items(LogLevel.values()) { level ->
                            val count = logs.count { it.level == level }
                            FilterChip(
                                selected = selectedFilter == level,
                                onClick = {
                                    viewModel.setLogLevelFilter(if (selectedFilter == level) null else level)
                                },
                                label = { Text("${level.name} ($count)", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(level.colorHex),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Section: Log Items
        if (filteredLogs.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NavySurface,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        text = "Tidak ditemukan log yang sesuai dengan filter atau kata kunci.",
                        color = TextSecondaryDark,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(filteredLogs) { logEntry ->
                LogItemCard(logEntry = logEntry)
            }
        }
    }
}

@Composable
fun LogItemCard(logEntry: LogEntry) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = NavySurface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(logEntry.level.colorHex).copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(logEntry.level.colorHex).copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = logEntry.level.name,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(logEntry.level.colorHex),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = logEntry.service,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CyanAccent,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = logEntry.timestamp,
                    fontSize = 10.sp,
                    color = TextSecondaryDark,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = logEntry.message,
                fontSize = 11.sp,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )

            if (logEntry.stackTrace != null) {
                Spacer(modifier = Modifier.height(6.dp))
                if (isExpanded) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NavyDeep,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = logEntry.stackTrace,
                            fontSize = 10.sp,
                            color = RoseError,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                } else {
                    Text(
                        text = "▶ Klik untuk melihat Stack Trace pelacakan baris kode...",
                        fontSize = 10.sp,
                        color = TextSecondaryDark
                    )
                }
            }
        }
    }
}
