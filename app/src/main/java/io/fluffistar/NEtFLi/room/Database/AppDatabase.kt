package io.fluffistar.NEtFLi.room.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.fluffistar.NEtFLi.room.Doa.DoaSerie_All
import io.fluffistar.NEtFLi.room.entities.Serie_All

@Database(entities = [Serie_All::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun SerieAllDao(): DoaSerie_All
}