package cn.eviao.bookstorage.persistence

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.eviao.bookstorage.model.Book
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BookDao {

    @Insert
    fun insert(book: Book): Single<Long>

    @Delete
    fun delete(book: Book): Completable

    @Query("select * from books where id = :id")
    fun loadBy(id: Long): Maybe<Book>

    @Query("select * from books where isbn = :isbn")
    fun loadBy(isbn: String): Maybe<Book>

    @Query("select count(*) != 0 from books where isbn = :isbn")
    fun loadExistsBy(isbn: String): Single<Boolean>

    @Query("select count(*) from books")
    fun count(): Single<Int>

    @Query("select count(*) from books where isbn = :isbn")
    fun countBy(isbn: String): Single<Int>

    @Query("select * from books where title like :title order by id desc")
    fun loadPage(title: String): DataSource.Factory<Int, Book>

    @Query("select * from books order by id desc")
    fun loadPage(): DataSource.Factory<Int, Book>
}