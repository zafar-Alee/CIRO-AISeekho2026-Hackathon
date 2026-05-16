package com.ciro.aiseekho.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomingSignalFeedScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Signal Feed") }) } // [cite: 39]
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("QUEUE STATUS", style = MaterialTheme.typography.labelSmall) // [cite: 40]
                    Text("124 Signals", style = MaterialTheme.typography.titleLarge) // [cite: 41]
                }
                Column {
                    Text("UNVERIFIED", style = MaterialTheme.typography.labelSmall) // [cite: 42]
                    Text("86", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.error) // [cite: 43]
                }
            }
            
            ScrollableTabRow(selectedTabIndex = 0) {
                Tab(selected = true, onClick = {}, text = { Text("All Sources") }) // [cite: 44]
                Tab(selected = false, onClick = {}, text = { Text("SMS (42)") }) // [cite: 44]
                Tab(selected = false, onClick = {}, text = { Text("Twitter (68)") }) // [cite: 45]
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { 
                    FeedCard(
                        user = "@KHISAFETYNET", // [cite: 48]
                        time = "2m ago", // [cite: 49]
                        rawText = "Shahrah-e-Faisal pe shadeed barish aur pani jama hai...", // [cite: 50]
                        translatedText = "Heavy rainfall causing water logging at Shahrah-e-Faisal. Nursery flyover is currently blocked.", // [cite: 51, 52]
                        location = "PECHS, Shahrah-e-Faisal" // [cite: 53]
                    )
                }
                item {
                    FeedCard(
                        user = "+92 300 1234567", // [cite: 56]
                        time = "8m ago", // [cite: 57]
                        rawText = "Sohrab Goth traffic jammed due to trailer breakdown.", // [cite: 58]
                        translatedText = "Massive traffic congestion at Sohrab Goth following a heavy vehicle mechanical failure.", // [cite: 59]
                        location = "Sohrab Goth, District East" // [cite: 60]
                    )
                }
            }
        }
    }
}

@Composable
fun FeedCard(user: String, time: String, rawText: String, translatedText: String, location: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(user, style = MaterialTheme.typography.labelMedium)
                Text(time, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("\"$rawText\"", style = MaterialTheme.typography.bodyMedium, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
            Spacer(modifier = Modifier.height(4.dp))
            Text(translatedText, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(location, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                Button(onClick = {}, modifier = Modifier.height(32.dp)) { Text("Verify") } // [cite: 54, 61]
            }
        }
    }
}