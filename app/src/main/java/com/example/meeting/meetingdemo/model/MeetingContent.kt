package com.example.meeting.meetingdemo.model

import androidx.databinding.ObservableField

data class MeetingContent(
    var currentTime: ObservableField<String>,
    var currentDate: ObservableField<String>,
    val status: ObservableField<String>,
    val backgroundColor: ObservableField<Int>,
    val statusFree: ObservableField<Int>,
    val statusBooked: ObservableField<Int>,
    val statusWaiting: ObservableField<Int>,
    val msg: ObservableField<String>,
    val organizer: ObservableField<String>
)