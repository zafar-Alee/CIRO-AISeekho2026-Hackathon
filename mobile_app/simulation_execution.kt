import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulationExecutionScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Tactical Simulation") }) } // [cite: 189]
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("TACTICAL SCENARIO", style = MaterialTheme.typography.labelSmall) // [cite: 190]
            Text("Shahrah-e-Faisal Diversion", style = MaterialTheme.typography.headlineSmall) // [cite: 191]
            Text("T+02:45:00", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error) // [cite: 192]
            
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Optimized Routing Impact", style = MaterialTheme.typography.titleMedium) // [cite: 198]
                    Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("ORIGINAL ETA", style = MaterialTheme.typography.labelSmall) // [cite: 200]
                            Text("48 Mins", style = MaterialTheme.typography.headlineMedium) // [cite: 201]
                        }
                        Column {
                            Text("SIMULATED ETA", style = MaterialTheme.typography.labelSmall) // [cite: 204]
                            Text("26 Mins", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary) // [cite: 205]
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("-22 MINS SAVED", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary) // [cite: 206]
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("ACTIVE UNITS IN SIMULATION", style = MaterialTheme.typography.labelMedium) // [cite: 203]
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("M-04 Amb (E-45)", style = MaterialTheme.typography.titleMedium) // [cite: 207]
                        Text("0.8km away", style = MaterialTheme.typography.bodySmall) // [cite: 208]
                    }
                    Text("82% FUEL", style = MaterialTheme.typography.labelMedium) // [cite: 209]
                }
            }
        }
    }
}