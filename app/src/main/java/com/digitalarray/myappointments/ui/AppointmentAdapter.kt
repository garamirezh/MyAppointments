package com.digitalarray.myappointments.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.digitalarray.myappointments.R
import com.digitalarray.myappointments.model.Appointment

class AppointmentAdapter(private val appointments: ArrayList<Appointment> ): RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvAppointmentId: AppCompatTextView = itemView.findViewById(R.id.tvAppointmentId)
        private val tvDoctorName: AppCompatTextView = itemView.findViewById(R.id.tvDoctorName)
        private val tvScheduledDate: AppCompatTextView = itemView.findViewById(R.id.tvScheduledDate)
        private val tvScheduledTime: AppCompatTextView = itemView.findViewById(R.id.tvScheduledTime)

        fun bind(appointment: Appointment){
            tvAppointmentId.text = itemView.context.getString(R.string.item_appointment_id, appointment.id)
            tvDoctorName.text = appointment.doctorName
            tvScheduledDate.text = itemView.context.getString(R.string.item_appointment_date, appointment.scheduledDate)
            tvScheduledTime.text = itemView.context.getString(R.string.item_appointment_time, appointment.scheduledTime)
        }
    }

    //Inflates XML items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        )
    }

    //Binds data
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    //Number of elements
    override fun getItemCount(): Int {
        return appointments.size
    }

}