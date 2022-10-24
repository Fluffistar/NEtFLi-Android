package io.fluffistar.NEtFLi.room.Doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.fluffistar.NEtFLi.room.entities.Serie_All

@Dao
interface  DoaSerie_All {
    @Query("SELECT * FROM Serie_All")
    fun getAll(): List<Serie_All>



    @Query("SELECT * FROM Serie_All WHERE title LIKE :title ")
    fun findByName(title: String ) : List<Serie_All>

    @Insert
    fun insertAll(vararg serieAll: Serie_All)

    @Delete
    fun delete(serieAll: Serie_All)
}