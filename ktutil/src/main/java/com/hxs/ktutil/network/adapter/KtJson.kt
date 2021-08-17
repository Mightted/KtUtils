package com.hxs.ktutil.network.adapter

interface KtJson {

    fun <T> toJson(objects: T, clazz: Class<T>): String

    fun <T:Any> fromJson(json: String, clazz: Class<T>): Any

}