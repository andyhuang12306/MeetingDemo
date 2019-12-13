package com.example.meeting.meetingdemo.http

import android.app.Activity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.meeting.meetingdemo.MyApplication
import org.json.JSONObject

class RequestPairTask(private val context: Activity,
                      private val listener: BaseRequestListener<String>,
                      private val randomUUID: String,
                      private val code: String): Runnable {

    override fun run() {
        val registerRequest = JsonObjectRequest(
            Request.Method.POST,
            (context.application as MyApplication).getRegisterApi(),
            JSONObject().put("UID", randomUUID)
                .put("PairingCode", code)
                .put("Hardware", JSONObject().put("Brand", "SKT")
                    .put("Model", "MDN-911"))
                .put("Software", JSONObject().put("Ver", "0.20190701.3-MDN-911")),
            Response.Listener { response ->
                val token: String = response["token"] as String
                (context.application as MyApplication).setToken(token)
                listener.onSuccess(response.toString())
            },
            Response.ErrorListener { error ->
                Log.d("MainActivity", error.message)
                run()
            })
        (context.application as MyApplication).getRequestQueue().add(registerRequest)
    }
}
