package com.digitalarray.myappointments.model

data class HoursInterval (val start: String, val end: String){
    override fun toString(): String {
        return "$start - $end"
    }
}