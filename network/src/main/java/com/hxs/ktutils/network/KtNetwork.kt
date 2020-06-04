package com.hxs.ktutils.network

import com.hxs.ktutils.network.bean.Method
import com.hxs.ktutils.network.bean.RequestParams
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object KtNetwork {

//    private lateinit var requestMethod: Method
//    private lateinit var baseUrl: String
//    private lateinit var path: String
//    private val params = SimpleArrayMap<String, String>()

    private val ktClient: OkHttpClient = OkHttpClient()


//    fun method(method: Method) = apply {
//        requestMethod = method
//    }


//    fun onGet() = apply {
//        val request = Request.Builder()
//
//    }


    fun request(ktRequest: KtRequest) {
        when (ktRequest.requestMethod) {
            Method.GET -> getRequest(ktRequest)
            Method.POST -> postRequest(ktRequest)
        }
    }

    @JvmStatic
    fun getRequest(ktRequest: KtRequest) {
        ktRequest.run {
            val builder = (baseUrl + path).toHttpUrlOrNull()?.newBuilder() ?: return
            params.map {
                builder.addQueryParameter(it.name, it.value)
            }

            val request = Request.Builder().url(builder.build()).build()
            ktClient.newCall(request).execute()


        }

    }

    @JvmStatic
    fun postRequest(ktRequest: KtRequest) {
        ktRequest.run {
            val builder = (baseUrl + path).toHttpUrlOrNull()?.newBuilder() ?: return
//            val requestBody = FormBody.Builder().let { bodyBuilder ->
//                addPostParams(bodyBuilder, params)
////                params.map {
////                    bodyBuilder.add(it.key, it.value)
////                }
//                bodyBuilder.build()
//            }
//            MultipartBody.FORM
            val requestBody = createFormBody(params)


            val countingBody = ProgressRequestWrapper(requestBody) { bytesWritten, contentLength ->
                val percentage = 100f * bytesWritten / contentLength
            }


            val request =
                Request.Builder().url(builder.build()).post(countingBody).build()
            ktClient.newCall(request).execute()


        }
    }

    private fun createFormBody(params: List<RequestParams>): RequestBody {
        return MultipartBody.Builder().let { bodyBuilder ->
            bodyBuilder.setType(MultipartBody.FORM)
            val formBodyBuilder = FormBody.Builder()

            for (param in params) {
                if (!param.isFile) {
                    formBodyBuilder.add(param.name, param.value)
                } else {
                    val requestBody =
                        File(param.filePath).asRequestBody(param.mediaType.toMediaTypeOrNull())
                    bodyBuilder.addFormDataPart(param.name, param.value, requestBody)
                }
            }
            bodyBuilder.addPart(formBodyBuilder.build())
            bodyBuilder.build()
        }
    }


//    fun fileUpload(ktRequest: KtRequest) {
//
//        ktRequest.run {
//            val builder = (baseUrl + path).toHttpUrlOrNull()?.newBuilder() ?: return
//            val requestBody = MultipartBody.Builder().let { bodyBuilder ->
//                if (params.size != 0) {
//                    bodyBuilder.addPart(createFormBody(params))
//                }
//                bodyBuilder.addFormDataPart
////                params.map {
////                    bodyBuilder
////                    bodyBuilder.add(it.key, it.value)
////                }
//                bodyBuilder.build()
//            }
//            MultipartBody.FORM
//
//            val request = Request.Builder().url(builder.build()).post(requestBody).build()
//
//            ktClient.newCall(request).execute()
//
//
//        }
//    }


}

