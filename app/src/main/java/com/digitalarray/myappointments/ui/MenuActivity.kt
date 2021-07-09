package com.digitalarray.myappointments.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.digitalarray.myappointments.PreferenceHelper
import com.digitalarray.myappointments.databinding.ActivityMenuBinding
import com.digitalarray.myappointments.PreferenceHelper.set
import com.google.android.gms.ads.AdRequest

class MenuActivity : AppCompatActivity() {

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
            clearSessionPreferences()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        initLoadAds()
    }

    private fun initLoadAds() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.bannerMenu.loadAd(adRequest)
    }

    private fun clearSessionPreferences() {
        /*val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session", false)
        editor.apply()*/
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = false
    }


}