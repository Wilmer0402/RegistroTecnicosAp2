package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

 data class PrioridadesViewModel @Inject constructor(
    private val prioridadRepository: PrioridadesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState: StateFlow<PrioridadUiState> = _uiState.asStateFlow()


    fun onEvent(event: PrioridadEvent) {
        when (event) {
            is PrioridadEvent.DescripcionChange -> onDescripcionChange(event.descripcion)
            is PrioridadEvent.PrioridadChange -> onPrioridadIdChange(event.prioridadId)
            PrioridadEvent.Save -> savePrioridad()
            PrioridadEvent.New -> nuevo()
            PrioridadEvent.Delete -> deletePrioridad()
        }
    }

    init {
        getPrioridades()
    }


    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getAll().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    fun findPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadRepository.find(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion ?: ""
                    )
                }
            }
        }
    }

    private fun savePrioridad() {
        viewModelScope.launch {
            if (_uiState.value.descripcion.isBlank()) {
                _uiState.update {
                    it.copy(errorMessage = "Campos vacíos")
                }
            } else {
                prioridadRepository.save(_uiState.value.toEntity())
                nuevo()
            }
        }
    }

    private fun deletePrioridad() {
        viewModelScope.launch {
            prioridadRepository.delete(_uiState.value.toEntity())
            nuevo()
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                errorMessage = null
            )
        }
    }
}

fun PrioridadUiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion ?: ""
)
