package com.example.meeting.meetingdemo

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.example.meeting.meetingdemo.activity.MeetingListActivity
import com.example.meeting.meetingdemo.http.ConfirmMeetingTask
import com.example.meeting.meetingdemo.http.RequestDetailTask
import com.getbase.floatingactionbutton.FloatingActionsMenu
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class BookOnclickHandler() {
    var popupWindow: PopupWindow? = PopupWindow()
    var popupWindowMenu: PopupWindow? = PopupWindow()
    var popupWindowExtendMeeting: PopupWindow? = PopupWindow()
    var listTimer = ArrayList<Timer>()


    fun onClickToBook(view: View) {
        try {
            (view.parent as FloatingActionsMenu).toggle()
        }catch (e: Exception){

        }
        displayQRCode(
            view,
            "https://kotlinlang.org/docs/reference/delegated-properties.html#observable"
        )
    }

    fun onClickExtendMeeting(view: View) {
        (view.parent as FloatingActionsMenu).toggle()
        displayQRCode(view, "https://mrm.try.com.sg/")
    }

    fun onClickConfirm(view: View) {
        val context = view.context as MeetingListActivity
        ConfirmMeetingTask(context, context.meetings[0]._id).run()
    }

    fun onCLickDismiss(context: Context) {
        listTimer.forEach { it.cancel() }
        listTimer.clear()
        var timer = Timer()
        listTimer.add(timer)
        timer.schedule(object : TimerTask() {
            override fun run() {
                (context as Activity).runOnUiThread {
                    if (popupWindow != null && popupWindow?.isShowing!!) popupWindow?.dismiss()
                    if (popupWindowMenu != null && popupWindowMenu?.isShowing!!) popupWindowMenu?.dismiss()
                    if (popupWindowExtendMeeting != null && popupWindowExtendMeeting?.isShowing!!) popupWindowExtendMeeting?.dismiss()
                }
                this.cancel()
            }

        }, 60 * 1000)
    }

    private fun displayQRCode(view: View, url: String) {
        val v = LayoutInflater.from(view.context)
            .inflate(R.layout.qrcode_pop_window, null)
        v.findViewById<ImageView>(R.id.iv_dismiss).setOnClickListener {
            popupWindow?.dismiss()
        }

        popupWindow?.contentView = v
        popupWindow?.setBackgroundDrawable(null)
        popupWindow?.isOutsideTouchable = true
        popupWindow?.isFocusable = true
        popupWindow?.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindow?.height = ViewGroup.LayoutParams.MATCH_PARENT
        Runnable {
            val bitmap = QRCodeEncoder.syncEncodeQRCode(
                url,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    300f,
                    view.context.resources.displayMetrics
                ).toInt()
            )
            (view.context as Activity).runOnUiThread {
                v.findViewById<ImageView>(R.id.iv_qr_code).setImageBitmap(bitmap)
                popupWindow?.showAtLocation(view.rootView, Gravity.CENTER, 0, 0)
            }
        }.run()
        onCLickDismiss(view.context)
    }
}