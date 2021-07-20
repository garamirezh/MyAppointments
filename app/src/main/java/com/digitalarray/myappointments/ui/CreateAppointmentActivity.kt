package com.digitalarray.myappointments.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.digitalarray.myappointments.R
import com.digitalarray.myappointments.databinding.ActivityCreateAppointmentBinding
import com.digitalarray.myappointments.io.ApiService
import com.digitalarray.myappointments.model.Doctor
import com.digitalarray.myappointments.model.Schedule
import com.digitalarray.myappointments.model.Specialty
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class CreateAppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAppointmentBinding
    private val selectedCalendar = Calendar.getInstance()
    private var selectedTimeRadioBtn: RadioButton? = null

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAppointmentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnNext.setOnClickListener {
            if (binding.etDescription.text.toString().length < 3) {
                binding.etDescription.error = getString(R.string.validate_appointment_description)
            } else {
                //continue Step 2
                binding.cvStep1.visibility = View.GONE
                binding.cvStep2.visibility = View.VISIBLE
            }

        }

        binding.btnNext2.setOnClickListener {
            when {
                binding.etScheduledDate.text.toString().isEmpty() -> {
                    binding.etScheduledDate.error = getString(R.string.validate_appointment_date)
                }
                selectedTimeRadioBtn == null -> {
                    Snackbar.make(binding.createAppointmentLinearLayout,
                        R.string.validate_appointment_time,Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    //continue Step 3
                    showAppointmentDateToConfirm()
                    binding.cvStep2.visibility = View.GONE
                    binding.cvStep3.visibility = View.VISIBLE
                }
            }
        }

        binding.btnConfirmAppointment.setOnClickListener {
            Toast.makeText(this, "Cita registrada exitosamente", Toast.LENGTH_LONG).show()
            finish()
        }

        loadSpecialties()
        listenSpecialtyChanges()
        listenDoctorAndDateChanges()

    }

    private fun listenDoctorAndDateChanges() {
        //Doctors
        binding.spinnerDoctors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val doctor: Doctor = adapter?.getItemAtPosition(position) as Doctor
                loadHours(doctor.id, binding.etScheduledDate.text.toString())
            }
        }
        //Schedule Date
        binding.etScheduledDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val doctor = binding.spinnerDoctors.selectedItem as Doctor
                loadHours(doctor.id, binding.etScheduledDate.text.toString())
            }

        })
    }

    private fun loadHours(doctorId: Int, date: String) {
        val call = apiService.getHours(doctorId, date)
        call.enqueue(object : Callback<Schedule>{
            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity, getString(R.string.error_loading_hours), Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if(response.isSuccessful){
                    val schedule = response.body()
                    Toast.makeText(this@CreateAppointmentActivity, "morning: ${schedule?.morning?.size}, afternoon: ${schedule?.afternoon?.size}", Toast.LENGTH_LONG).show()
                }
            }
        })
        //Toast.makeText(this, "doctor: $doctorId, date: $date", Toast.LENGTH_SHORT).show()
    }

    private fun loadSpecialties() {
        val call = apiService.getSpecialties()
        call.enqueue(object : Callback<ArrayList<Specialty>>{
            override fun onFailure(call: Call<ArrayList<Specialty>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity, getString(R.string.error_loading_specialties), Toast.LENGTH_LONG).show()
                finish()
            }
            override fun onResponse(call: Call<ArrayList<Specialty>>,response: Response<ArrayList<Specialty>>) {
                if(response.isSuccessful){
                    val specialties = response.body()
                    binding.spinnerSpeciality.adapter = ArrayAdapter(this@CreateAppointmentActivity, android.R.layout.simple_list_item_1, specialties!!)
                }
            }
        })


    }

    private fun listenSpecialtyChanges() {
        binding.spinnerSpeciality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val specialty: Specialty = adapter?.getItemAtPosition(position) as Specialty
                loadDoctors(specialty.id)
            }
        }
    }

    private fun loadDoctors(specialtyId: Int) {
        val call = apiService.getDoctors(specialtyId)
        call.enqueue(object : Callback<ArrayList<Doctor>> {
            override fun onResponse(
                call: Call<ArrayList<Doctor>>,
                response: Response<ArrayList<Doctor>>
            ) {
                if (response.isSuccessful) {
                    val doctors = response.body()
                    binding.spinnerDoctors.adapter = ArrayAdapter(
                        this@CreateAppointmentActivity,
                        android.R.layout.simple_list_item_1,
                        doctors!!
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<Doctor>>, t: Throwable) {
                Toast.makeText(
                    this@CreateAppointmentActivity,
                    getString(R.string.error_loading_doctors),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        })
    }

    private fun showAppointmentDateToConfirm() {
        binding.tvConfirmDescription.text = binding.etDescription.text.toString()
        binding.tvConfirmSpeciality.text = binding.spinnerSpeciality.selectedItem.toString()

        val selectedRadioBtnId = binding.radioGroupType.checkedRadioButtonId
        val selectedRadioType = binding.radioGroupType.findViewById<RadioButton>(selectedRadioBtnId)

        binding.tvConfirmType.text = selectedRadioType.text.toString()

        binding.tvConfirmDoctor.text = binding.spinnerDoctors.selectedItem.toString()
        binding.tvConfirmAppointmentDate.text = binding.etScheduledDate.text.toString()
        binding.tvConfirmAppointmentTime.text = selectedTimeRadioBtn?.text.toString()
    }


    fun onClickScheduleDate(view: View) {
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
            binding.etScheduledDate.error = null
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

        selectedTimeRadioBtn = null
        binding.radioGroupLeft.removeAllViews()
        binding.radioGroupRight.removeAllViews()

        val hours = arrayOf("8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM")
        var goToLeft = true

        hours.forEach {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = it

            radioButton.setOnClickListener { view ->
                selectedTimeRadioBtn?.isChecked = false
                selectedTimeRadioBtn = view as RadioButton?
                selectedTimeRadioBtn?.isChecked = true
            }

            if (goToLeft)
                binding.radioGroupLeft.addView(radioButton)
            else
                binding.radioGroupRight.addView(radioButton)
            goToLeft = !goToLeft
        }


    }

    private fun Int.twoDigits() = if (this >= 10) this.toString() else "0$this"

    override fun onBackPressed() {
        when {
            binding.cvStep3.visibility == View.VISIBLE -> {
                binding.cvStep3.visibility = View.GONE
                binding.cvStep2.visibility = View.VISIBLE

            }
            binding.cvStep2.visibility == View.VISIBLE -> {
                binding.cvStep2.visibility = View.GONE
                binding.cvStep1.visibility = View.VISIBLE

            }
            binding.cvStep1.visibility == View.VISIBLE -> {
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
}