package com.example.meeting.meetingdemo.model

data class EventModel(
    val _id: String,
    val Name: String,
    val Desc: String,
    val Start: String,
    val End: String,
    val Organizer: OrganizerModel
)