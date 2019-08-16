package cn.eviao.bookstorage.api.transformer

import cn.eviao.bookstorage.api.ApiException
import io.reactivex.Observable
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException
import io.reactivex.functions.Function


class ErrorTransformer() : Function<Throwable, Observable<Document>> {

    override fun apply(t: Throwable): Observable<Document> {
        val message = when (t) {
            is HttpStatusException -> when(t.statusCode) {
                404 -> "无法找到图书信息"
                500 -> "服务接口发生错误"
                else -> "服务请求异常"
            }
            is SocketTimeoutException -> "服务请求超时"
            else -> "服务请求失败"
        }
        return Observable.error(ApiException(message, t))
    }
}