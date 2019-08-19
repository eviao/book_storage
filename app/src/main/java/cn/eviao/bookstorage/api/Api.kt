package cn.eviao.bookstorage.api

import cn.eviao.bookstorage.model.Book
import io.reactivex.Observable

interface Api {
    fun fetch(isbn: String): Observable<Book>
}