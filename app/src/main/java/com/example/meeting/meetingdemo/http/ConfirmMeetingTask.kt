package com.example.meeting.meetingdemo.http

import android.app.Activity
import com.example.meeting.meetingdemo.MyApplication

class ConfirmMeetingTask(private val context: Activity, private val meetingId: String): Runnable {

    override fun run() {
        val confirmRequest = GsonRequest<String>(
            (context.application as MyApplication).getDetailApi()+"/"+meetingId,
            null,
            context,
            null,
            null
        )

        (context.application as MyApplication).getRequestQueue().add(confirmRequest)
    }
}
