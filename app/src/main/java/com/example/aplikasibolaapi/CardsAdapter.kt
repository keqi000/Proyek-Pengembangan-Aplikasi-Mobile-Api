package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CardsAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = cards[position]
        holder.homeFault.text = card.home_fault
        holder.awayFault.text = card.away_fault
        holder.card.text = card.card
    }

    override fun getItemCount(): Int = cards.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeFault: TextView = view.findViewById(R.id.homeFault)
        val awayFault: TextView = view.findViewById(R.id.awayFault)
        val card: TextView = view.findViewById(R.id.card)
    }
}
