// MainActivity.kt
package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close



data class TodoItem(
    val id: Long,
    val label: String,
    val completed: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TodoScreen()
                }
            }
        }
    }
}

@Composable
fun TodoScreen() {
    // Hoisted state
    var items: List<TodoItem> by rememberSaveable(stateSaver = listSaver(
        save = { list -> list.map { "${it.id}|${it.label}|${it.completed}" } },
        restore = { saved ->
            saved.map {
                val parts = it.split(  "|")
                TodoItem(parts[0].toLong(), parts[1], parts[2].toBoolean())
            }
        }
    )) { mutableStateOf(emptyList()) }

    val onAdd: (String) -> Unit = { label ->
        items = items + TodoItem(System.currentTimeMillis(), label.trim())
    }
    val onToggle: (TodoItem) -> Unit = { item ->
        items = items.map {
            if (it.id == item.id) it.copy(completed = !it.completed) else it
        }
    }
    val onDelete: (TodoItem) -> Unit = { item ->
        items = items.filter { it.id != item.id }
    }

    TodoContent(
        items = items,
        onAdd = onAdd,
        onToggle = onToggle,
        onDelete = onDelete
    )
}

@Composable
fun TodoContent(
    items: List<TodoItem>,
    onAdd: (String) -> Unit,
    onToggle: (TodoItem) -> Unit,
    onDelete: (TodoItem) -> Unit
) {
    val activeItems = items.filter { !it.completed }
    val completedItems = items.filter { it.completed }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        AddItemRow(onAdd = onAdd)

        Spacer(Modifier.height(16.dp))

        if (activeItems.isNotEmpty()) {
            Text("Items", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            activeItems.forEach { item ->
                TodoRow(item, onToggle, onDelete)
            }
        } else {
            Text("No items yet", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))

        if (completedItems.isNotEmpty()) {
            Text("Completed Items", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            completedItems.forEach { item ->
                TodoRow(item, onToggle, onDelete)
            }
        } else {
            if (items.isNotEmpty()) {
                Text("No completed items", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun AddItemRow(onAdd: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {Text("Add a task")},
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    if (text.isBlank()) {
                        Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT).show()
                    } else {
                        onAdd(text)
                        text = ""
                    }
                }
            ),
        )
        Button(onClick = {
            if (text.isBlank()) {
                Toast.makeText(context, "Please enter a task", Toast.LENGTH_SHORT).show()
            } else {
                onAdd(text)
                text = ""
            }
        }) {
            Text("Add")
        }
    }
}

@Composable
fun TodoRow(item: TodoItem, onToggle: (TodoItem) -> Unit, onDelete: (TodoItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.completed,
            onCheckedChange = { onToggle(item) }
        )
        Text(
            text = item.label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(onClick = { onDelete(item) }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete item"
            )
        }
    }
}
