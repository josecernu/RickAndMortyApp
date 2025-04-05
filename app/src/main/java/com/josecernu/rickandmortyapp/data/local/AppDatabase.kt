package com.josecernu.rickandmortyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.josecernu.rickandmortyapp.data.dao.FavoriteDao
import com.josecernu.rickandmortyapp.data.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}
