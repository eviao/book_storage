package cn.eviao.bookstorage.http

class HttpException(message: String) : RuntimeException(message) {
    constructor(t: Throwable): this(t.message!!)
}