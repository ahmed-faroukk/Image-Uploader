package com.example.retrofitimage.data

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface {
    @Multipart
    @POST("img/")
     suspend fun UploadPhoto(
         @Part photo: MultipartBody.Part
    ): retrofit2.Response<Image>

    companion object {
        operator fun invoke(): ApiInterface {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://sign-login-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiInterface::class.java)
        }
    }
}