package com.example.password_saver.RestApi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.password_saver.R
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.gson.Gson
import io.grpc.InternalChannelz.id
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

const val BASE_URL ="https://jsonplaceholder.typicode.com/"

class RestActivity : AppCompatActivity() {

    private var _interstitialAd: InterstitialAd? = null
    private var _bannerAd: AdView? = null
    private var _rewardAd: RewardedAd? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest)
        getMyData()
        getQuote()

        MobileAds.initialize(this)
        loadInterstitial()
        loadRewardAd()
        loadBannerAd()

    }

    private fun loadRewardAd() {
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                _rewardAd = null
            }

            override fun onAdLoaded(p0: RewardedAd) {
                _rewardAd = p0
            }
        })
    }

    private fun loadInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                _interstitialAd = null;
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                _interstitialAd = interstitialAd
            }
        })
    }

    private fun loadBannerAd() {
        _bannerAd = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        _bannerAd?.loadAd(adRequest)
    }

    fun showInterstitial(view: View) {
        _interstitialAd?.show(this)
    }

    fun showReward(view: View) {
        _rewardAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                _rewardAd = null
                loadRewardAd()
            }
        }

        _rewardAd?.show(this, OnUserEarnedRewardListener { rewardItem ->
            var rewardAmount = rewardItem.amount
            var rewardType = rewardItem.type
            println("$rewardAmount $rewardType Awarded")
        })
    }

    private fun getQuote() {
        val retro = Retro().getRetroClient().create(Interface::class.java)
        retro.getQuote().enqueue(object : Callback<List<Model>>{
            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Log.d("Fail","We Fail")
            }

            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                for (q in response.body()!!){
                    Log.e("WOW",q.quote.toString())
                }
            }
        })
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build().create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<List<MyDataItem>?> {
            override fun onResponse(
                call: Call<List<MyDataItem>?>,
                response: Response<List<MyDataItem>?>
            ) {
                val responseBody = response.body()!!

                val myStringBuilder = StringBuilder()
                for (myData in responseBody){
                    myStringBuilder.append(myData.body)
                    myStringBuilder.append("\n")

                }

                findViewById<TextView>(R.id.txt_rest).setText(myStringBuilder)
            }

            override fun onFailure(call: Call<List<MyDataItem>?>, t: Throwable) {
                Log.d("RestTest","onFailure:"+t.message)

            }
        })
    }



}