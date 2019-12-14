package com.example.meeting.meetingdemo.http

import android.app.Activity
import android.util.Base64
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.example.meeting.meetingdemo.MyApplication
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

class GsonRequest<T>(
    url: String,
    private val clazz: Class<T>?,
    private val context: Activity,
    private val listener: Response.Listener<T>?,
    errorListener: Response.ErrorListener?
) : Request<T>(Method.GET, url, errorListener) {
    private val gson = Gson()


    override fun getHeaders(): Map<String, String>{
        val header = HashMap<String, String>()
        header["deviceToken"]=(context.application as MyApplication).getToken()
        Log.d("MeetingList", header.toString())
        return header
    }

    override fun deliverResponse(response: T) = listener!!.onResponse(response)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Log.d("MeetingList", json)
            Response.success(
                gson.fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Response.error(ParseError(e))
        }
    }
}