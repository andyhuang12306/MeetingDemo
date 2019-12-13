package com.example.meeting.meetingdemo.util

import java.text.SimpleDateFormat
import java.util.*


object DateUtil {
    fun parseDate(str: String): Date{
        val string = str.replace("Z", " UTC")
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z")
        return format.parse(string)
    }
}