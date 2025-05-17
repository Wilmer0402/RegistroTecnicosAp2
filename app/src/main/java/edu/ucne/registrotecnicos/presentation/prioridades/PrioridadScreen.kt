package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.compose.foundation.BorderStroke
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
import androidx.navigation.NavController
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity

@Composable
fun PrioridadScreen(
    prioridadId: Int? = null,
    viewModel: PrioridadesViewModel,
    navController: NavController
) {
    var descripcion by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }
    var editando by remember { mutableStateOf<PrioridadEntity?>(null) }

    LaunchedEffect(prioridadId) {
        if (prioridadId != null && prioridadId > 0) {
            val prioridad = viewModel.findPrioridad(prioridadId)
            prioridad?.let {
                editando = it
                descripcion = it.descripcion
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "volver")
                }
            }

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Registro de Prioridades",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF9C27B0),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    OutlinedTextField(
                        value = editando?.prioridadId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Prioridad") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        placeholder = { Text("Ej: Alta, Media, Baja") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF9C27B0)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                descripcion = ""
                                errorMessage = null
                                editando = null
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF9C27B0)
                            ),
                            border = BorderStroke(1.dp, Color(0xFF9C27B0)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "nuevo")
                            Text("Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (descripcion.isBlank()) {
                                    errorMessage = "La descripción no puede estar vacía."
                                    return@OutlinedButton
                                }

                                viewModel.savePrioridad(
                                    PrioridadEntity(
                                        prioridadId = editando?.prioridadId,
                                        descripcion = descripcion
                                    )
                                )

                                descripcion = ""
                                errorMessage = null
                                editando = null

                                navController.navigateUp()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF9C27B0)
                            ),
                            border = BorderStroke(1.dp, Color(0xFF9C27B0)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "guardar")
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}


