package com.digitalarray.myappointments.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.digitalarray.myappointments.util.PreferenceHelper
import com.digitalarray.myappointments.databinding.ActivityMenuBinding
import com.digitalarray.myappointments.io.ApiService
import com.digitalarray.myappointments.util.PreferenceHelper.set
import com.digitalarray.myappointments.util.PreferenceHelper.get
import com.digitalarray.myappointments.util.toast
import com.google.android.gms.ads.AdRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private  lateinit var binding : ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnCreateAppointment.setOnClickListener {
            val intent = Intent(this, CreateAppointmentActivity::class.java)
            startActivity(intent)
        }

        binding.btnMyAppointments.setOnClickListener {
            val intent = Intent(this, AppointmentsActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener{
            performLogout()
        }
        initLoadAds()
    }

    private fun performLogout() {
        val jwt = preferences["jwt", ""]
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object : Callback<Void>{
            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreferences()

                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun initLoadAds() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.bannerMenu.loadAd(adRequest)
    }

    private fun clearSessionPreferences() {
        preferences["jwt"] = ""
    }


}