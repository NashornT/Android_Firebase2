package com.example.password_saver.RestApi

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    fun getRetroClient(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return  Retrofit.Builder().baseUrl("https://www.breakingbadapi.com/api/").addConverterFactory(GsonConverterFactory.create(gson)).build()
    }
}