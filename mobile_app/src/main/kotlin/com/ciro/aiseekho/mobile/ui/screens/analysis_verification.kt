package com.ciro.aiseekho.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisVerificationScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Flash Flood — Karachi") }) } // [cite: 87]
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Shahrah-e-Faisal / Nursery Flyover", color = MaterialTheme.colorScheme.error) // [cite: 88]
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("GEMINI AI ENGINE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary) // [cite: 89]
                    Text("95.4% Confidence", style = MaterialTheme.typography.headlineMedium) // [cite: 90]
                    Text("Verification status: Verified High-Priority", style = MaterialTheme.typography.bodySmall) // [cite: 91]
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("PREDICTED IMPACT ANALYSIS", style = MaterialTheme.typography.labelMedium) // [cite: 92]
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyVerticalGrid(columns = GridCells.Fixed(2), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { ImpactCard("08", "ROAD CLOSURES", "CRITICAL") } // [cite: 93, 94, 98]
                item { ImpactCard("142", "STRANDED VEHICLES", "ELEVATED") } // [cite: 107, 108, 109]
                item { ImpactCard("12k", "POWER OUTAGES", "HIGH") } // [cite: 96, 97, 99]
                item { ImpactCard("+2.4m", "WATER LEVEL", "STABLE") } // [cite: 110, 111, 112]
            }
        }
    }
}

@Composable
fun ImpactCard(value: String, title: String, status: String) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(status, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium)
            Text(title, style = MaterialTheme.typography.bodySmall)
        }
    }
}