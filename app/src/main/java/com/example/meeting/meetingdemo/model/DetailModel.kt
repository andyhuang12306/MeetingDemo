package com.example.meeting.meetingdemo.model

data class DetailModel(
    val Token: String,
    val Room: RoomModel,
    val Events: ArrayList<EventModel>
)