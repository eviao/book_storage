package cn.eviao.bookstorage.persistence

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.eviao.bookstorage.model.Book

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book)

    @Query("SELECT * FROM books ORDER BY id DESC")
    fun findAll(): DataSource.Factory<Int, Book>
}