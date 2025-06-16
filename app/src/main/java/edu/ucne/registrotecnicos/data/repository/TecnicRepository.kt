package edu.ucne.registrotecnicos.data.repository
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.TecnicDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import  javax.inject.Inject

class TecnicRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){
    fun getTecnic(tecnicoId: Int) : Flow<Resource<List<TecnicDto>>> = flow{
        try{
            emit(Resource.Loading())
            val tecnico = remoteDataSource.getTecnic(tecnicoId)
            emit(Resource.Success(tecnico))
        } catch (e: HttpException){
            emit(Resource.Error("Error de Internet: ${e.message()}"))
        }catch (e: Exception){
            emit(Resource.Error("Error Desconocido: ${e.message}"))
        }
    }

    suspend fun saveTecnic(tecnicDto: TecnicDto) = remoteDataSource.saveTecnic(tecnicDto)

    suspend fun deleteTecnic(id: Int) = remoteDataSource.deleteTecnic(id)

    suspend fun editTecnic(tecnicDto: TecnicDto) = remoteDataSource.updateTecnic(tecnicDto)

    fun getTecnic(): Flow<Resource<List<TecnicDto>>> = flow{
        try{
            emit(Resource.Loading())
            val tecnico = remoteDataSource.getTecnic()
            emit(Resource.Success(tecnico))
        }catch (e: HttpException) {
            emit(Resource.Error("Error de Internet: ${e.message()}"))

        }catch (e: Exception){
            emit(Resource.Error("Error Desconocido: ${e.message}"))
        }
    }
}