package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.SistemasDao
import edu.ucne.registrotecnicos.data.local.entities.SistemaEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.SistemasDto
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException


class SistemasRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sistemasDao: SistemasDao

){
    fun getSistema(id: Int): Flow<Resource<SistemasDto>> {
        return flow {
            try {
                emit(Resource.Loading())
                val vehiculos = remoteDataSource.getSistemas(id)
                if (vehiculos.isNotEmpty()) {
                    emit(Resource.Success(vehiculos.first()))
                } else {
                    emit(Resource.Error("No se encontró el vehículo"))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error: ${e.localizedMessage ?: "Error desconocido"}"))
            }
        }
    }

    fun getSistemas(): Flow<Resource<List<SistemasDto>>> = flow {
        var listSistemasDto: List<SistemaEntity> = emptyList()
        try {
            emit(Resource.Loading())
            val sistemas = remoteDataSource.getSistemas()
            val sistemasEntity = sistemas.map {
                it.toEntity()
            }
            sistemasDao.save(sistemasEntity)
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {
        }
        listSistemasDto = sistemasDao.getAll()
        val listaSistemaDto = listSistemasDto.map {
            it.toDto()
        }

        emit(Resource.Success(listaSistemaDto))
    }

    private fun SistemasDto.toEntity() = SistemaEntity(
        sistemaId = this.sistemaId,
        nombre = this.nombre ?: "",
        descripcion = this.descripcion ?: "",
        costo = this.costo ?: 0.0
    )

    private fun SistemaEntity.toDto() = SistemasDto(
        sistemaId = this.sistemaId,
        nombre = this.nombre ?: "",
        descripcion = this.descripcion ?: "",
        costo = this.costo ?: 0.0
    )


    suspend fun saveSistema(sistemasDto: SistemasDto) = remoteDataSource.saveSistemas(sistemasDto)

    suspend fun editSistema(sistemasDto: SistemasDto) = remoteDataSource.updateSistemas(sistemasDto)

    suspend fun deleteSistema(idSistema: Int) = remoteDataSource.deleteSistemas(idSistema)
}
