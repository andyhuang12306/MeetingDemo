package com.example.meeting.meetingdemo.progressbarformatter

import com.dinuscxj.progressbar.CircleProgressBar

class HookProgressFormatter : CircleProgressBar.ProgressFormatter {
    override fun format(progress: Int, max: Int): CharSequence {
        return ""
    }
}