package com.hxs.ktutil.network.bean

import java.io.File

data class RequestParams(
    val name: String = "",
    val value: String = ""

) {
    lateinit var mediaType: String
    lateinit var file: File
    var isFile: Boolean = false
    fun extra(mediaType: String, file: File) = apply {
        if (file.exists()) {
            isFile = true
            this.mediaType = mediaType
            this.file = file
        }
    }
}