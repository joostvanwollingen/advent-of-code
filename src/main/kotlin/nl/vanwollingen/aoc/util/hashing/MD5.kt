package nl.vanwollingen.aoc.util.hashing

import java.math.BigInteger
import java.security.MessageDigest

fun findLeadingZeroes(input: String, search: String, start: Long = 0L): Pair<Long, String> {
    var count = start
    var hash = ""
    while (!hash.startsWith(search)) {
        hash = md5("${input}${count}")
        count++
    }
    return count - 1 to hash
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}