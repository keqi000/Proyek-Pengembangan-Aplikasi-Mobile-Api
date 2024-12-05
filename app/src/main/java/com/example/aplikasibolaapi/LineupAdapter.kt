package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LineupAdapter(private val players: List<Player>) : RecyclerView.Adapter<LineupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lineup, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        holder.playerName.text = player.lineup_player
        holder.playerNumber.text = player.lineup_number
        holder.playerPosition.text = player.lineup_position
    }

    override fun getItemCount(): Int = players.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.playerName)
        val playerNumber: TextView = view.findViewById(R.id.playerNumber)
        val playerPosition: TextView = view.findViewById(R.id.playerPosition)
    }
}


