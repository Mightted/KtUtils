package com.hxs.ktutils.network

import androidx.collection.SimpleArrayMap
import com.hxs.ktutils.network.bean.Method
import com.hxs.ktutils.network.bean.RequestParams
import okhttp3.FormBody

class KtRequest {

    lateinit var requestMethod: Method
    lateinit var baseUrl: String
    lateinit var path: String
    val params = arrayListOf<RequestParams>()


    fun method(method: Method) = apply {
        requestMethod = method
    }

    fun url(url: String) = apply {
        baseUrl = url
    }

    fun path(path: String) = apply {
        this.path = path
    }

    fun addParameter(name: String, value: String) = apply {
        params.add(RequestParams(name, value))
//        params.put(name, value)
//        FormBody
    }

    fun addFileParameter(name: String, value: String, mediaType: String, path: String) = apply {
        params.add(RequestParams(name, value).extra(mediaType, path))
//        params.put(name, value)
//        FormBody
    }


    fun addParameters(paramList: List<RequestParams>) = apply {
        paramList.map {
            params.addAll(paramList)
        }
    }

}