package cn.eviao.bookstorage.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cn.eviao.bookstorage.model.Book
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book): Single<Long>

    @Query("SELECT * FROM books WHERE id = :id")
    fun loadById(id: Int): Maybe<Book>

    @Query("SELECT * FROM books WHERE isbn = :isbn")
    fun loadByIsbn(isbn: String): Maybe<Book>

    @Query("SELECT COUNT(*) != 0 FROM books WHERE isbn = :isbn")
    fun loadExistsByIsbn(isbn: String): Single<Boolean>

    @Query("SELECT * FROM books ORDER BY id DESC")
    fun loadAll(): DataSource.Factory<Int, Book>
}