package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName= "Sistemas")
data class SistemaEntity(
    @PrimaryKey
    val sistemaId: Int? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val costo: Double = 0.0

)