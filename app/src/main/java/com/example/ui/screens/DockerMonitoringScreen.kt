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
import com.example.model.ContainerInfo
import com.example.model.ContainerState
import com.example.ui.theme.*
import com.example.ui.viewmodel.WebCraftViewModel

@Composable
fun DockerMonitoringScreen(
    viewModel: WebCraftViewModel,
    modifier: Modifier = Modifier
) {
    val containers by viewModel.containers.collectAsState()
    val serverReplicas by viewModel.serverReplicas.collectAsState()
    val isDeploying by viewModel.isDeploying.collectAsState()
    val deploymentLogs by viewModel.deploymentLogs.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AntiGravity Hero Banner
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = NavySurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CyanAccent.copy(alpha = 0.5f)),
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
                                Icon(Icons.Default.Widgets, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Text("AntiGravity Docker Orchestrator", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Runtime: Docker Compose v3.8 | Network: laravel-net", fontSize = 11.sp, color = TextSecondaryDark)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = EmeraldSuccess.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "CLUSTER HEALTHY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Cluster Quick Stats
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
                                Text("Total Containers", fontSize = 10.sp, color = TextSecondaryDark)
                                Text("${containers.size} Active", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavySurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Cluster Load", fontSize = 10.sp, color = TextSecondaryDark)
                                val avgCpu = containers.map { it.cpuPercent }.average()
                                Text(String.format("%.1f%%", avgCpu), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavySurfaceVariant,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Auto-Scaling", fontSize = 10.sp, color = TextSecondaryDark)
                                Text("$serverReplicas Replicas", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AmberWarning)
                            }
                        }
                    }
                }
            }
        }

        // Section: Responsive Server Scaling (AntiGravity Dynamic Scaler)
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
                            Text("Dukungan Skalabilitas Server Responsif", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("AntiGravity Auto-Scaler: Seimbangkan beban lonjakan traffic", fontSize = 11.sp, color = TextSecondaryDark)
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ElectricBlueDark.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = "$serverReplicas Pods",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Slider(
                        value = serverReplicas.toFloat(),
                        onValueChange = { viewModel.setReplicas(it.toInt()) },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = CyanAccent,
                            activeTrackColor = CyanAccent,
                            inactiveTrackColor = NavyBorder
                        ),
                        modifier = Modifier.testTag("server_scaling_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1 Instance (Hemat RAM)", fontSize = 10.sp, color = TextSecondaryDark)
                        Text("3 (Beban Normal)", fontSize = 10.sp, color = TextSecondaryDark)
                        Text("5 (Peak Traffic)", fontSize = 10.sp, color = CyanAccent)
                    }
                }
            }
        }

        // Section: 1-Click Automated CI/CD Deployment
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0F1B2E),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlueDark),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Deployment Otomatis & Pembaruan Sistem", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Zero-downtime container swap dengan rollback otomatis", fontSize = 11.sp, color = TextSecondaryDark)
                        }

                        Button(
                            onClick = { viewModel.triggerAutomatedDeployment() },
                            enabled = !isDeploying,
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent, contentColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("trigger_deployment_button")
                        ) {
                            if (isDeploying) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = NavyDeep, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Deploying...", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Jalankan CI/CD", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    if (deploymentLogs.isNotEmpty() || isDeploying) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NavyDeep,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                deploymentLogs.takeLast(6).forEach { logLine ->
                                    Text(
                                        text = logLine,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = if (logLine.contains("SUCCESS")) EmeraldSuccess else TextPrimaryDark
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Containers List
        item {
            Text(
                text = "Daftar Kontainer Docker Terisolasi",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(containers) { container ->
            ContainerCard(
                container = container,
                onRestart = { viewModel.restartContainer(container.id) }
            )
        }
    }
}

@Composable
fun ContainerCard(
    container: ContainerInfo,
    onRestart: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = NavySurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
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
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(Color(container.state.colorHex))
                    )
                    Text(
                        text = container.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(container.state.colorHex).copy(alpha = 0.2f)
                ) {
                    Text(
                        text = container.state.label,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(container.state.colorHex),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Image: ${container.image} | Port: ${container.port}", fontSize = 10.sp, color = TextSecondaryDark, fontFamily = FontFamily.Monospace)

            Spacer(modifier = Modifier.height(10.dp))

            // Resource Gauges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // CPU Gauge
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("CPU Usage", fontSize = 10.sp, color = TextSecondaryDark)
                        Text(String.format("%.1f%%", container.cpuPercent), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (container.cpuPercent / 100f).coerceIn(0f, 1f) },
                        color = if (container.cpuPercent > 75f) RoseError else CyanAccent,
                        trackColor = NavyBorder,
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                    )
                }

                // RAM Gauge
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Memory (RAM)", fontSize = 10.sp, color = TextSecondaryDark)
                        Text("${container.memoryMb.toInt()} MB", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldSuccess)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { (container.memoryMb / container.maxMemoryMb).coerceIn(0f, 1f) },
                        color = EmeraldSuccess,
                        trackColor = NavyBorder,
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = NavyBorder.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Uptime: ${container.uptime}", fontSize = 10.sp, color = TextSecondaryDark)
                    Text("I/O: ${container.networkIo}", fontSize = 10.sp, color = TextSecondaryDark)
                }

                IconButton(
                    onClick = onRestart,
                    modifier = Modifier.size(28.dp).testTag("restart_container_${container.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Restart Container",
                        tint = AmberWarning,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
