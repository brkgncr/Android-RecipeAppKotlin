package com.burak.cookbook.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.burak.cookbook.model.Cook

// Make SQL Creation
@Database(entities = [Cook::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun userDao(): RecipeDAO
}