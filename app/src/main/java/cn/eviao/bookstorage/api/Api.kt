package cn.eviao.bookstorage.api

import io.reactivex.Observable

interface Api {
    fun fetch(isbn: String): Observable<Response>
}