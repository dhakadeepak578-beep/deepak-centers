package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.outlined.AssignmentTurnedIn
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AppTab

@Composable
fun AppBottomNavigation(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val tabs = listOf(
            Triple(AppTab.SERVICES, Icons.Filled.Assignment, Icons.Outlined.Assignment),
            Triple(AppTab.AI_ASSISTANT, Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
            Triple(AppTab.CHECKLIST, Icons.Filled.AssignmentTurnedIn, Icons.Outlined.AssignmentTurnedIn),
            Triple(AppTab.UPDATES, Icons.Filled.Campaign, Icons.Outlined.Campaign),
            Triple(AppTab.TRACKING, Icons.Filled.TrackChanges, Icons.Outlined.TrackChanges),
            Triple(AppTab.APPLY_UPLOAD, Icons.Filled.CloudUpload, Icons.Outlined.CloudUpload),
            Triple(AppTab.CENTER_INFO, Icons.Filled.LocationCity, Icons.Outlined.LocationCity)
        )

        tabs.forEach { (tab, filledIcon, outlinedIcon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    if (tab == AppTab.UPDATES) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFFD32F2F),
                                    contentColor = Color.White
                                ) {
                                    Text("6")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) filledIcon else outlinedIcon,
                                contentDescription = tab.titleEn,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) filledIcon else outlinedIcon,
                            contentDescription = tab.titleEn,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.titleHi,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF0D47A1),
                    selectedTextColor = Color(0xFF0D47A1),
                    indicatorColor = Color(0xFFE3F2FD),
                    unselectedIconColor = Color(0xFF64748B),
                    unselectedTextColor = Color(0xFF64748B)
                ),
                modifier = Modifier.testTag("nav_${tab.name.lowercase()}")
            )
        }
    }
}
