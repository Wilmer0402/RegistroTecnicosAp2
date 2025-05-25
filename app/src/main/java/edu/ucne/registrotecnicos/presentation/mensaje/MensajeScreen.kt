package edu.ucne.registrotecnicos.presentation.mensaje

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MensajeScreen(
    viewModel: MensajesViewModel = hiltViewModel(),
    goBack: () -> Unit,
    ticketId: Int
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(ticketId) {
        viewModel.onEvent(MensajeEvent.TicketIdChange(ticketId))
    }

    MensajeBodyScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goBack = goBack
    )
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MensajeBodyScreen(
    uiState: MensajeUiState,
    onEvent: (MensajeEvent) -> Unit,
    goBack: () -> Unit
) {
    var selectedRemitente by remember { mutableStateOf(uiState.remitente ?: "") }
    val listState = rememberLazyListState()

    // Ordenamos mensajes por fecha ascendente (más antiguos arriba)
    val mensajesOrdenados = uiState.mensajes.sortedBy { it.fecha }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Messages", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(mensajesOrdenados) { mensaje ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut()
                    ) {
                        MensajeRow(mensaje)
                    }
                }
            }
            LaunchedEffect(mensajesOrdenados.size) {
                if (mensajesOrdenados.isNotEmpty()) {
                    listState.animateScrollToItem(mensajesOrdenados.lastIndex)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Text("Responder", style = MaterialTheme.typography.titleMedium)

            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf("Operator", "Owner").forEach { tipo ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = selectedRemitente == tipo,
                            onClick = {
                                selectedRemitente = tipo
                                onEvent(MensajeEvent.TipoRemitenteChange(tipo))
                            }
                        )
                        Text(tipo)
                    }
                }
            }

            if (selectedRemitente.isNotEmpty()) {
                OutlinedTextField(
                    value = uiState.remitente ?: "",
                    onValueChange = { onEvent(MensajeEvent.RemitenteChange(it)) },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                OutlinedTextField(
                    value = uiState.contenido ?: "",
                    onValueChange = { onEvent(MensajeEvent.ContenidoChange(it)) },
                    label = { Text("Mensaje") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )

                Button(
                    onClick = { onEvent(MensajeEvent.Save) },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Enviar", style = MaterialTheme.typography.labelLarge)
                }
            } else {
                Text(
                    text = "Seleccione un remitente para comenzar a escribir...",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun MensajeRow(mensaje: MensajeEntity) {
    val isOperator = mensaje.tipoRemitente == "Operator"
    val bubbleColor = if (isOperator) Color(0xFFFFF0F5) else Color(0xFFE0F7FA)
    val horizontalArrangement = if (isOperator) Arrangement.Start else Arrangement.End

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.Top
    ) {
        if (isOperator) {
            Avatar(nombre = "O") // Operator
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(bubbleColor, RoundedCornerShape(12.dp))
                    .padding(12.dp)
                    .widthIn(max = 300.dp)
            ) {
                Column {
                    Text(
                        text = mensaje.contenido,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Por ${mensaje.remitente} el ${
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(mensaje.fecha)
                        } (${mensaje.tipoRemitente})",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                        color = Color.Gray
                    )
                }
            }
        }

        if (!isOperator) {
            Spacer(modifier = Modifier.width(8.dp))
            Avatar(nombre = "U") // Owner
        }
    }
}

@Composable
fun Avatar(nombre: String) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Gray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
