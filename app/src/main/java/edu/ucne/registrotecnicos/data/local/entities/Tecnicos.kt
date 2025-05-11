package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnicos")
data class TecnicoEntity (
    @PrimaryKey
    val tecnicoId: Int? =  0,
    val nombre: String = "",
    val sueldo: Double = 0.0
)