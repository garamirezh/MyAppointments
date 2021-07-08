package com.digitalarray.myappoitments.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.digitalarray.myappoitments.databinding.ActivityAppointmentsBinding
import com.digitalarray.myappoitments.model.Appointment

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
    }
}