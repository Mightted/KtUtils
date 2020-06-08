package com.hxs.ktutils.network

import com.hxs.ktutils.network.adapter.KtJson
import com.hxs.ktutils.network.adapter.MoshiAdapter
import com.hxs.ktutils.network.bean.Method
import okhttp3.OkHttpClient
import java.io.IOException

typealias ProgressCallback = ((progress: Float) -> Unit)

object KtNetwork {

    private val ktClient: OkHttpClient = OkHttpClient()
    private var ktJson: KtJson = MoshiAdapter()

    fun setJsonAdapter(adapter: KtJson) = apply {
        ktJson = adapter
    }

    @JvmStatic
    fun request(ktRequest: KtRequest): String {
        return request(ktRequest, String::class.java) ?: ""
    }


    @JvmStatic
    fun <T> request(ktRequest: KtRequest, clazz: Class<T>): T? {
        return when (ktRequest.requestMethod) {
            Method.GET -> getRequest(ktRequest, clazz)
            Method.POST -> postRequest(ktRequest, clazz)
        }
    }

    @JvmStatic
    fun getRequest(ktRequest: KtRequest): String {
        return getRequest(ktRequest, String::class.java) ?: ""
    }

    @JvmStatic
    fun <T> getRequest(ktRequest: KtRequest, clazz: Class<T>): T? {
        ktRequest.run {
            val request = ktRequest.buildGet()
            ktClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw throw IOException("Unexpected code $response")

                println(response.body!!.string())
                return ktJson.fromJson(response.body!!.string(), clazz)
            }
        }
    }


    @JvmStatic
    fun postRequest(ktRequest: KtRequest): String {
        return postRequest(ktRequest, String::class.java) ?: ""
    }

    /**
     * post请求主方法
     * [ktRequest] 请求对象主体
     *
     */
    private fun <T> postRequest(ktRequest: KtRequest, clazz: Class<T>): T? {
        ktRequest.run {
            val request = ktRequest.buildPost()
            ktClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw throw IOException("Unexpected code $response")

                return ktJson.fromJson(response.body!!.string(), clazz)
            }
        }
    }
}

