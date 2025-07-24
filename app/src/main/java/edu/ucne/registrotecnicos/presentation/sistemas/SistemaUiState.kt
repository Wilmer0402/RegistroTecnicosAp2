package edu.ucne.registrotecnicos.presentation.sistemas

import edu.ucne.registrotecnicos.data.remote.dto.SistemasDto


data class SistemaUiState(
    val sistemaId: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val costo: Double = 0.0,

    val errorMessage: String? = null,
    val errorNombre: String? = null,
    val errorDescripcion: String? = null,
    val errorCosto: String? = null,

    val successMessage: String? = null,
    val isSuccess: Boolean = false,
    val isLoading: Boolean = false,

    val sistemas: List<SistemasDto> = emptyList()
)