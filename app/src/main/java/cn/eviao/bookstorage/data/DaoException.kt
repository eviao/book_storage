package cn.eviao.bookstorage.data

open class DaoException(message: String)  : RuntimeException(message) {
    constructor(t: Throwable): this(t.message!!)
}