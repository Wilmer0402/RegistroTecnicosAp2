package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.SistemasDto
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

    //Sistemas

    suspend fun getSistemas() = tecnicoingApi.getSistemas()

    suspend fun  updateSistemas(sistemasDto: SistemasDto) = tecnicoingApi.updateSistemas(sistemasDto)

    suspend fun  saveSistemas(sistemasDto: SistemasDto) = tecnicoingApi.saveSistemas(sistemasDto)

    suspend fun deleteSistemas(id: Int) = tecnicoingApi.deleteSistemas(id)

    suspend fun getSistemas(id:Int) = tecnicoingApi.getSistemas(id)


}