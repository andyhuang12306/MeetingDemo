package com.example.meeting.meetingdemo.activity

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.meeting.meetingdemo.MyApplication
import com.example.meeting.meetingdemo.R
import com.example.meeting.meetingdemo.databinding.ActivityMainBinding
import com.example.meeting.meetingdemo.http.BaseRequestListener
import com.example.meeting.meetingdemo.http.RequestPairTask
import com.example.meeting.meetingdemo.model.PairCode
import kotlin.random.Random


class MainActivity : BaseActivity(),
    BaseRequestListener<String> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )
        val code: String=generatePairCode()
        (application as MyApplication).setCode(code)
        binding.pairCode= PairCode(code)
        val uid = getUID()
        RequestPairTask(
            this,
            this,
            uid.toString(),
            code
        ).run()
    }

    private fun getUID(): Int {
        val uid: Int
        uid = try {
            val info: ApplicationInfo = application.packageManager.getApplicationInfo(
                application.packageName, 0
            )
            info.uid
        } catch (e: PackageManager.NameNotFoundException) {
            -1
        }
        Log.d("MainActivity", "UID = $uid")
        val code = (application as MyApplication).getCode()
        Log.d("MainActivity", "Code = $code")
        return uid
    }

    private fun generatePairCode(): String {
        val stringBuilder=StringBuilder()
        var size=0
        while (size<4){
            stringBuilder.append(Random.nextInt(10))
            size++
        }
        return stringBuilder.toString()
    }

    override fun onSuccess(data: String) {
        val intent=Intent(this, MeetingListActivity::class.java)
        startActivity(intent)
    }

    override fun onFailure(code: String) {

    }
}
