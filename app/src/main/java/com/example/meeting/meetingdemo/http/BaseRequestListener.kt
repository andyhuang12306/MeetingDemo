package com.example.meeting.meetingdemo.http

interface BaseRequestListener<T> {
    fun onSuccess(data: T)
    fun onFailure(code: T)
}
