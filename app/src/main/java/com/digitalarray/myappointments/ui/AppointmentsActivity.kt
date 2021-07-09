package com.digitalarray.myappointments.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.digitalarray.myappointments.databinding.ActivityAppointmentsBinding
import com.digitalarray.myappointments.model.Appointment
import com.google.android.gms.ads.AdRequest

class AppointmentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val appointments = ArrayList<Appointment>()
        appointments.add(
            Appointment(1,"Medico Test1", "12/12/2018","3:00 PM")
        )
        appointments.add(
            Appointment(2,"Medico Test2", "12/12/2018","3:00 PM")
        )
        appointments.add(
            Appointment(3,"Medico Test3", "12/12/2018","3:00 PM")
        )

        binding.rvAppointments.layoutManager = LinearLayoutManager(this)
        binding.rvAppointments.adapter = AppointmentAdapter(appointments)
        initLoadAds()
    }

    private fun initLoadAds() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.bannerMyAppointments.loadAd(adRequest)
    }
}