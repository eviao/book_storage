package cn.eviao.bookstorage.utils


fun isValidImage(url: String): Boolean {
    return !url.isBlank() && url.endsWith(".jpg") && !url.contains("book-default-lpic")
}

fun isValidISBN(isbn: String): Boolean {
    val pattern = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$"
    return Regex(pattern).matches(isbn)
}