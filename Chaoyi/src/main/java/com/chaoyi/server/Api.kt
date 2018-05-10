package com.chaoyi.server

import com.chaoyi.model.PicData
import com.chaoyi.model.Pics
import com.chaoyi.model.picsReuestData
import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


/**
 * Created by yupenglei on 17/8/9.
 */
interface Api {
    companion object {
        val rxInstance: Api by lazy {
            ApiBuilder().addLogging(HttpLoggingInterceptor.Level.BODY).buildCommonApi()
        }
    }

    /**
     * 用于构建api
     */
    class ApiBuilder {
        companion object {
            //网络请求添加header和随机数
            private val requestInterceptor = Interceptor { chain ->
                val request = chain.request()
                val newRequest = request.newBuilder()
                        .addHeader("Accept-Encoding", "application/json")
                        .build()
                chain.proceed(newRequest)
            }

            private const val host = "http://api.tide.moreless.io/"
        }

        private val builder = Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        private val client = OkHttpClient.Builder().addInterceptor(requestInterceptor)

        fun addLogging(logLevel: HttpLoggingInterceptor.Level): ApiBuilder = apply {
            client.addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
        }

        fun buildCommonApi(): Api {
            return build(host, Api::class.java)
        }

        private fun <T> build(url: String, clazz: Class<T>): T =
                builder.baseUrl(url)
                        .client(client.build())
                        .build().create(clazz)
    }

    @Headers("Content-Type: application/json; charset=UTF-8")
    @POST("pics")
    fun getPics(@Body info: RequestBody = RequestBody.create(
            MediaType.parse("Content-Type: application/json; charset=UTF-8")
            , Gson().toJson(picsReuestData())))
            : Observable<String>

}
