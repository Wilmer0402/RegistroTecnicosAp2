package edu.ucne.registrotecnicos.data.remote.dto

data class SistemasDto (
    val sistemaId: Int? = null,
    val nombre: String,
    val descripcion: String,
    val costo: Double
)