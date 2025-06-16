package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.TecnicDto
import javax.inject.Inject



class RemoteDataSource @Inject constructor(
    private val tecnicoingApi: TecnicoingApi
){
    suspend fun getTecnic() = tecnicoingApi.getTecnic()

    suspend fun updateTecnic(tecnicosDto: TecnicDto) = tecnicoingApi.updateTecnic(tecnicosDto)

    suspend fun saveTecnic(tecnicosDto: TecnicDto) = tecnicoingApi.saveTecnic(tecnicosDto)

    suspend fun deleteTecnic(id: Int) = tecnicoingApi.deleteTecnic(id)

    suspend fun getTecnic(id: Int) = tecnicoingApi.getTecnic(id)
}