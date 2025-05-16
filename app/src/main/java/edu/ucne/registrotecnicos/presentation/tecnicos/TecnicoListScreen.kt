package edu.ucne.registrotecnicos.presentation.tecnicos

import android.graphics.drawable.Icon
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
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity>,
    onEdit: (Int?) -> Unit,
    onDelete: (TecnicoEntity) -> Unit
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
                            text = "Lista de Técnicos",
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
                Icon(Icons.Filled.Add, "Agregar Nuevo")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico, { onEdit(tecnico.tecnicoId) }, { onDelete(tecnico) })
                }
            }
        }
    }
}

@Composable
private fun TecnicoRow(
    tecnico: TecnicoEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = tecnico.tecnicoId.toString())
        Text(
            modifier = Modifier.weight(2f),
            text = tecnico.nombre,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(modifier = Modifier.weight(2f), text = tecnico.sueldo.toString())
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Editar")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
    HorizontalDivider()
}

@Preview
@Composable
private fun Preview() {
    val tecnicos = listOf(
        TecnicoEntity(
            tecnicoId = 1,
            nombre = "Juan",
            sueldo = 100.0
        ),
        TecnicoEntity(
            tecnicoId = 2,
            nombre = "José Francisco Rodríguez López",
            sueldo = 200.0
        )
    )
    RegistroTecnicosTheme {
        TecnicoListScreen(
            tecnicoList = tecnicos,
            onEdit = {},
            onDelete = {}
        )
    }
}
