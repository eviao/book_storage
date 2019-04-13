package cn.eviao.bookstorage.http.impl

import cn.eviao.bookstorage.http.Http
import cn.eviao.bookstorage.http.Response
import io.reactivex.Observable

class DoubanAPIImpl : Http {
    private val BASE_URL = "https://api.douban.com/v2/book/isbn"

    override fun fetch(isbn: String): Observable<Response> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}