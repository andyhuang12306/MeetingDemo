package com.example.meeting.meetingdemo.progressbarformatter

import com.dinuscxj.progressbar.CircleProgressBar

class TimeProgressFormatter : CircleProgressBar.ProgressFormatter {
    override fun format(progress: Int, max: Int): CharSequence {
        val hour = (max - progress) / 60
        val seconds = (max - progress) - hour * 60
        if(progress>=max){return ""}
        return "${hour}:${seconds}"
    }
}