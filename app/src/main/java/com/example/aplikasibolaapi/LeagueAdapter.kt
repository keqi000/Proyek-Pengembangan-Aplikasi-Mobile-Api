package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LeagueAdapter(
    private val leagues: Array<League>,
    private val onLeagueClick: (Int) -> Unit
) : RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>() {

    inner class LeagueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val leagueName: TextView = itemView.findViewById(R.id.itemText)
        val leagueLogo: ImageView = itemView.findViewById(R.id.itemImage)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLeagueClick(leagues[position].league_id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return LeagueViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        val league = leagues[position]
        holder.leagueName.text = league.league_name

        // Use the league_logo URL provided in the API response
        val logoUrl = league.league_logo  // This is now directly from the API response

        // Load the logo with Glide and handle placeholder and error images
        Glide.with(holder.itemView.context)
            .load(logoUrl)
            .placeholder(R.drawable.placeholder_image)  // Placeholder image while loading
            .error(R.drawable.error_image)             // Fallback in case of failure
            .into(holder.leagueLogo)
    }

    override fun getItemCount() = leagues.size
}
