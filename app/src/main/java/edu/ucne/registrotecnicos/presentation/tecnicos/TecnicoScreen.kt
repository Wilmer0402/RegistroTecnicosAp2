package edu.ucne.registrotecnicos.presentation.tecnicos

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
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

@Composable
fun TecnicoScreen(
    tecnicoId: Int? = null,
    viewModel: TecnicosViewModel,
    navController: NavController,
    function: () -> Unit
) {
    var nombre: String by remember { mutableStateOf("") }
    var sueldo: Double by remember { mutableStateOf(0.0) }
    var sueldoTexto by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }
    var editando by remember { mutableStateOf<TecnicoEntity?>(null) }

    LaunchedEffect(tecnicoId) {
        if (tecnicoId != null && tecnicoId > 0) {
            val tecnico = viewModel.findTecnico(tecnicoId)
            tecnico?.let {
                editando = it
                nombre = it.nombre
                sueldo = it.sueldo
                sueldoTexto = it.sueldo.toString()
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
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Registro de Técnicos",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF9C27B0),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.padding(4.dp))

                    OutlinedTextField(
                        value = editando?.tecnicoId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Técnico") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Técnico") },
                        placeholder = { Text("Ej: Juan Pérez") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF9C27B0)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = sueldoTexto,
                        onValueChange = { newValue ->
                            sueldoTexto = newValue
                            sueldo = newValue.toDoubleOrNull() ?: 0.0
                        },
                        label = { Text("Sueldo del Técnico") },
                        placeholder = { Text("Ej: 550.75") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF9C27B0),
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = Color(0xFF9C27B0)
                        )
                    )

                    Spacer(modifier = Modifier.padding(6.dp))
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
                                nombre = ""
                                sueldo = 0.0
                                sueldoTexto = ""
                                errorMessage = null
                                editando = null
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF9C27B0)
                            ),
                            border = BorderStroke(1.dp, Color(0xFF9C27B0)),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "new button")
                            Text("Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (nombre.isBlank()) {
                                    errorMessage = "El nombre no puede estar vacío."
                                    return@OutlinedButton
                                }
                                if (sueldo <= 0.0) {
                                    errorMessage = "El sueldo no puede ser cero o menor."
                                    return@OutlinedButton
                                }
                                viewModel.saveTecnico(
                                    TecnicoEntity(
                                        tecnicoId = editando?.tecnicoId,
                                        nombre = nombre,
                                        sueldo = sueldo
                                    )
                                )
                                nombre = ""
                                sueldo = 0.0
                                sueldoTexto = ""
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
                            Icon(Icons.Default.Edit, contentDescription = "save button")
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}




/*@Preview(showBackground = true)
@Composable
private fun TecnicoPreview() {
    val navController = rememberNavController()
    val viewModel = TecnicosViewModel(
        tecnicosRepository = TODO()
    )
    RegistroTecnicosTheme {
        TecnicoScreen(
            tecnicoId = null,
            viewModel = viewModel,
            navController = navController
        ) {}
    }
}
*/