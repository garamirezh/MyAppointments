package com.digitalarray.myappoitments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.digitalarray.myappoitments.databinding.ActivityMenuBinding
import com.digitalarray.myappoitments.PreferenceHelper.set

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