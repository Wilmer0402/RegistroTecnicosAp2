package edu.ucne.registrotecnicos.presentation.prioridades

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
fun PrioridadScreen(
    prioridadId: Int?,
    viewModel: PrioridadesViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(prioridadId) {
        prioridadId?.let {
            if (it > 0) {
                viewModel.findPrioridad(it)
            }
        }
    }

    PrioridadBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadBodyScreen(
    uiState: PrioridadUiState,
    onEvent: (PrioridadEvent) -> Unit,
    goBack: () -> Unit
) {
    // Colores iguales a archivo 1
    val primaryBlue = Color(0xFF2196F3)
    val accentGreen = Color(0xFF4CAF50)
    val brightYellow = Color(0xFFFFEB3B)
    val lightBackground = Color(0xFFF1F8E9)

    Scaffold(
        containerColor = lightBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically) {
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
                        text = "Registro de Prioridades",
                        style = MaterialTheme.typography.headlineMedium,
                        color = accentGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = uiState.prioridadId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Prioridad") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.descripcion ?: "",
                        onValueChange = { onEvent(PrioridadEvent.DescripcionChange(it)) },
                        label = { Text("Descripción") },
                        placeholder = { Text("Ej: Alta, Media, Baja") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryBlue
                        )
                    )

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
                            onClick = { onEvent(PrioridadEvent.New) },
                            colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo", tint = brightYellow)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Nuevo", color = Color.White)
                        }

                        Button(
                            onClick = {
                                onEvent(PrioridadEvent.Save)
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
