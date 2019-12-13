package com.example.meeting.meetingdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.meeting.meetingdemo.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.meeting.meetingdemo.R
import com.example.meeting.meetingdemo.databinding.ItemMeetingContentBinding
import com.example.meeting.meetingdemo.model.Meeting

class RecyclerAdapter(private val meetings: ArrayList<Meeting>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var binding: ItemMeetingContentBinding?=null

    inner class ViewHolder(val binding: ItemMeetingContentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_meeting_content,
            parent,
            false
        )
        return if(binding!=null){
            ViewHolder(binding!!)
        }else{
            ViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_meeting_content,
                parent,
                false
            ))
        }
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meeting = meetings[position]
        holder.binding.setVariable(BR.meeting, meeting)
    }

    fun excutePendingBindgs() {
        binding?.executePendingBindings()
    }

}