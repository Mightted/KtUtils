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
    fun request(ktRequest: KtRequest): Any {
        return request(ktRequest, String::class.java)
    }


    @JvmStatic
    fun <T> request(ktRequest: KtRequest, clazz: Class<T>): Any {
        return when (ktRequest.requestMethod) {
            Method.GET -> getRequest(ktRequest, clazz)
            Method.POST -> postRequest(ktRequest, clazz)
        }
    }

    @JvmStatic
    fun getRequest(ktRequest: KtRequest): Any {
        return getRequest(ktRequest, String::class.java)
    }

    @JvmStatic
    fun <T> getRequest(ktRequest: KtRequest, clazz: Class<T>): Any {
        ktRequest.run {
            val request = ktRequest.buildGet()
            ktClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw throw IOException("Unexpected code $response")

                return if (clazz.classes != String::class.java) {
                    ktJson.fromJson(response.body!!.string(), clazz) ?: response.body!!.string()
                } else {
                    response.body!!.string()
                }
            }
        }
    }


    @JvmStatic
    fun postRequest(ktRequest: KtRequest): Any {
        return postRequest(ktRequest, String::class.java)
    }

    /**
     * post请求主方法
     * [ktRequest] 请求对象主体
     *
     */
    private fun <T> postRequest(ktRequest: KtRequest, clazz: Class<T>): Any {
        ktRequest.run {
            val request = ktRequest.buildPost()
            ktClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw throw IOException("Unexpected code $response")

                return if (clazz != String::class.java) {
                    ktJson.fromJson(response.body!!.string(), clazz) ?: response.body!!.string()
                } else {
                    response.body!!.string()
                }
            }
        }
    }
}

