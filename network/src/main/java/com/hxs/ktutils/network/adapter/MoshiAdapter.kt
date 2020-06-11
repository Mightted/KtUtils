package com.hxs.ktutils.network.adapter

import com.squareup.moshi.Moshi

class MoshiAdapter : KtJson {

    companion object {
        val moshi: Moshi = Moshi.Builder().build()
    }

    override fun <T> toJson(objects: T, clazz: Class<T>): String {
        return moshi.adapter(clazz).toJson(objects)
    }

    override fun <T:Any> fromJson(json: String, clazz: Class<T>): Any {
        return moshi.adapter(clazz).fromJson(json) ?: json
    }
}