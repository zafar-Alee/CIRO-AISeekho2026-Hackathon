import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide BottomBar on Login/Signup
    val showBottomBar = currentRoute !in listOf("login", "signup")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(
                        "dashboard" to "Dashboard", // [cite: 31]
                        "feed" to "Feed",           // [cite: 32]
                        "analysis" to "Analysis",   // [cite: 33]
                        "command" to "Command",     // [cite: 34]
                        "simulation" to "Simulation"// [cite: 35]
                    )
                    items.forEach { (route, label) ->
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Place, contentDescription = label) }, // Replace with specific Visily icons
                            label = { Text(label) },
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignupScreen(navController) }
            composable("dashboard") { DashboardScreen() }
            composable("feed") { IncomingSignalFeedScreen() }
            composable("analysis") { AnalysisVerificationScreen() }
            composable("command") { CommandCenterScreen() }
            composable("simulation") { SimulationExecutionScreen() }
        }
    }
}