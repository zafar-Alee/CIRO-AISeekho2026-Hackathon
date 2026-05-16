import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Karachi, Pakistan") }) // [cite: 2]
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatCard("ACTIVE CRISES", "14", "+2 in 60m") // [cite: 3, 4, 5]
                    StatCard("BLOCKED ROUTES", "08", "-1 in 30m") // [cite: 6, 7, 8]
                }
            }
            item {
                Text("LIVE TACTICAL MAP", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium) // [cite: 9]
                // Placeholder for Map Component (e.g., Google Maps Compose)
                Card(modifier = Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 16.dp)) {
                    CenterAlignedTopAppBar(title = { Text("Map View (Lyari, Saddar, Keamari)") }) // [cite: 10, 11, 12]
                }
            }
            item {
                Text("SIGNAL STREAM", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium) // [cite: 16]
            }
            items(2) { index ->
                if(index == 0) SignalItem("SMS-1122", "بڑی آگ لگ گئی ہے، فوراً مدد بھیجیں۔", "Lyari, Sector 4") // [cite: 18, 23, 24]
                else SignalItem("CITIZENAPP", "ٹریفک جام ہے، راستہ بلاک ہو گیا ہے۔", "Saddar Market") // [cite: 25, 26, 27]
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, subtitle: String) {
    Card(modifier = Modifier.width(160.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Text(value, style = MaterialTheme.typography.headlineLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun SignalItem(source: String, message: String, location: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(source, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(message, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(location, style = MaterialTheme.typography.bodySmall)
        }
    }
}