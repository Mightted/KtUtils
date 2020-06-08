package com.hxs.ktutils.network.adapter

interface KtJson {

    fun <T> toJson(objects: T, clazz: Class<T>): String

    fun <T> fromJson(json: String, clazz: Class<T>): T?

}