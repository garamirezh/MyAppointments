package com.digitalarray.myappoitments

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.digitalarray.myappoitments.databinding.ActivityCreateAppointmentBinding
import java.util.*

class CreateAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAppointmentBinding
    private val selectedCalendar = Calendar.getInstance()
    private var selectedRadioButton: RadioButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAppointmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnNext.setOnClickListener {
            if(binding.etDescription.text.toString().length < 3) {
                binding.etDescription.error = getString(R.string.validate_appointment_description)
            }else{
                //continue Step 2
                binding.cvStep1.visibility = View.GONE
                binding.cvStep2.visibility = View.VISIBLE
            }

        }

        binding.btnConfirmAppointment.setOnClickListener {
            Toast.makeText(this, "Cita registrada exitosamente", Toast.LENGTH_LONG).show()
            finish()
        }

        val specialityOptions =
            arrayOf("Speciality A", "Speciality B", "Speciality C", "Speciality D")
        binding.spinnerSpeciality.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, specialityOptions)

        val doctorOptions = arrayOf("Doctor A", "Doctor B", "Doctor C", "Doctor D")
        binding.spinnerDoctors.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, doctorOptions)
    }


    fun onClickScheduleDate() {
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        val listener = DatePickerDialog.OnDateSetListener { _, y, m, d ->
            Toast.makeText(this, "$y-$m-$d", Toast.LENGTH_LONG).show()
            selectedCalendar.set(y, m, d)
            binding.etScheduledDate.setText(
                resources.getString(
                    R.string.date_format,
                    y,
                    m.twoDigits(),
                    d.twoDigits()
                )
            )
            displayRadioButtons()
        }

        //new dialog
        val datePickerDialog = DatePickerDialog(this, listener, year, month, dayOfMonth)

        //set limits
        val datePicker = datePickerDialog.datePicker
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        datePicker.minDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_MONTH, 29)
        datePicker.maxDate = calendar.timeInMillis

        //show dialog
        datePickerDialog.show()

    }

    private fun displayRadioButtons() {

        selectedRadioButton = null
        binding.radioGroupLeft.removeAllViews()
        binding.radioGroupRight.removeAllViews()

        val hours = arrayOf("8:00 AM", "8:30 AM","9:00 AM","9:30 AM","10:00 AM","10:30 AM" )
        var goToLeft = true

        hours.forEach {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = it

            radioButton.setOnClickListener { view ->
                selectedRadioButton?.isChecked = false
                selectedRadioButton = view as RadioButton?
                selectedRadioButton?.isChecked = true
            }

            if(goToLeft)
                binding.radioGroupLeft.addView(radioButton)
            else
                binding.radioGroupRight.addView(radioButton)
            goToLeft = !goToLeft
        }


    }

    private fun Int.twoDigits() = if(this>=10) this.toString() else "0$this"

    override fun onBackPressed() {
        if(binding.cvStep2.visibility == View.VISIBLE){
            binding.cvStep1.visibility == View.GONE
            binding.cvStep1.visibility = View.VISIBLE
        }else if(binding.cvStep1.visibility == View.VISIBLE){
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.dialog_create_appointment_exit_title))
            builder.setMessage(getString(R.string.dialog_create_appointment_exit_message))
            builder.setPositiveButton(getString(R.string.dialog_create_appointment_exit_positive_btn)) { _, _ ->
                finish()
            }
            builder.setNegativeButton(getString(R.string.dialog_create_appointment_exit_negative_btn)) { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}