package com.example.homeisolation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private  val historyList: ArrayList<HistoryModel>): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.transaction_item, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = historyList[position]
        holder.heart_rate.text = "Heart Rate : " + currentitem.heart_rate + " BPM"
        holder.spo2.text = "SpO2: " + currentitem.spo2 + " %"
        holder.timestamp.text = currentitem.timestamp
        holder.location.text = "Location: " + currentitem.location
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val heart_rate: TextView = itemView.findViewById(R.id.heartRateValue)
        val spo2 :TextView = itemView.findViewById(R.id.spo2Value)
        var timestamp :TextView = itemView.findViewById(R.id.transcationTimestamp)
        var location: TextView = itemView.findViewById(R.id.locationValue)

    }

