package com.burak.cookbook.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe (
    // Make SQL Column name and types

    @ColumnInfo(name = "name")
    var name : String,

    @ColumnInfo(name = "material")
    var material : String,

    @ColumnInfo(name = "recipe")
    var recipe : String,

    @ColumnInfo(name = "image")
    var image : ByteArray

) {
    // Make ID column and auto update

    @PrimaryKey(autoGenerate = true)
    var id = 0
}
