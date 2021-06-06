package com.example.application

import android.app.Application
import com.example.api.DevAPI
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

class DevApplication : Application() {

    lateinit var devAPIService: DevAPI

    companion object {
        const val DEV_URL = "https://developerslife.ru"

        lateinit var instance: DevApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val mRetrofit : Retrofit = Retrofit.Builder()
            .baseUrl(DEV_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        devAPIService = mRetrofit.create()
    }

}