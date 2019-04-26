package cn.eviao.bookstorage.data.dao

open class DaoException(message: String)  : RuntimeException(message) {
    constructor(t: Throwable): this(t.message!!)
}