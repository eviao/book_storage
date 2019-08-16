package cn.eviao.bookstorage.api

import cn.eviao.bookstorage.model.Book

data class Response(
    val book: Book,
    val tags: List<String> = emptyList()
)