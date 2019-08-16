package cn.eviao.bookstorage.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.eviao.bookstorage.model.Book

@Database(entities = arrayOf(Book::class), version = 1, exportSchema = false)
abstract class DataSource : RoomDatabase() {

    abstract fun bookDao(): BookDao


    companion object {
        const val DB_NAME = "book_storage.db"

        @Volatile private var INSTANCE: DataSource? = null

        fun getInstance(context: Context): DataSource =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                DataSource::class.java, DB_NAME
            ).build()
    }
}