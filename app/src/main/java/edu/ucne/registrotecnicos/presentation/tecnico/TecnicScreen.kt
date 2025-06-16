package edu.ucne.registrotecnicos.presentation.tecnico

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.UiEvent
import kotlinx.coroutines.launch

@Composable
fun TecnicScreen(
    tecnicoId: Int? = null,
    viewModel: TecnicViewModel = hiltViewModel(),
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(tecnicoId) {
        tecnicoId?.let {
            if (it > 0) {
                viewModel.onEvent(TecnicEvent.GetTecnicos(it))
            }
        }
    }

    TecnicBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack,
        viewModel = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicBodyScreen(
    uiState: TecnicUiState,
    onEvent: (TecnicEvent) -> Unit,
    goBack: () -> Unit,
    viewModel: TecnicViewModel
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val primaryBlue = Color(0xFF2196F3)
    val accentGreen = Color(0xFF4CAF50)
    val brightYellow = Color(0xFFFFEB3B)
    val lightBackground = Color(0xFFF1F8E9)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavigateUp -> goBack()
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.isSuccess || !uiState.errorMessage.isNullOrBlank()) {
        if (uiState.isSuccess && !uiState.successMessage.isNullOrBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.successMessage,
                    duration = SnackbarDuration.Short
                )
                onEvent(TecnicEvent.ResetSuccessMessage)
            }
        } else if (!uiState.errorMessage.isNullOrBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.errorMessage,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = lightBackground
    ) { innerPadding ->
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
                Spacer(modifier = Modifier.width(8.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Nuevo Técnico",
                        style = MaterialTheme.typography.headlineMedium,
                        color = accentGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = uiState.tecnicoId?.toString() ?: "0",
                        onValueChange = {},
                        label = { Text("ID Técnico") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.nombre ?: "",
                        onValueChange = { onEvent(TecnicEvent.NombreChange(it)) },
                        label = { Text("Nombre del Técnico") },
                        placeholder = { Text("Ej: Juan Pérez") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = !uiState.errorNombre.isNullOrBlank(),
                        supportingText = {
                            uiState.errorNombre?.let { error -> Text(text = error, color = Color.Red) }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = uiState.sueldo?.toString() ?: "",
                        onValueChange = {
                            onEvent(TecnicEvent.SueldoChange(it.toDoubleOrNull() ?: 0.0))
                        },
                        label = { Text("Sueldo por Hora") },
                        placeholder = { Text("Ej: 550.75") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        isError = uiState.sueldo != null && uiState.sueldo <= 0 && !uiState.errorSueldo.isNullOrBlank(),
                        supportingText = {
                            uiState.errorSueldo?.let { error -> Text(text = error, color = Color.Red) }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = primaryBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { onEvent(TecnicEvent.Nuevo) },
                            colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", tint = brightYellow)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Limpiar", color = Color.White)
                        }

                        Button(
                            onClick = { onEvent(TecnicEvent.PostTecnic) },
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
