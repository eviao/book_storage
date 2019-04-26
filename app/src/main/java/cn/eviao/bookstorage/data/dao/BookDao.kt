package cn.eviao.bookstorage.data.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.eviao.bookstorage.model.Book
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book): Single<Long>

    @Delete
    fun delete(book: Book): Completable

    @Query("SELECT * FROM books WHERE id = :id")
    fun loadById(id: Long): Maybe<Book>

    @Query("SELECT * FROM books WHERE isbn = :isbn")
    fun loadByIsbn(isbn: String): Maybe<Book>

    @Query("SELECT COUNT(*) != 0 FROM books WHERE isbn = :isbn")
    fun loadExistsByIsbn(isbn: String): Single<Boolean>

    @Query("SELECT COUNT(*) FROM books")
    fun count(): Flowable<Int>

    @Query("SELECT * FROM books ORDER BY id DESC")
    fun loadAll(): DataSource.Factory<Int, Book>
}