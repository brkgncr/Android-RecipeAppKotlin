package com.burak.cookbook.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.burak.cookbook.model.Recipe
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface RecipeDAO {
    // Make SQL Queries
    @Query("SELECT * FROM Recipe")
    fun getALL() : Flowable<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun findById(id: Int) : Flowable<Recipe>

    @Insert
    fun insert(recipe: Recipe) : Completable

    @Delete
    fun delete(recipe: Recipe) : Completable
}