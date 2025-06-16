package edu.ucne.registrotecnicos.presentation.tecnico

import edu.ucne.registrotecnicos.data.remote.dto.TecnicDto

data class TecnicUiState (
    val tecnicoId: Int = 0,
    val nombre: String = "",
    val sueldo: Double = 0.0,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSuccess : Boolean = false,
    val errorNombre: String? = null,
    val errorSueldo: String? = null,
    val successMessage: String? = null,
    val tecnicos: List<TecnicDto> = emptyList()
)
