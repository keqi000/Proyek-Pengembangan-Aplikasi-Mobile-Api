package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class StatisticsAdapter(private val statistics: List<Statistic>) : RecyclerView.Adapter<StatisticsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistic, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val statistic = statistics[position]
        holder.statType.text = statistic.type
        holder.homeStat.text = statistic.home
        holder.awayStat.text = statistic.away
    }

    override fun getItemCount(): Int = statistics.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statType: TextView = view.findViewById(R.id.statType)
        val homeStat: TextView = view.findViewById(R.id.homeStat)
        val awayStat: TextView = view.findViewById(R.id.awayStat)
    }
}
