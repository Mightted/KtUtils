package com.hxs.ktutils.network

import android.text.TextUtils
import com.hxs.ktutils.network.bean.Method
import com.hxs.ktutils.network.bean.RequestParams
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

import java.io.File

class KtRequest {

    var requestMethod: Method = Method.GET
    private var baseUrl: String = ""
    private var path: String = ""
    private val params = arrayListOf<RequestParams>()
    private var callback: ProgressCallback? = null


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
    }

    fun addFileParameter(mediaType: String, file: File, name: String = "", value: String = "") =
        apply {
            params.add(RequestParams(name, value).extra(mediaType, file))
        }

    /**
     * [callback] 进度监听器，可选参数，用于监听上传文件的进度
     */
    fun setOnProgressChangeListener(callback: ProgressCallback) = apply {
        this.callback = callback
    }

    fun addParameters(paramList: List<RequestParams>) = apply {
        paramList.map {
            params.addAll(paramList)
        }
    }

    private fun buildUrl(baseUrl: String, path: String): HttpUrl.Builder {
        return (baseUrl + path).toHttpUrl().newBuilder()
    }


    fun buildGet(): Request {
        val builder = buildUrl(baseUrl, path)
        params.map {
            builder.addQueryParameter(it.name, it.value)
        }
        return Request.Builder().url(builder.build()).build()
    }


    fun buildPost(): Request {
        val httpUrl = buildUrl(baseUrl, path).build()
        val requestBody = createFormBody(params)
        val countingBody = ProgressRequestWrapper(requestBody) { bytesWritten, contentLength ->
            val percentage = 100f * bytesWritten / contentLength
            callback?.let { it(percentage) }
        }
        return Request.Builder().url(httpUrl).post(countingBody).build()
    }


    /**
     * 构建post请求体
     * [params]为统一请求参数，包括了一般的键值对参数和文件上传参数
     */
    private fun createFormBody(params: List<RequestParams>): RequestBody {
        return MultipartBody.Builder().let { bodyBuilder ->
            bodyBuilder.setType(MultipartBody.FORM)
            val formBodyBuilder = FormBody.Builder()

            for (param in params) {
                // 非文件上传，只需要填入键值对
                if (!param.isFile) {
                    formBodyBuilder.add(param.name, param.value)
                } else {
                    // 文件上传参数
                    val requestBody =
                        param.file.asRequestBody(param.mediaType.toMediaTypeOrNull())
                    // 如果没有参数名或者文件名，直接添加文件即可
                    if (TextUtils.isEmpty(param.name) || TextUtils.isEmpty(param.value)) {
                        bodyBuilder.addPart(requestBody)
                    } else {
                        bodyBuilder.addFormDataPart(param.name, param.value, requestBody)
                    }
                }
            }
            bodyBuilder.addPart(formBodyBuilder.build())
            bodyBuilder.build()
        }
    }
}