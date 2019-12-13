package com.example.meeting.meetingdemo.model

import androidx.databinding.ObservableField

data class Meeting(
    val _id: String="",
    val startTime: String="09:30",
    val endTime: String="10:30",
    var status: String="FREE",
    var textColor: ObservableField<Int>,
    val topic: String="Comm Session",
    val bookedBy: String="Tom",
    val startTimeStamp: Long,
    val endTimeStamp: Long
)