package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import edu.ucne.registrotecnicos.presentation.navigation.PrioridadesNavHost
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

//class MainActivity : ComponentActivity() {
//    private lateinit var tecnicoDb: TecnicoDb
//    private lateinit var tecnicosRepository: TecnicosRepository
//    private lateinit var tecnicosViewModel: TecnicosViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//
//        tecnicoDb = Room.databaseBuilder(
//            applicationContext,
//            TecnicoDb::class.java,
//            "Tecnico.db"
//        ).fallbackToDestructiveMigration()
//            .build()
//
//        tecnicosRepository = TecnicosRepository(tecnicoDb.TecnicoDao())
//
//        tecnicosViewModel = TecnicosViewModel(tecnicosRepository)
//
//        setContent {
//            val lifecycleOwner = LocalLifecycleOwner.current
//            val tecnicoList by tecnicoDb.TecnicoDao().getAll()
//                .collectAsStateWithLifecycle(
//                    initialValue = emptyList(),
//                    lifecycleOwner = lifecycleOwner,
//                    minActiveState = Lifecycle.State.STARTED
//                )
//
//            RegistroTecnicosTheme {
//                val nav = rememberNavController()
//                TecnicosNavHost(
//                    nav,
//                    tecnicoList,
//                    tecnicosViewModel,
//                    nav
//                )
//            }
//        }
//    }
//
//
//
//}

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    private lateinit var prioridadesRepository: PrioridadesRepository
    private lateinit var prioridadesViewModel: PrioridadesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()

        prioridadesRepository = PrioridadesRepository(tecnicoDb.PrioridadDao())

        prioridadesViewModel = PrioridadesViewModel(prioridadesRepository)

        setContent {
            val lifecycleOwner = LocalLifecycleOwner.current
            val prioridadList by tecnicoDb.PrioridadDao().getAll()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = lifecycleOwner,
                    minActiveState = Lifecycle.State.STARTED
                )

            RegistroTecnicosTheme {
                val nav = rememberNavController()
                PrioridadesNavHost(
                    nav,
                    prioridadList,
                    prioridadesViewModel,
                    nav
                )
            }
        }
    }



}
