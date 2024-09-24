package template.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


@Composable
fun BottomNavBar(
    navController: NavController,
    currentSelectedScreen: Screen
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentSelectedScreen == Screen.Teams,
            onClick = { navController.navigate(Screen.Teams) },
            label = {
                Text(text = "Home")
            },
            icon = {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        )
        NavigationBarItem(
            selected = currentSelectedScreen == Screen.Players,
            onClick = { navController.navigate(Screen.Players) },
            label = {
                Text(text = "Players")
            },
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        )
    }
}