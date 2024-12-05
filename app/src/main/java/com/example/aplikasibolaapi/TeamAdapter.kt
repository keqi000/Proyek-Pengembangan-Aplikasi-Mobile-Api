package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TeamAdapter(
    private val teams: Array<Team>,
    private val onTeamClick: (Int) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    inner class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val teamName: TextView = itemView.findViewById(R.id.teamName)
        val teamBadge: ImageView = itemView.findViewById(R.id.teamBadge)

        fun bind(team: Team) {
            // Print the team_key and team_name for debugging
            println("Team Key: ${team.team_key}, Team Name: ${team.team_name}, Team Badge: ${team.team_badge}")

            teamName.text = team.team_name

            // Use the badge URL provided in the API response
            val badgeUrl = team.team_badge

            // Load the badge with Glide
            Glide.with(itemView.context)
                .load(badgeUrl)
                .placeholder(R.drawable.placeholder_image)  // Placeholder image while loading
                .error(R.drawable.error_image)             // Fallback in case of an error
                .into(teamBadge)
        }

        init {
            itemView.setOnClickListener {
                // Use `team_key` when the item is clicked
                onTeamClick(teams[bindingAdapterPosition].team_key)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teams[position])
    }

    override fun getItemCount() = teams.size
}
