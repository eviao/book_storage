package cn.eviao.bookstorage.api.impl

import cn.eviao.bookstorage.api.Api
import cn.eviao.bookstorage.api.transformer.ErrorTransformer
import cn.eviao.bookstorage.api.transformer.RetryTransformer
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.utils.isValidImage
import io.reactivex.Observable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document


class DoubanCrawlImpl : Api {
    private val BASE_URL = "https://book.douban.com/isbn"

    private fun getDocument(isbn: String): Document {
        return Jsoup.connect("${BASE_URL}/${isbn}").get()
    }

    private fun getSubject(document: Document): String {
        return document.baseUri().split("/").filter { it.isNotBlank() }.last()
    }

    private fun parseTitle(document: Document): String {
        return document.select("h1 > span").text().trim()
    }

    private fun parseRating(document: Document): Double? {
        return document.select(".rating_wrap .rating_self strong").text()?.trim()?.toDoubleOrNull()
    }

    private fun parseImage(document: Document): String? {
        return document.select("#mainpic > a").attr("href")
    }

    private fun parseAuthors(document: Document): String? {
        return document
            .select("#info .pl")
            .filter { it.text().contains("作者") }
            .flatMap { it.nextElementSibling().select("a") }
            .map { it.text() }
            .ifEmpty { null }
            ?.reduce { t, u -> "${t} / ${u}" }
    }

    private fun parseAttrTag(document: Document, title: String): String? {
        return document
            .select("#info .pl")
            .filter { it.text().contains(title) }
            .map { it.nextSibling().outerHtml() }
            .map { it.trim() }
            .ifEmpty { null }
            ?.first()
    }

    private fun parseSubtitle(document: Document): String? = parseAttrTag(document, "副标题")

    private fun parseOriginTitle(document: Document): String? = parseAttrTag(document, "原作名")

    private fun parsePublisher(document: Document): String? = parseAttrTag(document, "出版社")

    private fun parsePubdate(document: Document): String? = parseAttrTag(document, "出版年")

    private fun parseSummary(document: Document): String? {
        return document.select(".related_info .intro p").text()?.trim()
    }

    private fun parseCatalog(document: Document, subject: String): String? {
        return document
            .select(".related_info div[id=dir_${subject}_full] br")
            .stream()
            .map { it.previousSibling().outerHtml() }
            .reduce { t, u -> "${t}\r\n ${u}" }
            .map { it.trim() }
            .orElse(null)
    }

    private fun parseTags(document: Document): String? {
        return document
            .select("#db-tags-section .tag")
            .map { it.text() }
            .filter { it?.isNotBlank() ?: false }
            .map { it.trim() }
            .ifEmpty { null }
            ?.reduce { t, u -> "${t};${u}" }
    }

    private fun parseContent(document: Document, isbn: String): Book {
        val subject = getSubject(document)

        val title = parseTitle(document)
        val subtitle = parseSubtitle(document)
        val originTitle = parseOriginTitle(document)

        val rating = parseRating(document)
        val image = parseImage(document)

        val authors = parseAuthors(document)
        val tags = parseTags(document)

        val publisher = parsePublisher(document)
        val pubdate = parsePubdate(document)

        val summary = parseSummary(document)
        val catalog = parseCatalog(document, subject)

        return Book(
            title = title,
            subtitle = subtitle,
            originTitle = originTitle,
            image = if (image != null && isValidImage(image)) image else null,
            isbn = isbn,
            pubdate = pubdate,
            rating = rating,
            summary = summary,
            catalog = catalog,
            publisher = publisher,
            authors = authors,
            tags = tags
        )
    }

    override fun fetch(isbn: String): Observable<Book> {
        return Observable
            .create<Document> {
                it.onNext(getDocument(isbn))
                it.onComplete()
            }
            .retryWhen(RetryTransformer(2, 2000))
            .onErrorResumeNext(ErrorTransformer())
            .map { parseContent(it, isbn) }
    }
}