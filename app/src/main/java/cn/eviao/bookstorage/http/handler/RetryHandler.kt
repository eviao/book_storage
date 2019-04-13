package cn.eviao.bookstorage.http.handler

import io.reactivex.Observable
import io.reactivex.functions.Function
import org.jsoup.HttpStatusException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class RetryHandler(val max: Int, val delay: Long) : Function<Observable<out Throwable>, Observable<*>> {
    private var count: Int = 0

    override fun apply(attempts: Observable<out Throwable>): Observable<*> {
        return attempts.flatMap { e ->
            if (((e is HttpStatusException && e.statusCode == 500) || e is SocketTimeoutException) && (count++ < max)) {
                Observable.timer(delay, TimeUnit.MILLISECONDS)
            } else {
                Observable.error<Throwable>(e)
            }
        }
    }
}