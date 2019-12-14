package com.example.meeting.meetingdemo

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.BaseHttpStack
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley
import java.io.File
import java.io.IOException
import java.security.InvalidParameterException

class MyApplication : Application(){


    private var mSerialPort: SerialPort? = null
    private lateinit var requestQueue:RequestQueue
    private val REGISTER_API = "https://mrm.try.com.sg/api/device/reg"
    private val GET_DETAIL_API = "https://mrm.try.com.sg/api/device"
    private val GET_CONFIRM_MEETING_API = "https://mrm.try.com.sg/api/device/confirme"
    private var token = ""
    private var pairCode = ""

    @Throws(
        SecurityException::class,
        IOException::class,
        InvalidParameterException::class
    )

    override fun onCreate() {
        super.onCreate()
        requestQueue=Volley.newRequestQueue(this)
    }

    fun setCode(t: String){
        pairCode=t
    }

    fun getCode(): String{
        return pairCode
    }

    fun setToken(t: String){
        token=t
    }

    fun getToken(): String{
        return token
    }

    fun getRequestQueue(): RequestQueue{
        return requestQueue
    }

    fun getRegisterApi(): String{
        return REGISTER_API
    }

    fun getDetailApi(): String{
        return GET_DETAIL_API
    }

    fun getConfirmMeetingApi(): String{
        return GET_CONFIRM_MEETING_API
    }

    fun getSerialPort(): SerialPort? {
        if (mSerialPort == null) {
            mSerialPort = SerialPort(File("/dev/ttyS1"), 19200)
        }
        return mSerialPort
    }

    fun closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort?.close()
            mSerialPort = null
        }
    }
}