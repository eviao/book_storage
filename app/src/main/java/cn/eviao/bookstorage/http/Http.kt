package cn.eviao.bookstorage.http

import io.reactivex.Observable


interface Http {
    fun fetch(isbn: String): Observable<Response>
}