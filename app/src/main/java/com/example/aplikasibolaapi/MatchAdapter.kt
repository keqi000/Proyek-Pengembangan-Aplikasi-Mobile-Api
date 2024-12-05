package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MatchAdapter(
    private val matches: Array<Match>,
    private val onMatchClick: (Match) -> Unit
) : RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teamName: TextView = itemView.findViewById(R.id.teamName)
        val opponentName: TextView = itemView.findViewById(R.id.opponentName)
        val matchDate: TextView = itemView.findViewById(R.id.matchDate)
        val teamHomeBadge: ImageView = itemView.findViewById(R.id.teamHomeBadge)
        val teamAwayBadge: ImageView = itemView.findViewById(R.id.teamAwayBadge)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onMatchClick(matches[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]

        // Set team names and match date
        holder.teamName.text = match.match_hometeam_name
        holder.opponentName.text = match.match_awayteam_name
        holder.matchDate.text = match.match_date  // Set the match date

        // Load team badges using Glide
        Glide.with(holder.itemView.context)
            .load(match.team_home_badge)
            .placeholder(R.drawable.placeholder_image) // Placeholder image
            .error(R.drawable.error_image) // Error image
            .into(holder.teamHomeBadge)

        Glide.with(holder.itemView.context)
            .load(match.team_away_badge)
            .placeholder(R.drawable.placeholder_image) // Placeholder image
            .error(R.drawable.error_image) // Error image
            .into(holder.teamAwayBadge)
    }

    override fun getItemCount() = matches.size
}

