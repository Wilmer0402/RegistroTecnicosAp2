package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

@Composable
fun TecnicoListScreen(
    viewModel: TecnicosViewModel = hiltViewModel(),
    goToTecnico: (Int) -> Unit,
    createTecnico: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoListBodyScreen(
        uiState = uiState,
        goToTecnico = goToTecnico,
        createTecnico = createTecnico,
        deleteTecnico = { tecnico ->
            viewModel.onEvent(TecnicoEvent.TecnicoChange(tecnico.tecnicoId ?: 0))
            viewModel.onEvent(TecnicoEvent.Delete)
        }
    )
}

@Composable
private fun TecnicoRow(
    tecnico: TecnicoEntity,
    goToTecnico: (Int) -> Unit,
    deleteTecnico: (TecnicoEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // ID
            Text(
                text = "ID: ${tecnico.tecnicoId ?: 0}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            // Nombre
            Text(
                text = tecnico.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(3f)
            )
            // Sueldo
            Text(
                text = tecnico.sueldo.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            // Botones editar y eliminar
            Row {
                IconButton(
                    onClick = { goToTecnico(tecnico.tecnicoId ?: 0) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = { deleteTecnico(tecnico) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListBodyScreen(
    uiState: TecnicoUiState,
    goToTecnico: (Int) -> Unit,
    createTecnico: () -> Unit,
    deleteTecnico: (TecnicoEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Técnicos",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = createTecnico,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar nuevo técnico")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(uiState.tecnico) { tecnico ->
                TecnicoRow(
                    tecnico = tecnico,
                    goToTecnico = goToTecnico,
                    deleteTecnico = deleteTecnico
                )
            }
        }
    }
}
