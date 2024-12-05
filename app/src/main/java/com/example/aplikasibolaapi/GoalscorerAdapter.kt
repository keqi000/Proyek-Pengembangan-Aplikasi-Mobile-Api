package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class GoalscorerAdapter(private val goalscorers: List<Goalscorer>) : RecyclerView.Adapter<GoalscorerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goalscorer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goalscorer = goalscorers[position]
        holder.homeScorer.text = goalscorer.home_scorer
        holder.awayScorer.text = goalscorer.away_scorer
        holder.score.text = goalscorer.score
    }

    override fun getItemCount(): Int = goalscorers.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeScorer: TextView = view.findViewById(R.id.homeScorer)
        val awayScorer: TextView = view.findViewById(R.id.awayScorer)
        val score: TextView = view.findViewById(R.id.score)
    }
}
