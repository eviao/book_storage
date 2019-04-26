package cn.eviao.bookstorage.http

class HttpException(val code: Int, message: String) : RuntimeException(message) {
    constructor(message: String): this(-1, message)
    constructor(t: Throwable): this(t.message!!)
}