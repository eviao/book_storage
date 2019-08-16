package cn.eviao.bookstorage.model

data class Book(
    val id: Long? = null,

    val title: String? = null,
    val subtitle: String? = null,
    val originTitle: String? = null,

    val isbn: String? = null,
    val pubdate: String? = null,
    val image: String? = null,

    val rating: Double? = null,
    val catalog: String? = null,
    val summary: String? = null,

    val author: String? = null,
    val publisher: String? = null
) {

    fun isNew() = (id == null || id == 0L)
}