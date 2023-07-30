package io.mcnulty.avwx.utils

fun String.addSpacePrefix(): String {
    return if (this.isNotEmpty()) " $this" else ""
}