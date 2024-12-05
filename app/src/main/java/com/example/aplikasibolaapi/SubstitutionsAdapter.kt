package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class SubstitutionsAdapter(private val substitutions: List<Substitution>) : RecyclerView.Adapter<SubstitutionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_substitution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val substitution = substitutions[position]  // Get the substitution object at the current position

        // Set the data from substitution object to the views
        holder.timeTextView.text = substitution.time  // Assuming the substitution data includes 'time'
        holder.substitutionTextView.text = substitution.substitution  // Assuming it includes substitution description
    }

    override fun getItemCount(): Int = substitutions.size  // Return the size of the substitutions list

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Initialize your view elements here (for example, TextViews)
        val timeTextView: TextView = view.findViewById(R.id.substitutionTime)  // Assuming you have a TextView for time
        val substitutionTextView: TextView = view.findViewById(R.id.substitutionText)  // TextView for substitution info
    }
}


