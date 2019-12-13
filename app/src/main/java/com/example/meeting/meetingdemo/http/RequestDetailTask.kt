package com.example.meeting.meetingdemo.http

import com.example.meeting.meetingdemo.MyApplication
import com.example.meeting.meetingdemo.activity.MeetingListActivity
import com.example.meeting.meetingdemo.model.DetailModel

class RequestDetailTask(private val context: MeetingListActivity): Runnable {

    override fun run() {
        val detailRequest = GsonRequest(
            (context.application as MyApplication).getDetailApi(),
            DetailModel::class.java,
            context,
            context,
            context
        )

        (context.application as MyApplication).getRequestQueue().add(detailRequest)
    }
}
