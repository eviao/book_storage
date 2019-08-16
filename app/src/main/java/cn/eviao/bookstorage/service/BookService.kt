package cn.eviao.bookstorage.service

import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.persistence.BookDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.Single

class BookService(private val dataSource: DataSource) {

    private val bookDao: BookDao = dataSource.bookDao()

    private val pagingConfig = Config(
        pageSize = 30,
        enablePlaceholders = true,
        maxSize = 300
    )

    fun add(book: Book): Single<Long> {
        return bookDao.insert(book)
    }

    fun count() = bookDao.count()

    fun loadAll(title: String) = bookDao
        .loadAll("%$title%")
        .toLiveData(pagingConfig)

    fun loadAll() = bookDao
        .loadAll()
        .toLiveData(pagingConfig)
}