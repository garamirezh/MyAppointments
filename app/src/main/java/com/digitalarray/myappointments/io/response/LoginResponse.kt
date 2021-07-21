package com.digitalarray.myappointments.io.response

import com.digitalarray.myappointments.model.User

data class LoginResponse (val success: Boolean, val user: User, val jwt: String)