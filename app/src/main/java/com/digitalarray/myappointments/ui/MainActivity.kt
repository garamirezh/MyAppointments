package com.digitalarray.myappointments.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.digitalarray.myappointments.util.PreferenceHelper
import com.digitalarray.myappointments.databinding.ActivityMainBinding
import com.digitalarray.myappointments.util.PreferenceHelper.get
import com.digitalarray.myappointments.util.PreferenceHelper.set
import com.digitalarray.myappointments.R
import com.digitalarray.myappointments.io.ApiService
import com.digitalarray.myappointments.io.response.LoginResponse
import com.digitalarray.myappointments.util.toast
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

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

        if (preferences["jwt", ""].contains("."))
            goToMenuActivity()


        binding.btnLogin.setOnClickListener {
            //validates
            performLogin()
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

    private fun performLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(email.trim().isEmpty() || password.trim().isEmpty()){
            toast(getString(R.string.error_empty_credentials))
            return
        }


        val call = apiService.postLogin(email, password)
        call.enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse == null){
                        toast(getString(R.string.error_login_response))
                        return
                    }
                    if(loginResponse.success){
                        createSessionPreferences(loginResponse.jwt)
                        toast(getString(R.string.welcome_name, loginResponse.user.name))
                        goToMenuActivity()
                    }else {
                        toast(getString(R.string.error_invalid_credentials))
                    }
                }else{
                    toast(getString(R.string.error_login_response))
                }
            }
        })
    }

    private fun createSessionPreferences(jwt:String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["jwt"] = jwt
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
