package com.hxs.ktutils.network.bean

data class RequestParams(
    val name: String = "",
    val value: String = ""

) {
    lateinit var mediaType: String
    lateinit var filePath: String
    var isFile: Boolean = false
    fun extra(mediaType: String, path: String) = apply {
        isFile = true
        this.mediaType = mediaType
        filePath = path

    }
}