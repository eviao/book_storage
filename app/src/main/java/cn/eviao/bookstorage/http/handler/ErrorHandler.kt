package cn.eviao.bookstorage.http.handler

import cn.eviao.bookstorage.http.HttpException
import io.reactivex.Observable
import io.reactivex.functions.Function
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException


class ErrorHandler : Function<Throwable, Observable<Document>> {

    override fun apply(t: Throwable): Observable<Document> {
        return when(t) {
            is HttpStatusException -> {
                Observable.error(when(t.statusCode) {
                    404 -> HttpException("无法找到图书信息")
                    500 -> HttpException("服务接口发生错误")
                    else -> HttpException("服务请求异常")
                })
            }
            is SocketTimeoutException -> {
                Observable.error(HttpException("服务请求超时"))
            }
            else -> {
                Observable.error(HttpException(t))
            }
        }
    }
}