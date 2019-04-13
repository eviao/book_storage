package cn.eviao.bookstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.eviao.bookstorage.data.AppDatabase
import cn.eviao.bookstorage.data.DaoException
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.model.Tag
import io.reactivex.Maybe
import java.util.ArrayList

class BookDetailViewModel(app: Application) : AndroidViewModel(app) {
    private val bookDao = AppDatabase.get(app).bookDao()
    private val tagDao = AppDatabase.get(app).tagDao()
    private val bookTagRefDao = AppDatabase.get(app).bookTagRefDao()

    val book = MutableLiveData<Book>()
    val tags = MutableLiveData<ArrayList<String>>()

    fun loadBook(isbn: String): Maybe<Book> {
        return bookDao.loadByIsbn(isbn)
            .map {
                book.postValue(it)
                it
            }
            .switchIfEmpty(Maybe.error(DaoException("图书信息不存在")))
    }

    fun loadTags(bookId: Long): Maybe<List<Tag>> {
        return tagDao.loadByBookId(bookId)
            .map {
                val data = it.map { it.text }
                tags.postValue(data as ArrayList<String>)
                it
            }
    }
}