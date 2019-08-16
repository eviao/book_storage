package cn.eviao.bookstorage.api.transformer

import io.reactivex.Observable
import io.reactivex.functions.Function
import org.jsoup.HttpStatusException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class RetryTransformer(val max: Int, val delay: Long) : Function<Observable<out Throwable>, Observable<*>> {
    private var count: Int = 0

    private fun isTimeout(e: Throwable) = when (e) {
            is HttpStatusException -> {
                e.statusCode == 408 || e.statusCode == 504
            }
            is SocketTimeoutException -> true
            else -> false
        }

    override fun apply(attempts: Observable<out Throwable>): Observable<*> {
        return attempts.flatMap { e ->
            if (isTimeout(e) && (count++ < max)) {
                Observable.timer(delay, TimeUnit.MILLISECONDS)
            } else {
                Observable.error<Throwable>(e)
            }
        }
    }
}