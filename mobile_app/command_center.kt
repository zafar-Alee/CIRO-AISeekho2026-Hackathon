import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandCenterScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Command Centre") }) } // [cite: 137]
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Card(modifier = Modifier.fillMaxWidth().height(150.dp).padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("LIVE OPS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error) // [cite: 138]
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text("UNITS ACTIVE"); Text("42", style = MaterialTheme.typography.headlineMedium) } // [cite: 139, 140]
                        Column { Text("SECTOR STATUS"); Text("Stable", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary) } // [cite: 139, 141]
                    }
                }
            }
            
            ScrollableTabRow(selectedTabIndex = 0) {
                Tab(selected = true, onClick = {}, text = { Text("Active") }) // [cite: 143]
                Tab(selected = false, onClick = {}, text = { Text("Rescue") }) // [cite: 144]
                Tab(selected = false, onClick = {}, text = { Text("Traffic") }) // [cite: 145]
            }

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item { Text("ACTIVE TASKING", style = MaterialTheme.typography.titleMedium) } // [cite: 148]
                item { 
                    TaskCard(
                        agency = "Rescue 1122", // [cite: 149]
                        task = "Flood Extraction: Orangi Town", // [cite: 151]
                        status = "On Site", // [cite: 152]
                        eta = "12 min" // [cite: 153]
                    )
                }
                item {
                    TaskCard(
                        agency = "Traffic Police", // [cite: 154]
                        task = "Route Clearance: Shahrah-e-Faisal", // [cite: 156]
                        status = "Deployed", // [cite: 158]
                        eta = "25 min" // [cite: 159]
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(agency: String, task: String, status: String, eta: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(agency, style = MaterialTheme.typography.labelMedium)
            Text(task, style = MaterialTheme.typography.bodyLarge)
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(status, color = MaterialTheme.colorScheme.primary)
                Text("ETA: $eta")
            }
        }
    }
}