package com.hxs.ktutils.network.adapter

import com.squareup.moshi.Moshi

class MoshiAdapter : KtJson {

    companion object {
        val moshi: Moshi = Moshi.Builder().build()
    }

    override fun <T> toJson(obj: T, clazz: Class<T>): String {
        return moshi.adapter(clazz).toJson(obj)
    }

    override fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return moshi.adapter(clazz).fromJson(json)
    }


}