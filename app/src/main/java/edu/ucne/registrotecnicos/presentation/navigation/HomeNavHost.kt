package edu.ucne.registrotecnicos.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadesViewModel
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicosViewModel
import edu.ucne.registrotecnicos.presentation.tickets.TicketsViewModel
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.presentation.Home.HomeScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen

@Composable
fun HomeNavHost(
    navController: NavHostController,
    tecnicoList: List<TecnicoEntity>,
    ticketList: List<TicketEntity>,
    prioridadList: List<PrioridadEntity>,
    tecnicosViewModel: TecnicosViewModel,
    ticketsViewModel: TicketsViewModel,
    prioridadesViewModel: PrioridadesViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home // <- Esto asume que tienes una pantalla principal o menú
    ) {
        composable<Screen.Home> {
            HomeScreen( // Esta pantalla deberías crearla
                onNavigateToTecnicos = { navController.navigate(Screen.TecnicoList) },
                onNavigateToTickets = { navController.navigate(Screen.TicketList) },
                onNavigateToPrioridades = { navController.navigate(Screen.PrioridadList) }
            )
        }

        // Rutas de Técnicos
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                onEdit = { id -> navController.navigate(Screen.Tecnico(id)) },
                onDelete = { tecnico -> tecnicosViewModel.deleteTecnico(tecnico) }
            )
        }
        composable<Screen.Tecnico> { backStack ->
            val id = backStack.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(id, tecnicosViewModel, navController) { }
        }

        // Rutas de Tickets
        composable<Screen.TicketList> {
            TicketListScreen(
                ticketList = ticketList,
                onEdit = { id -> navController.navigate(Screen.Ticket(id)) },
                onDelete = { ticket -> ticketsViewModel.deleteTicket(ticket) }
            )
        }
        composable<Screen.Ticket> { backStack ->
            val id = backStack.toRoute<Screen.Ticket>().ticketId
            TicketScreen(id, ticketsViewModel, navController) { }
        }

        // Rutas de Prioridades
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                prioridadList = prioridadList,
                onEdit = { id -> navController.navigate(Screen.Prioridad(id)) },
                onDelete = { prioridad -> prioridadesViewModel.deletePrioridad(prioridad) }
            )
        }
        composable<Screen.Prioridad> { backStack ->
            val id = backStack.toRoute<Screen.Prioridad>().prioridadId
            PrioridadScreen(id, prioridadesViewModel, navController)
        }
    }
}
