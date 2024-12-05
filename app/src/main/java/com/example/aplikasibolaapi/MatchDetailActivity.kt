package com.example.aplikasibolaapi

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import java.io.IOException

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var homeBadge: ImageView
    private lateinit var awayBadge: ImageView
    private lateinit var homeTeamName: TextView
    private lateinit var awayTeamName: TextView
    private lateinit var matchScore: TextView
    private lateinit var goalscorerList: RecyclerView
    private lateinit var cardsList: RecyclerView
    private lateinit var substitutionsList: RecyclerView
    private lateinit var lineupList: RecyclerView
    private lateinit var statisticsList: RecyclerView

    private lateinit var matchId: String
    private lateinit var leagueId: String
    private val gson = Gson()

    private val apiKey = "2a6517e0f771e5d8642a617241a6f281b44c631e337a4471be584c7c0e1d069d"
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        // Initialize Views
        homeBadge = findViewById(R.id.teamHomeBadge)
        awayBadge = findViewById(R.id.teamAwayBadge)
        homeTeamName = findViewById(R.id.homeTeamName)
        awayTeamName = findViewById(R.id.awayTeamName)
        matchScore = findViewById(R.id.matchScore)
        goalscorerList = findViewById(R.id.goalscorerList)
        cardsList = findViewById(R.id.cardsList)
        substitutionsList = findViewById(R.id.substitutionsList)
        lineupList = findViewById(R.id.lineupList)
        statisticsList = findViewById(R.id.statisticsList)

        goalscorerList.layoutManager = LinearLayoutManager(this)
        cardsList.layoutManager = LinearLayoutManager(this)
        substitutionsList.layoutManager = LinearLayoutManager(this)
        lineupList.layoutManager = LinearLayoutManager(this)
        statisticsList.layoutManager = LinearLayoutManager(this)

        matchId = intent.getStringExtra("matchId") ?: ""
        leagueId = intent.getIntExtra("leagueId", -1).toString()

        fetchMatchDetails(matchId, leagueId)
    }

    private fun fetchMatchDetails(matchId: String, leagueId: String) {
        val from_date = "2000-01-01"
        val to_date = "2100-01-01"

        val url =
            "https://apiv3.apifootball.com/?action=get_events&from=$from_date&to=$to_date&league_id=$leagueId&match_id=$matchId&APIkey=$apiKey"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MatchDetailActivity, "Failed to fetch match details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if (body == null) {
                    runOnUiThread {
                        Toast.makeText(this@MatchDetailActivity, "No match data found.", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val matchDetails: List<MatchDetail> = gson.fromJson(body, Array<MatchDetail>::class.java).toList()

                    runOnUiThread {
                        val details = matchDetails[0]

                        // Update Home Team Data
                        homeTeamName.text = details.match_hometeam_name
                        Glide.with(this@MatchDetailActivity)
                            .load(details.team_home_badge)
                            .placeholder(R.drawable.placeholder_image)
                            .into(homeBadge)

                        // Update Away Team Data
                        awayTeamName.text = details.match_awayteam_name
                        Glide.with(this@MatchDetailActivity)
                            .load(details.team_away_badge)
                            .placeholder(R.drawable.placeholder_image)
                            .into(awayBadge)

                        // Update Match Score
                        matchScore.text = "${details.match_hometeam_score} - ${details.match_awayteam_score}"

                        // Set adapters for other lists
                        goalscorerList.adapter = GoalscorerAdapter(details.goalscorer)
                        cardsList.adapter = CardsAdapter(details.cards)
                        substitutionsList.adapter = SubstitutionsAdapter(
                            details.substitutions.home + details.substitutions.away
                        )
                        lineupList.adapter = LineupAdapter(
                            details.lineup.home.starting_lineups + details.lineup.away.starting_lineups
                        )
                        statisticsList.adapter = StatisticsAdapter(details.statistics)
                    }
                } catch (e: JSONException) {
                    runOnUiThread {
                        Toast.makeText(this@MatchDetailActivity, "Error parsing match data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
