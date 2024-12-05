package com.example.aplikasibolaapi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.*
import android.content.Intent
import org.json.JSONException
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val apiKey = "2a6517e0f771e5d8642a617241a6f281b44c631e337a4471be584c7c0e1d069d"
    private val client = OkHttpClient()
    private lateinit var recyclerView: RecyclerView
    private val gson = Gson()

    // Stack to keep track of the views and the associated IDs
    private val viewStack = Stack<Pair<String, Int>>() // Pair of view type and associated ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchCountries()

        // Register OnBackPressedCallback for handling back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewStack.isNotEmpty()) {
                    val (lastView, lastId) = viewStack.pop()
                    when (lastView) {
                        "MatchDetails" -> fetchTeams(lastId)
                        "Teams" -> fetchLeagues(lastId)
                        "Leagues" -> fetchCountries()
                        else -> {
                            isEnabled = false
                            onBackPressedDispatcher.onBackPressed()
                        }
                    }
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun fetchCountries() {
        val url = "https://apiv3.apifootball.com/?action=get_countries&APIkey=$apiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch countries", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val countries = gson.fromJson(body, Array<Country>::class.java)

                runOnUiThread {
                    recyclerView.adapter = CountryAdapter(countries) { countryId ->
                        fetchLeagues(countryId)
                        viewStack.push(Pair("Leagues", countryId)) // Push current view and countryId
                    }
                }
            }
        })
    }

    private fun fetchLeagues(countryId: Int) {
        val url = "https://apiv3.apifootball.com/?action=get_leagues&country_id=$countryId&APIkey=$apiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch leagues", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val leagues = gson.fromJson(body, Array<League>::class.java)

                // Loop through the leagues and print league id and name in the terminal
                leagues.forEach { league ->
                    Log.d("League Info", "League ID: ${league.league_id}, Name: ${league.league_name}")
                }

                runOnUiThread {
                    recyclerView.adapter = LeagueAdapter(leagues) { leagueId ->
                        fetchTeams(leagueId)
                        viewStack.push(Pair("Teams", countryId)) // Push current view and leagueId
                    }
                }
            }
        })
    }


    private fun fetchTeams(leagueId: Int) {
        val url = "https://apiv3.apifootball.com/?action=get_teams&league_id=$leagueId&APIkey=$apiKey"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch teams", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val teams = gson.fromJson(body, Array<Team>::class.java)

                runOnUiThread {
                    recyclerView.adapter = TeamAdapter(teams) { teamKey ->
                        fetchMatchDetails(teamKey, leagueId)  // Pass leagueId here
                        viewStack.push(Pair("MatchDetails", leagueId))  // Pass teamKey instead of team_id
                    }
                }
            }
        })
    }

    private fun fetchMatchDetails(teamKey: Int, leagueId: Int) {
        val from_date = "2000-01-01"  // Use a very old date to cover all matches
        val to_date = "2100-01-01"    // Use a far future date to cover all matches

        val url = "https://apiv3.apifootball.com/?action=get_events&from=$from_date&to=$to_date&league_id=$leagueId&APIkey=$apiKey"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch match details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if (body == null) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "No match data found.", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val matches = gson.fromJson(body, Array<Match>::class.java)

                    val filteredMatches = matches.filter {
                        it.match_hometeam_id == teamKey.toString() || it.match_awayteam_id == teamKey.toString()
                    }.map { match ->
                        Match(
                            match_id = match.match_id,
                            match_hometeam_id = match.match_hometeam_id,
                            match_hometeam_name = match.match_hometeam_name,
                            match_awayteam_id = match.match_awayteam_id,
                            match_awayteam_name = match.match_awayteam_name,
                            match_date = match.match_date,
                            match_time = match.match_time,
                            team_home_badge = match.team_home_badge,
                            team_away_badge = match.team_away_badge
                        )
                    }.sortedByDescending { it.match_date }

                    // Log match ids to the Logcat
                    for (match in filteredMatches) {
                        Log.d("MatchDetails", "Match ID: ${match.match_id}") // Log each match_id
                    }

                    // Set the RecyclerView adapter to show the filtered matches
                    runOnUiThread {
                        recyclerView.adapter = MatchAdapter(filteredMatches.toTypedArray()) { match ->
                            // Inside onClick for match item, navigate to MatchDetailActivity
                            val intent = Intent(this@MainActivity, MatchDetailActivity::class.java)
                            intent.putExtra("matchId", match.match_id)  // Pass the matchId
                            intent.putExtra("leagueId", leagueId)  // Pass the leagueId if needed
                            startActivity(intent)
                        }
                    }
                } catch (e: JSONException) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Error parsing match data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
/*
    private fun showMatchDetails(match: Match) {
        val matchTitle = "${match.match_hometeam_name} vs ${match.match_awayteam_name}"
        Toast.makeText(this, "Match details: $matchTitle", Toast.LENGTH_LONG).show()
    }
*/




// Data Classes
data class Country(val country_id: Int, val country_name: String, val country_logo: String)

data class League(val league_id: Int, val league_name: String, val league_logo: String)

data class Team(val team_key: Int, val team_name: String, val team_badge: String)

data class Match(
    val match_id: String,
    val match_hometeam_id: String,
    val match_hometeam_name: String,
    val match_awayteam_id: String,
    val match_awayteam_name: String,
    val match_date: String,
    val match_time: String,
    val team_home_badge: String,
    val team_away_badge: String
)

data class MatchDetail(
    val match_id: String,
    val country_name: String,
    val league_name: String,
    val match_date: String,
    val match_status: String,
    val match_hometeam_name: String,
    val match_hometeam_score: String,
    val match_awayteam_name: String,
    val match_awayteam_score: String,
    val goalscorer: List<Goalscorer>,
    val cards: List<Card>,
    val substitutions: Substitutions,
    val lineup: Lineup,
    val statistics: List<Statistic>,
    val team_home_badge: String,
    val team_away_badge: String
)

data class Goalscorer(
    val time: String,
    val home_scorer: String,
    val away_scorer: String,
    val score: String
)

data class Card(
    val time: String,
    val home_fault: String,
    val away_fault: String,
    val card: String
)

data class Substitutions(
    val home: List<Substitution>,
    val away: List<Substitution>
)

data class Substitution(
    val time: String,
    val substitution: String
)

data class Lineup(
    val starting_lineups: List<Player>,
    val substitutes: List<Player>,
    val coach: List<Player>,
    val missing_players: List<Player>,
    val home: TeamLineup,
    val away: TeamLineup
)

data class TeamLineup(
    val starting_lineups: List<Player>,
    val substitutes: List<Player>,
    val coach: List<Player>,
    val missing_players: List<Player>
)

data class Player(
    val lineup_player: String,
    val lineup_number: String,
    val lineup_position: String,
    val player_key: String
)


data class Statistic(
    val type: String,
    val home: String,
    val away: String
)