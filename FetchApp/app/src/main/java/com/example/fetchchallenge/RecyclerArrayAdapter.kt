package com.example.fetchchallenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerArrayAdapter(urlResponseList : MutableList<JsonContentInfo>) : RecyclerView.Adapter<RecyclerArrayAdapter.ResponseInfoViewHolder>() {

    private var mJsonResponseInfoList = urlResponseList

    class ResponseInfoViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val mResponseListId : TextView = itemView.findViewById(R.id.list_id)
        val mResponseId : TextView = itemView.findViewById(R.id.content_id)
        val mResponseName : TextView = itemView.findViewById(R.id.content_name)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ResponseInfoViewHolder {
        return ResponseInfoViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_recyclerview, parent, false))
    }

    override fun onBindViewHolder(holder : ResponseInfoViewHolder, position: Int) {
        holder.mResponseListId.text = mJsonResponseInfoList[position].responseListId
        holder.mResponseId.text = mJsonResponseInfoList[position].responseId.toString()
        holder.mResponseName.text = mJsonResponseInfoList[position].responseName
    }

    override fun getItemCount() : Int {
        return mJsonResponseInfoList.size
    }
}

data class JsonContentInfo(
    val responseListId : String,
    val responseId : Int,
    val responseName : String
)