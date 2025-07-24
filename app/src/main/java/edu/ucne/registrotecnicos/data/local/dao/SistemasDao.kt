package edu.ucne.registrotecnicos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registrotecnicos.data.local.entities.SistemaEntity

@Dao
interface SistemasDao {
    @Upsert()
    suspend fun save(Sistema:List<SistemaEntity>)
    @Query("""
            SELECT *
                FROM Sistemas
                WHERE SistemaId =:id
                limit 1
    """)

    suspend fun find(id: Int): SistemaEntity?
    @Delete
    suspend fun delete(Sistema:SistemaEntity)
    @Query("SELECT * FROM Sistemas")
    suspend fun getAll(): List<SistemaEntity>
}