package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    onEdit: (Int?) -> Unit,
    onDelete: (TicketEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tickets",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF9C27B0),
                ) })

        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEdit(0) }) {
                Icon(Icons.Filled.Add, "Agregar nueva")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(ticketList) { ticket ->
                    TicketRow(ticket, { onEdit(ticket.ticketId) }, { onDelete(ticket) })
                }
            }
        }
    }
}


@Composable
private fun TicketRow(
    ticket: TicketEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
    val formattedDate = dateFormatter.format(ticket.fecha)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = ticket.ticketId.toString())
        Text(
            modifier = Modifier.weight(2f),
            text = formattedDate,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            modifier = Modifier.weight(2f),
            text = ticket.descripcion,
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Editar")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
    HorizontalDivider()
}
