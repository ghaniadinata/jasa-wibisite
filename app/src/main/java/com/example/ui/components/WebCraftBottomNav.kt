package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavySurface
import com.example.ui.viewmodel.AppNavTab

@Composable
fun WebCraftBottomNav(
    currentTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = NavySurface,
        tonalElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        val tabs = listOf(
            Triple(AppNavTab.PORTAL, Icons.Default.Dashboard, "Portal Klien"),
            Triple(AppNavTab.KATALOG, Icons.Default.Calculate, "Katalog"),
            Triple(AppNavTab.DEVOPS, Icons.Default.DeveloperBoard, "DevOps"),
            Triple(AppNavTab.LOGS, Icons.Default.Terminal, "Logs"),
            Triple(AppNavTab.SECURITY, Icons.Default.Security, "Keamanan"),
            Triple(AppNavTab.ANALYTICS, Icons.Default.Insights, "Analitik")
        )

        tabs.forEach { (tab, icon, label) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0A0F1D),
                    selectedTextColor = CyanAccent,
                    indicatorColor = CyanAccent,
                    unselectedIconColor = Color(0xFF9CA3AF),
                    unselectedTextColor = Color(0xFF9CA3AF)
                ),
                modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
            )
        }
    }
}
