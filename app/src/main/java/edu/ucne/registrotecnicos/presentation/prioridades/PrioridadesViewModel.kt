package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.repository.PrioridadesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PrioridadesViewModel (
    private val prioridadRepository: PrioridadesRepository
): ViewModel() {
    fun savePrioridad(prioridad: PrioridadEntity) {
        viewModelScope.launch {
            prioridadRepository.save(prioridad)
        }
    }

    suspend fun findPrioridad(id: Int): PrioridadEntity? {
        return prioridadRepository.find(id)
    }

    fun deletePrioridad(prioridad: PrioridadEntity) {
        viewModelScope.launch {
            prioridadRepository.delete(prioridad)
        }
    }

}