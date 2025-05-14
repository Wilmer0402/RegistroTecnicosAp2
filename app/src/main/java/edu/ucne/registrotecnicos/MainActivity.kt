package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()
        setContent {
            RegistroTecnicosTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TecnicoScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun TecnicoScreen() {
        var nombre: String by remember { mutableStateOf("") }
        var sueldo: Double by remember { mutableStateOf(0.0) }
        var errorMessage: String? by remember { mutableStateOf("") }
        var editando by remember { mutableStateOf<TecnicoEntity?>(null) }
        val scope = rememberCoroutineScope()

        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
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
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        OutlinedTextField(
                            value = "0",
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
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C27B0),
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color(0xFF9C27B0)
                            )
                        )

                        OutlinedTextField(
                            value = sueldo.toString(),
                            onValueChange = { newValue ->
                                sueldo = newValue.toDoubleOrNull() ?: 0.0
                            },
                            label = { Text("Sueldo del Técnico") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF9C27B0),
                                unfocusedTextColor = Color.Gray,
                                focusedLabelColor = Color(0xFF9C27B0)
                            )
                        )

                        Spacer(modifier = Modifier.padding(2.dp))
                        errorMessage?.let {
                            Text(text = it, color = Color.Red)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {},
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF9C27B0)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF9C27B0)),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "new button")
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

                                    scope.launch {
                                        saveTecnico(
                                            TecnicoEntity(
                                                tecnicoId = editando?.tecnicoId,
                                                nombre = nombre,
                                                sueldo = sueldo
                                            )
                                        )
                                        nombre = ""
                                        sueldo = 0.0
                                        errorMessage = null
                                        editando = null
                                    }
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF9C27B0)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF9C27B0)),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "save button"
                                )
                                Text(text = "Guardar")
                            }
                        }
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                val tecnicoList by tecnicoDb.TecnicoDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )

                TecnicoListScreen(
                    tecnicoList = tecnicoList,
                    onEdit = { tecnico ->
                        nombre = tecnico.nombre
                        sueldo = tecnico.sueldo
                        editando = tecnico
                    },
                    onDelete = { tecnico ->
                        scope.launch {
                            tecnicoDb.TecnicoDao().delete(tecnico)
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun TecnicoListScreen(
        tecnicoList: List<TecnicoEntity>,
        onEdit: (TecnicoEntity) -> Unit,
        onDelete: (TecnicoEntity) -> Unit
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lista de Técnicos",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF9C27B0),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico, onEdit, onDelete)
                }
            }
        }
    }

    @Composable
    private fun TecnicoRow(
        tecnico: TecnicoEntity,
        onEdit: (TecnicoEntity) -> Unit,
        onDelete: (TecnicoEntity) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = tecnico.tecnicoId.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.weight(3f),
                text = tecnico.nombre,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                modifier = Modifier.weight(2f),
                text = "$${"%.2f".format(tecnico.sueldo)}",
                style = MaterialTheme.typography.bodyLarge
            )

            Row {
                IconButton(onClick = { onEdit(tecnico) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }

                IconButton(onClick = { onDelete(tecnico) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }

    private suspend fun saveTecnico(tecnico: TecnicoEntity) {
        tecnicoDb.TecnicoDao().save(tecnico)
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        RegistroTecnicosTheme {
            TecnicoScreen()
        }
    }
}
