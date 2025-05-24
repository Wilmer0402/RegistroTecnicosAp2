package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MensajeScreen(viewModel: MensajesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        TextField(
            value = uiState.contenido ?: "",
            onValueChange = { viewModel.onEvent(MensajeEvent.ContenidoChange(it)) },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = uiState.remitente ?: "",
            onValueChange = { viewModel.onEvent(MensajeEvent.RemitenteChange(it)) },
            label = { Text("Remitente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { viewModel.onEvent(MensajeEvent.Save) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Mensaje")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mensajes del Ticket ${uiState.ticketId}", style = MaterialTheme.typography.h6)

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(uiState.mensajes) { mensaje ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Remitente: ${mensaje.remitente}")
                        Text("Contenido: ${mensaje.contenido}")
                        Text("Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm").format(mensaje.fecha)}")
                    }
                }
            }
        }

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colors.error)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMensajeScreen() {
    val fakeState = MensajeUiState(
        contenido = "Mensaje de prueba",
        remitente = "Juan Pérez",
        fecha = Date(),
        ticketId = 1,
        mensajes = listOf(
            MensajeEntity(1, Date(), "Contenido 1", "Remitente 1", 1),
            MensajeEntity(2, Date(), "Contenido 2", "Remitente 2", 1)
        )
    )

    MaterialTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            TextField(
                value = fakeState.contenido ?: "",
                onValueChange = {},
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = fakeState.remitente ?: "",
                onValueChange = {},
                label = { Text("Remitente") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Mensaje")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Mensajes del Ticket ${fakeState.ticketId}", style = MaterialTheme.typography.h6)

            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(fakeState.mensajes) { mensaje ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Remitente: ${mensaje.remitente}")
                            Text("Contenido: ${mensaje.contenido}")
                            Text("Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm").format(mensaje.fecha)}")
                        }
                    }
                }
            }
        }
    }
}
