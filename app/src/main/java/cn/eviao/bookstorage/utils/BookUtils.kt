package cn.eviao.bookstorage.utils

import java.util.regex.Pattern

object BookUtils {

    fun isValidIsbn(isbn: String): Boolean {
        val regex = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$"
        return Regex(regex).matches(isbn)
    }
}