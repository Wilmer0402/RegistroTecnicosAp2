package edu.ucne.registrotecnicos.presentation.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.TecnicDto
import edu.ucne.registrotecnicos.data.repository.TecnicRepository
import edu.ucne.registrotecnicos.presentation.UiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicViewModel @Inject constructor(
    private val tecnicRepository: TecnicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TecnicUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getTecnic()
    }

    fun onEvent(event: TecnicEvent) {
        when (event) {
            is TecnicEvent.TecnicIdChange -> tecnicoIdChange(event.tecnicoId)
            is TecnicEvent.NombreChange -> nombreChange(event.nombre)
            is TecnicEvent.SueldoChange -> sueldoChange(event.sueldo)
            is TecnicEvent.GetTecnicos -> findTecnic(event.id)
            TecnicEvent.PostTecnic -> addTecnic()
            TecnicEvent.Nuevo -> nuevo()
            TecnicEvent.GetTecnic -> getTecnic()
            TecnicEvent.LimpiarErrorNombre -> limpiarErrorNombre()
            TecnicEvent.LimpiarErrorSueldo -> limpiarErrorSueldo()
            TecnicEvent.ResetSuccessMessage -> _uiState.update { it.copy(isSuccess = false, successMessage = null) }
        }
    }

    private fun tecnicoIdChange(id: Int) {
        _uiState.update { it.copy(tecnicoId = id) }
    }

    private fun nombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre) }
    }

    private fun sueldoChange(sueldo: Double) {
        _uiState.update { it.copy(sueldo = sueldo) }
    }

    private fun limpiarErrorNombre() {
        _uiState.update { it.copy(errorNombre = "") }
    }

    private fun limpiarErrorSueldo() {
        _uiState.update { it.copy(errorSueldo = "") }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                tecnicoId = 0,
                nombre = "",
                sueldo = 0.0,
                errorNombre = "",
                errorSueldo = "",
                errorMessage = ""
            )
        }
    }

    private fun addTecnic() {
        viewModelScope.launch {
            var error = false

            if (_uiState.value.nombre.isBlank()) {
                _uiState.update { it.copy(errorNombre = "Este campo es obligatorio *") }
                error = true
            }
            if (_uiState.value.sueldo <= 0) {
                _uiState.update { it.copy(errorSueldo = "Debe ser mayor que cero *") }
                error = true
            }
            if (error) return@launch

            try {
                tecnicRepository.saveTecnic(_uiState.value.toEntity())
                _uiState.update {
                    it.copy(
                        isSuccess = true,
                        successMessage = "Técnico guardado correctamente",
                        errorMessage = null
                    )
                }
                getTecnic()
                nuevo()
                delay(2000)
                _uiEvent.emit(UiEvent.NavigateUp)
            } catch (e: retrofit2.HttpException) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error en la API: ${e.code()} - ${e.message()}",
                        isSuccess = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Error al guardar el técnico: ${e.localizedMessage}",
                        isSuccess = false
                    )
                }
            }
        }
    }

    private fun getTecnic() {
        viewModelScope.launch {
            tecnicRepository.getTecnic().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                tecnicos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun findTecnic(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                tecnicRepository.getTecnic(tecnicoId).collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val tecnico = resource.data?.firstOrNull()
                            _uiState.update {
                                it.copy(
                                    tecnicoId = tecnico?.tecnicoId ?: 0,
                                    nombre = tecnico?.nombre ?: "",
                                    sueldo = tecnico?.sueldo ?: 0.0
                                )
                            }
                        }
                        is Resource.Error -> _uiState.update {
                            it.copy(errorMessage = resource.message)
                        }
                        is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }
}

fun TecnicUiState.toEntity() = TecnicDto(
    tecnicoId = tecnicoId,
    nombre = nombre,
    sueldo = sueldo
)
