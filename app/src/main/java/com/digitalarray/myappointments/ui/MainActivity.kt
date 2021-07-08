package com.digitalarray.myappointments.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.digitalarray.myappointments.PreferenceHelper
import com.digitalarray.myappointments.databinding.ActivityMainBinding
import com.digitalarray.myappointments.PreferenceHelper.get
import com.digitalarray.myappointments.PreferenceHelper.set
import com.digitalarray.myappointments.R
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val snackBar by lazy {
        Snackbar.make(binding.mainLayout, R.string.press_back_again, Snackbar.LENGTH_LONG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /* val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
         val session = preferences.getBoolean("session", false)*/

        val preferences = PreferenceHelper.defaultPrefs(this)

        if (preferences["session", false])
            goToMenuActivity()


        binding.btnLogin.setOnClickListener {
            //validates
            createSessionPreferences()

            goToMenuActivity()
        }

        binding.tvGoToSignup.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.please_fill_your_register_data),
                Toast.LENGTH_LONG
            ).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createSessionPreferences() {
        /* val preferences = getSharedPreferences("general", Context.MODE_PRIVATE)
         val editor = preferences.edit()
         editor.putBoolean("session", true)
         editor.apply()*/
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["session"] = true
    }

    private fun goToMenuActivity() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (snackBar.isShown)
            super.onBackPressed()
        else
            snackBar.show()
    }
}
