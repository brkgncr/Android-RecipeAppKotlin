package com.burak.cookbook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.burak.cookbook.model.Cook

@Dao
interface RecipeDAO {
    // Make SQL Queries

    @Query("SELECT * FROM Cook")
    fun getALL() : List <Cook>

    @Query("SELECT * FROM cook WHERE id = :id")
    fun findByUd(id: Int) : Cook

    @Insert
    fun insert(cook: Cook)

    @Delete
    fun delete(cook: Cook)
}