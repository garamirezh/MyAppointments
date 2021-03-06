package com.digitalarray.myappointments.io

import com.digitalarray.myappointments.io.response.LoginResponse
import com.digitalarray.myappointments.model.Doctor
import com.digitalarray.myappointments.model.Schedule
import com.digitalarray.myappointments.model.Specialty
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("specialties")
    fun getSpecialties(): Call<ArrayList<Specialty>>

    @GET("specialties/{specialties}/doctors")
    fun getDoctors(@Path("specialties") specialtyId: Int): Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId: Int, @Query("date") date: String): Call<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email: String, @Query("password") password: String): Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>

    companion object Factory {
        private const val  BASE_URL = "https://appointments.digitalarray.com.co/api/"

        fun create(): ApiService{
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}