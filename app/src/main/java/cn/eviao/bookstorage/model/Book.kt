package cn.eviao.bookstorage.model

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "books")
data class Book(
    @Column(name = "title", index = true) val title: String,
    @Column(name = "subtitle") val subtitle: String? = null,
    @Column(name = "originTitle") val originTitle: String? = null,

    @Column(name = "image") val image: String? = null,

    @Column(name = "isbn") val isbn: String,
    @Column(name = "pubdate") val pubdate: String? = null,

    @Column(name = "rating") val rating: Double? = null,
    @Column(name = "catalog") val catalog: String? = null,
    @Column(name = "summary") val summary: String? = null,

    @Column(name = "publisher") val publisher: Publisher? = null
) : Model()

@Table(name = "books_tags_relation")
data class BookTag(
    @Column(name = "book") val book: Book,
    @Column(name = "tag") val tag: Tag
) : Model()

@Table(name = "books_authors_relation")
data class BookAuthor(
    @Column(name = "book") val book: Book,
    @Column(name = "author") val author: Author
) : Model()
