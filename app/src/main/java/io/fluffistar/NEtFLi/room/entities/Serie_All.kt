package io.fluffistar.NEtFLi.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Serie_All(
    @PrimaryKey val link: String,
    @ColumnInfo(name = "title") val title: String
)