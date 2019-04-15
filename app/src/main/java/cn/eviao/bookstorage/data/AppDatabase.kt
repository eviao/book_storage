package cn.eviao.bookstorage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.eviao.bookstorage.data.dao.BookDao
import cn.eviao.bookstorage.data.dao.BookTagDao
import cn.eviao.bookstorage.data.dao.TagDao
import cn.eviao.bookstorage.model.*
import cn.eviao.bookstorage.service.BookService
import java.util.concurrent.Executors

private val DB_NAME: String = "book_storage.db"

@Database(
    entities = [
        Book::class,
        Tag::class,
        BookTag::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun tagDao(): TagDao
    abstract fun bookTagDao(): BookTagDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    fillData(context.applicationContext)
                }
            }).build()
        }

        private fun fillData(context: Context) {
            val bookService = BookService(context)

            Executors.newSingleThreadExecutor().execute {
                BOOK_DATA.map { isbn ->
                    bookService.pullBook(isbn)
                        .subscribe({
                            println("数据初始化完成..")
                        }, {
                            it.printStackTrace()
                        })
                }
            }
        }
    }
}

private val BOOK_DATA = arrayOf("9787111135104", "9787111544937")