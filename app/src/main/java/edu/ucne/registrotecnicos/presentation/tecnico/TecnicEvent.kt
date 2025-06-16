package edu.ucne.registrotecnicos.presentation.tecnico

sealed interface TecnicEvent {
    data class TecnicIdChange(val tecnicoId: Int): TecnicEvent
    data class NombreChange(val nombre: String): TecnicEvent
    data class SueldoChange(val sueldo: Double): TecnicEvent

    data object PostTecnic: TecnicEvent
    data object GetTecnic: TecnicEvent
    data object Nuevo: TecnicEvent
    data object LimpiarErrorNombre: TecnicEvent
    data object LimpiarErrorSueldo: TecnicEvent
    data class GetTecnicos(val id : Int): TecnicEvent
    data object ResetSuccessMessage: TecnicEvent
}