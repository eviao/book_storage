package cn.eviao.bookstorage.utils

object BookUtils {

    fun isValidISBN(isbn: String) = Regex("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$").matches(isbn)
}