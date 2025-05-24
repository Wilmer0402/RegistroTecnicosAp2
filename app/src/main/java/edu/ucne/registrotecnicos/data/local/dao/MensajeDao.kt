package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MensajeDao{
    @Upsert()
    suspend fun save(Mensaje: MensajeEntity)
    @Query("""
            SELECT *
                FROM Mensajes
                WHERE mensajeId =:id
                limit 1
    """)
    suspend fun find(id: Int): MensajeEntity?
    @Delete
    suspend fun delete(Mensaje: MensajeEntity)
    @Query("SELECT * FROM Mensajes Where ticketId =:ticketId")
    fun getAll(ticketId: Int): Flow<List<MensajeEntity>>
}

