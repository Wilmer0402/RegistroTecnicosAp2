package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListScreen(
    prioridadList: List<PrioridadEntity>,
    onEdit: (Int?) -> Unit,
    onDelete: (PrioridadEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Lista de Prioridades",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFF9C27B0),
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEdit(0) }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Nueva Prioridad")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(prioridadList) { prioridad ->
                    PrioridadRow(prioridad, { onEdit(prioridad.prioridadId) }, { onDelete(prioridad) })
                }
            }
        }
    }
}

@Composable
private fun PrioridadRow(
    prioridad: PrioridadEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = prioridad.prioridadId.toString())
        Text(
            modifier = Modifier.weight(3f),
            text = prioridad.descripcion,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Editar Prioridad")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar Prioridad")
        }
    }
    HorizontalDivider()
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    val prioridades = listOf(
        PrioridadEntity(
            prioridadId = 1,
            descripcion = "Alta",
        ),
        PrioridadEntity(
            prioridadId = 2,
            descripcion = "Alta",
        )
    )
    RegistroTecnicosTheme {
        PrioridadListScreen(
            prioridadList = prioridades,
            onEdit = {},
            onDelete = {}
            )
    }
}
