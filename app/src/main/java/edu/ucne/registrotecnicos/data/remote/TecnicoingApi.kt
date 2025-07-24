package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.local.dao.SistemasDao
import edu.ucne.registrotecnicos.data.remote.dto.SistemasDto
import edu.ucne.registrotecnicos.data.remote.dto.TecnicDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface TecnicoingApi {
    @GET("api/Tecnicos")
    suspend fun getTecnic(): List<TecnicDto>

    @GET("api/Tecnicos/{id}")
    suspend fun getTecnic(@Path("id") id: Int): List<TecnicDto>

    @PUT("api/Tecnicos/{id}")
    suspend fun updateTecnic(@Body tecnicosDto: TecnicDto): TecnicDto

    @POST("api/Tecnicos")
    suspend fun saveTecnic(@Body tecnicosDto: TecnicDto): TecnicDto

    @DELETE("api/Tecnicos/{id}")
    suspend fun deleteTecnic(@Path("id") id: Int): Response<Unit>

    //Sistemas

    @GET("api/Sistemas")
    suspend fun getSistemas(): List<SistemasDto>

    @GET("api/Sistemas/{id}")
    suspend fun getSistemas(@Path("id") id: Int): List<SistemasDto>

    @PUT("api/Sistemas/{id}")
    suspend fun updateSistemas(@Body sistemasDto: SistemasDto): SistemasDto

    @POST("api/Sistemas")
    suspend fun saveSistemas(@Body sistemasDto: SistemasDto): SistemasDto

    @DELETE("api/Sistemas/{id}")
    suspend fun deleteSistemas(@Path("id") id: Int): Response<Unit>
}

