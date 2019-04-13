package cn.eviao.bookstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.eviao.bookstorage.http.Http
import cn.eviao.bookstorage.http.Response
import cn.eviao.bookstorage.http.impl.DoubanCrawlImpl
import cn.eviao.bookstorage.data.AppDatabase
import cn.eviao.bookstorage.model.BookTagRef
import cn.eviao.bookstorage.model.Tag
import cn.eviao.bookstorage.service.BookService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class BookAddViewModel(private val app: Application) : AndroidViewModel(app) {

    private val http = DoubanCrawlImpl()
    private val bookService = BookService(app)

    val response = MutableLiveData<Response>()

    fun fetchBook(isbn: String): Observable<Response> {
        return http.fetch(isbn)
            .map {
                response.postValue(it)
                it
            }
    }

    fun addBook(response: Response): Completable {
        return bookService.addBook(response)
    }
}