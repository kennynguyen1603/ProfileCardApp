package com.example.profilecardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            ProfileCardAppTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            listOf(
                                Screen.Profile to Icons.Default.Person,
                                Screen.Gallery to Icons.Default.PhotoLibrary,
                                Screen.Settings to Icons.Default.Settings
                            ).forEach { (screen, icon) ->
                                NavigationBarItem(
                                    icon = { Icon(icon, contentDescription = screen.title) },
                                    label = { Text(screen.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Profile.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Profile.route) {
                            ProfileScreen()
                        }
                        composable(Screen.Gallery.route) {
                            GalleryScreen()
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(
                                darkTheme = darkTheme,
                                onThemeChanged = { darkTheme = it }
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String) {
    object Profile : Screen("profile", "Profile")
    object Gallery : Screen("gallery", "Gallery")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun ProfileScreen() {
    var expanded by remember { mutableStateOf(false) }
    val extraPadding = animateDpAsState(
        targetValue = if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "padding animation"
    ).value.coerceAtLeast(0.dp)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clickable { expanded = !expanded },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = extraPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .shadow(12.dp, CircleShape)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        AsyncImage(
                            model = "https://i.pinimg.com/originals/03/86/c4/0386c49bdd4bc5e2e59a77f0ae86b8bb.jpg",
                            contentDescription = "Profile Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "John Smith",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

//                    Text(
//                        text = "Califonia, LA",
//                        fontSize = 18.sp,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(top = 4.dp)
//                    )

                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ProfileInfoItem(
                                icon = Icons.Default.Email,
                                label = "Email",
                                value = "kenny@example.com"
                            )

                            ProfileInfoItem(
                                icon = Icons.Default.Phone,
                                label = "Phone",
                                value = "+84 123 456 789"
                            )

                            ProfileInfoItem(
                                icon = Icons.Default.LocationOn,
                                label = "Location",
                                value = "Ho Chi Minh City, Vietnam"
                            )

                            Text(
                                text = "Tap to collapse",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    if (!expanded) {
                        Text(
                            text = "Tap to expand",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Media Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SocialButton(
                    icon = Icons.Default.Share,
                    color = Color(0xFF1877F2), // Facebook blue
                    onClick = { /* Handle click */ }
                )

                SocialButton(
                    icon = Icons.AutoMirrored.Filled.Send,
                    color = Color(0xFF1DA1F2), // Twitter blue
                    onClick = { /* Handle click */ }
                )

                SocialButton(
                    icon = Icons.Default.Favorite,
                    color = Color(0xFFE4405F), // Instagram pink
                    onClick = { /* Handle click */ }
                )

                SocialButton(
                    icon = Icons.Default.Call,
                    color = Color(0xFF0e76a8), // LinkedIn blue
                    onClick = { /* Handle click */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(label = "Posts", value = "128")
                    StatItem(label = "Followers", value = "8,542")
                    StatItem(label = "Following", value = "753")
                }
            }
        }
    }
}

@Composable
fun ProfileInfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SocialButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .background(color)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun GalleryScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Photo Gallery",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Coming Soon!",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SettingsScreen(darkTheme: Boolean, onThemeChanged: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Dark Theme",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    Switch(
                        checked = darkTheme,
                        onCheckedChange = onThemeChanged,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }

            // Additional settings items
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage notification preferences"
            )

            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Privacy",
                subtitle = "Control your privacy settings"
            )

            SettingsItem(
                icon = Icons.AutoMirrored.Filled.Help,
                title = "Help & Support",
                subtitle = "Get assistance and answers"
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "App version 1.0.0"
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileCardAppTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF9575CD),
            onPrimary = Color.White,
            primaryContainer = Color(0xFF4527A0),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFF80DEEA),
            onSecondary = Color.Black,
            secondaryContainer = Color(0xFF0097A7),
            onSecondaryContainer = Color.White,
            tertiary = Color(0xFFFFAB91),
            onTertiary = Color.Black,
            tertiaryContainer = Color(0xFFE64A19),
            onTertiaryContainer = Color.White,
            background = Color(0xFF121212),
            onBackground = Color.White,
            surface = Color(0xFF1E1E1E),
            onSurface = Color.White,
            surfaceVariant = Color(0xFF2D2D2D),
            onSurfaceVariant = Color.LightGray
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF673AB7),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFD1C4E9),
            onPrimaryContainer = Color(0xFF311B92),
            secondary = Color(0xFF00BCD4),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFB2EBF2),
            onSecondaryContainer = Color(0xFF006064),
            tertiary = Color(0xFFFF5722),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFCCBC),
            onTertiaryContainer = Color(0xFFBF360C),
            background = Color.White,
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black,
            surfaceVariant = Color(0xFFF5F5F5),
            onSurfaceVariant = Color.DarkGray
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
