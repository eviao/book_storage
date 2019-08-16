package cn.eviao.bookstorage.api


class ApiException(message: String, cause: Throwable) : RuntimeException(message, cause)