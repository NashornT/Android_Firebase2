package com.example.password_saver.RestApi

import retrofit2.Call
import retrofit2.http.GET

interface Interface {
    @GET("quotes")
    fun getQuote(): Call<List<Model>>
}