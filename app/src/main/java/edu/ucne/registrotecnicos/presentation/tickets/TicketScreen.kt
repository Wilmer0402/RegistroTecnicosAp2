package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TicketScreen(
    viewModel: TicketsViewModel = hiltViewModel(),
    ticketId: Int?,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        ticketId?.let {
            if (it > 0) {
                viewModel.findTicket(it)
            }
        }
    }

    TicketBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    uiState: TicketUiState,
    onEvent: (TicketEvent) -> Unit,
    goBack: () -> Unit
) {
    val primaryBlue = Color(0xFF2196F3)
    val accentGreen = Color(0xFF4CAF50)
    val brightYellow = Color(0xFFFFEB3B)
    val lightBackground = Color(0xFFF1F8E9)

    var expandedPrioridad by remember { mutableStateOf(false) }
    var expandedTecnico by remember { mutableStateOf(false) }

    Scaffold(containerColor = lightBackground) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = goBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = primaryBlue)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Registro de Tickets",
                        style = MaterialTheme.typography.headlineMedium,
                        color = accentGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = uiState.ticketId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Ticket") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Prioridad Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = !expandedPrioridad }
                    ) {
                        OutlinedTextField(
                            value = uiState.prioridades.find { it.prioridadId == uiState.prioridadId }?.descripcion ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Prioridad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryBlue,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = primaryBlue
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            uiState.prioridades.forEach { prioridad ->
                                DropdownMenuItem(
                                    text = { Text(prioridad.descripcion) },
                                    onClick = {
                                        onEvent(TicketEvent.PrioridadIdChange(prioridad.prioridadId ?: 0))
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.cliente ?: "",
                        onValueChange = { onEvent(TicketEvent.ClienteChange(it)) },
                        label = { Text("Nombre del cliente") },
                        placeholder = { Text("Ej: Juan Pérez") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.asunto ?: "",
                        onValueChange = { onEvent(TicketEvent.AsuntoChange(it)) },
                        label = { Text("Asunto") },
                        placeholder = { Text("Ej: Impresora, Red, Etc") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.descripcion ?: "",
                        onValueChange = { onEvent(TicketEvent.DescripcionChange(it)) },
                        label = { Text("Descripción") },
                        placeholder = { Text("Ej: Red Lenta, Tinta, Etc") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Técnico Dropdown
                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico }
                    ) {
                        OutlinedTextField(
                            value = uiState.tecnicos.find { it.tecnicoId == uiState.tecnicoId }?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Técnico") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTecnico) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryBlue,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = primaryBlue
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            uiState.tecnicos.forEach { tecnico ->
                                DropdownMenuItem(
                                    text = { Text(tecnico.nombre) },
                                    onClick = {
                                        onEvent(TicketEvent.TecnicoIdChange(tecnico.tecnicoId ?: 0))
                                        expandedTecnico = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    uiState.errorMessage?.let {
                        Text(
                            text = it,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { onEvent(TicketEvent.New) },
                            colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = brightYellow)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nuevo", color = Color.White)
                        }

                        Button(
                            onClick = {
                                onEvent(TicketEvent.Save)
                                goBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Guardar", tint = brightYellow)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Guardar", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
