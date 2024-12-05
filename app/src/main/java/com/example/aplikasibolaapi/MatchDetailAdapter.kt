package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MatchDetailAdapter(private val matchDetails: List<MatchDetail>) : RecyclerView.Adapter<MatchDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val matchDetail = matchDetails[position]

        // Bind match detail data
        holder.matchHometeamName.text = matchDetail.match_hometeam_name
        holder.matchAwayteamName.text = matchDetail.match_awayteam_name
        holder.matchDate.text = matchDetail.match_date
        holder.matchStatus.text = matchDetail.match_status
        holder.homeScore.text = matchDetail.match_hometeam_score
        holder.awayScore.text = matchDetail.match_awayteam_score

        // Bind other data to adapters
        holder.goalscorerList.adapter = GoalscorerAdapter(matchDetail.goalscorer)
        holder.cardsList.adapter = CardsAdapter(matchDetail.cards)

        // Flatten substitutions and pass to SubstitutionsAdapter
        val substitutions = matchDetail.substitutions.home + matchDetail.substitutions.away
        holder.substitutionsList.adapter = SubstitutionsAdapter(substitutions)

        // Combine lineups for home and away teams
        val allPlayers = matchDetail.lineup.home.starting_lineups +
                matchDetail.lineup.away.starting_lineups +
                matchDetail.lineup.home.substitutes +
                matchDetail.lineup.away.substitutes
        holder.lineupList.adapter = LineupAdapter(allPlayers)

        holder.statisticsList.adapter = StatisticsAdapter(matchDetail.statistics)
    }


    override fun getItemCount(): Int = matchDetails.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val matchHometeamName: TextView = view.findViewById(R.id.matchHometeamName)
        val matchAwayteamName: TextView = view.findViewById(R.id.matchAwayteamName)
        val matchDate: TextView = view.findViewById(R.id.matchDate)
        val matchStatus: TextView = view.findViewById(R.id.matchStatus)
        val homeScore: TextView = view.findViewById(R.id.homeScore)
        val awayScore: TextView = view.findViewById(R.id.awayScore)

        val goalscorerList: RecyclerView = view.findViewById(R.id.goalscorerList)
        val cardsList: RecyclerView = view.findViewById(R.id.cardsList)
        val substitutionsList: RecyclerView = view.findViewById(R.id.substitutionsList)
        val lineupList: RecyclerView = view.findViewById(R.id.lineupList)
        val statisticsList: RecyclerView = view.findViewById(R.id.statisticsList)
    }
}
