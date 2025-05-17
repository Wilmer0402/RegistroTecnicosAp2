package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registrotecnicos.data.local.dao.PrioridadDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import junit.runner.Version
import  edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
@Database(
    entities = [
        TecnicoEntity::class,
    PrioridadEntity::class

    ],
    version = 2,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun TecnicoDao(): TecnicoDao
    abstract fun PrioridadDao(): PrioridadDao

}