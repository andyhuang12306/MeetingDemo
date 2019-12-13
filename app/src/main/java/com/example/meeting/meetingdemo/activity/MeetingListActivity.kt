package com.example.meeting.meetingdemo.activity


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.VolleyError
import com.example.meeting.meetingdemo.BookOnclickHandler
import com.example.meeting.meetingdemo.MyApplication
import com.example.meeting.meetingdemo.R
import com.example.meeting.meetingdemo.adapter.RecyclerAdapter
import com.example.meeting.meetingdemo.databinding.ActivityMeetingContentBinding
import com.example.meeting.meetingdemo.http.RequestDetailTask
import com.example.meeting.meetingdemo.model.DetailModel
import com.example.meeting.meetingdemo.model.Meeting
import com.example.meeting.meetingdemo.model.MeetingContent
import com.example.meeting.meetingdemo.progressbarformatter.HookProgressFormatter
import com.example.meeting.meetingdemo.progressbarformatter.TimeProgressFormatter
import com.example.meeting.meetingdemo.util.DateUtil
import kotlinx.android.synthetic.main.activity_meeting_content.*
import kotlinx.android.synthetic.main.title_activity.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MeetingListActivity : BaseActivity(),
    Response.Listener<DetailModel>, Response.ErrorListener {

    private var meetings = ArrayList<Meeting>()
    private val formatTime = SimpleDateFormat("hh:mm a")
    private val formatDate = SimpleDateFormat("EEE, d MMM yyyy")
    private val bookOnclickHandler = BookOnclickHandler()

    private val timeFormatter=
        TimeProgressFormatter()
    private val hookFormatter=
        HookProgressFormatter()

    lateinit var roomName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMeetingContentBinding>(
            this,
            R.layout.activity_meeting_content
        )

        val meetingContent =
            MeetingContent(
                ObservableField("13:30"),
                ObservableField("Thursday, July 19"),
                ObservableField("Free"),
                ObservableField(resources.getColor(R.color.color_free)),
                ObservableField(View.VISIBLE),
                ObservableField(View.GONE),
                ObservableField(View.GONE),
                ObservableField("Book now"),
                ObservableField("Tom")
            )

        roomName = binding.titleLayout.room_name
        binding.meeting = meetingContent
        binding.clickHandler =
            bookOnclickHandler


        val recyclerView = binding.recyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = RecyclerAdapter(meetings)
        setTimerTask(this, meetingContent, binding)
    }


    private fun setTimerTask(context: MeetingListActivity,
                             meetingContent: MeetingContent,
                             binding: ActivityMeetingContentBinding
    ) {
        val timer = Timer()
        binding.lineProgressBooked.max = 0
        binding.lineProgressWaiting.max = 0
        val currentTimeMillis = System.currentTimeMillis()
        val date = Date(currentTimeMillis)
        updateCurrentTimeAndDate(meetingContent, binding, date)
        val seconds = date.seconds
        timer.schedule(object : TimerTask() {
            var progressBooked = 0
            var progressWaiting = 0
            override fun run() {
                RequestDetailTask(context).run()
                val currentTimeMillisInner = System.currentTimeMillis()
                val dateInner = Date(currentTimeMillisInner)
                runOnUiThread {
                    updateCurrentTimeAndDate(meetingContent, binding, dateInner)
                    if (binding.lineProgressBooked.max == 0&&binding.lineProgressWaiting.max==0) {
                        mapMeetings(binding)
                        updateMeetingStatus(binding)
                    }
                    if(binding.meeting?.status?.get()=="Booked"){
                        if (binding.lineProgressBooked.max != 0) {
                            progressBooked += 1
                            binding.lineProgressBooked.progress = progressBooked
                            if (progressBooked >= binding.lineProgressBooked.max) {
                                binding.lineProgressBooked.max = 0
                                binding.lineProgressWaiting.max = 0
                                progressBooked=0
                                progressWaiting=0
                            }
                        }
                    }else if(binding.meeting?.status?.get()=="Waiting"){
                        if (binding.lineProgressWaiting.max != 0) {
                            progressWaiting += 1
                            binding.lineProgressWaiting.progress = progressWaiting
                            if (progressWaiting >= binding.lineProgressWaiting.max) {
                                binding.lineProgressBooked.max = 0
                                binding.lineProgressWaiting.max = 0
                                progressBooked=0
                                progressWaiting=0
                            }
                        }
                    }
                }
            }
        }, 60.minus(seconds.toLong())*1000 , 1000)
    }

    private fun mapMeetings(binding: ActivityMeetingContentBinding) {
        val filter = meetings.filter { it.endTimeStamp < System.currentTimeMillis() }
        filter.map { meetings.remove(it) }
        runOnUiThread {recyclerView.adapter?.notifyDataSetChanged()}
        meetings.map {
            if (it.startTimeStamp < System.currentTimeMillis() && System.currentTimeMillis() < it.endTimeStamp) {
                binding.lineProgressBooked.max =
                    ((it.endTimeStamp - System.currentTimeMillis()) / 1000).toInt()
                it.status = "Booked"
            } else if (
                it.startTimeStamp>System.currentTimeMillis()
                &&it.startTimeStamp-System.currentTimeMillis() <= 15 * 60 * 1000
            ) {
                binding.lineProgressWaiting.max =
                    ((it.startTimeStamp - System.currentTimeMillis()) / 1000).toInt()
                it.status = "Waiting"
            }  else if(
                it.startTimeStamp>System.currentTimeMillis()
                &&it.startTimeStamp-System.currentTimeMillis()>1*60*1000
            ){
                it.status="Free"
            }
        }
    }

    private fun updateMeetingStatus(binding: ActivityMeetingContentBinding) {
        if (meetings.size > 0) {
            val firstMeeting = meetings[0]
            binding.meeting?.status?.set(firstMeeting.status)
            binding.meeting?.msg?.set(firstMeeting.topic)
            binding.meeting?.organizer?.set(firstMeeting.bookedBy)
            when (firstMeeting.status) {
                "Free" -> {
                    binding.meeting?.backgroundColor?.set(resources.getColor(R.color.color_free))
                    binding.meeting?.statusFree?.set(View.VISIBLE)
                    binding.meeting?.statusBooked?.set(View.GONE)
                    binding.meeting?.statusWaiting?.set(View.GONE)
                    firstMeeting.textColor.set(resources.getColor(R.color.color_text_default))
                }
                "Booked" -> {
                    binding.meeting?.backgroundColor?.set(resources.getColor(R.color.color_booked))
                    binding.meeting?.statusFree?.set(View.GONE)
                    binding.meeting?.statusBooked?.set(View.VISIBLE)
                    binding.meeting?.statusWaiting?.set(View.GONE)
                    binding.lineProgressBooked.setProgressFormatter(timeFormatter)
                    firstMeeting.textColor.set(resources.getColor(R.color.color_booked))
                }
                "Waiting" -> {
                    binding.meeting?.backgroundColor?.set(resources.getColor(R.color.color_waiting))
                    binding.meeting?.statusFree?.set(View.GONE)
                    binding.meeting?.statusBooked?.set(View.GONE)
                    binding.meeting?.statusWaiting?.set(View.VISIBLE)
                    binding.lineProgressWaiting.setProgressFormatter(hookFormatter)
                    firstMeeting.textColor.set(resources.getColor(R.color.color_waiting))
                }
            }
            (recyclerView.adapter as RecyclerAdapter).excutePendingBindgs()
        }else{
            binding.meeting?.status?.set("Free")
            binding.meeting?.backgroundColor?.set(resources.getColor(R.color.color_free))
            binding.meeting?.statusFree?.set(View.VISIBLE)
            binding.meeting?.statusBooked?.set(View.GONE)
            binding.meeting?.statusWaiting?.set(View.GONE)
        }
    }

    private fun updateCurrentTimeAndDate(
        meetingContent: MeetingContent,
        binding: ActivityMeetingContentBinding,
        date: Date
    ) {
        val time = formatTime.format(date)
        val day = formatDate.format(date)
        meetingContent.currentTime.set(time)
        meetingContent.currentDate.set(day)
        binding.executePendingBindings()

    }

    override fun onDestroy() {
        super.onDestroy()
        bookOnclickHandler.popupWindow=null
        bookOnclickHandler.popupWindowMenu=null
        bookOnclickHandler.popupWindowExtendMeeting=null

    }

    override fun onErrorResponse(error: VolleyError?) {
        Log.d("MeetingList", error?.toString())
    }

    override fun onResponse(response: DetailModel?) {
        val token = response?.Token
        if(token!=null){
            (application as MyApplication).setToken(token)
        }
        val name = response?.Room?.Name
        if(name!=null){
            roomName.text=name
        }
        val events = response?.Events
        if(events!=null){
            val eventIds=ArrayList<String>()
            val deletedMeetings=ArrayList<Meeting>()
            for(event in events){
                val dateEnd = DateUtil.parseDate(event.End)
                val date = Date()
                if(dateEnd.after(date)){
                    val id = event._id
                    eventIds.add(id)
                    var goON=true
                    for(meet in meetings){
                        if(meet._id==id){
                            goON=false
                        }
                    }
                    if(goON){
                        val dateStart = DateUtil.parseDate(event.Start)
                        val topic = event.Name
                        val hoursStart = dateStart.hours
                        val minutesStart = dateStart.minutes
                        var minutesStartS: String
                        minutesStartS=if(minutesStart>=10){
                            minutesStart.toString()
                        }else{
                            minutesStart.toString()+"0"
                        }
                        val startTime=String.format("%s:%s", hoursStart, minutesStartS)
                        val hoursEnd = dateEnd.hours
                        val minutesEnd = dateEnd.minutes
                        var minutesEndS: String
                        minutesEndS=if(minutesStart>=10){
                            minutesEnd.toString()
                        }else{
                            minutesEnd.toString()+"0"
                        }
                        val endTime=String.format("%s:%s", hoursEnd, minutesEndS)
                        val organizer = event.Organizer.DisplayName
                        val meeting = Meeting(
                            id,
                            startTime,
                            endTime,
                            "Free",
                            ObservableField(resources.getColor(R.color.color_text_default)),
                            topic,
                            organizer,
                            dateStart.time,
                            dateEnd.time
                        )
                        meetings.add(meeting)
                    }
                }
            }
            meetings.sortBy { it.startTime }
            meetings.map {
                if (!eventIds.contains(it._id)) {
                    deletedMeetings.add(it)
                }
            }
            meetings.removeAll(deletedMeetings)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        Log.d("MeetingList", response?.Events.toString())
    }


}


