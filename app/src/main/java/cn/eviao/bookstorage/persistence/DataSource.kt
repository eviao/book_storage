package cn.eviao.bookstorage.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.eviao.bookstorage.App
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.model.Box

@Database(entities = arrayOf(
    Book::class,
    Box::class
), version = 1, exportSchema = false)
abstract class DataSource : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun boxDao(): BoxDao

    companion object {
        const val DB_NAME = "book_storage.db"

        @Volatile private var INSTANCE: DataSource? = null

        fun getInstance(): DataSource =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase().also { INSTANCE = it }
            }

        private fun buildDatabase() = Room.databaseBuilder(App.getContext(),
            DataSource::class.java, DB_NAME).build()
    }
}