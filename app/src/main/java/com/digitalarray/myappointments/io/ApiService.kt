package com.digitalarray.myappointments.io

import com.digitalarray.myappointments.model.Doctor
import com.digitalarray.myappointments.model.Specialty
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("specialties")
    abstract fun getSpecialties(): Call<ArrayList<Specialty>>

    @GET("specialties/{specialties}/doctors")
    abstract fun getDoctors(@Path("specialties") specialtyId: Int): Call<ArrayList<Doctor>>

    companion object Factory {
        private const val  BASE_URL = "https://appointments.digitalarray.com.co/api/"

        fun create(): ApiService{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}