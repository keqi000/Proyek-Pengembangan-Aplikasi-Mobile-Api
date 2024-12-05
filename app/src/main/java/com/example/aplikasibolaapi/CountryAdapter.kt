package com.example.aplikasibolaapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CountryAdapter(
    private val countries: Array<Country>,
    private val onCountryClick: (Int) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryName: TextView = itemView.findViewById(R.id.itemText)
        val countryLogo: ImageView = itemView.findViewById(R.id.itemImage)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCountryClick(countries[position].country_id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.countryName.text = country.country_name

        // Load country logo from the API
        val logoUrl = country.country_logo // This URL is provided in the API response
        Glide.with(holder.itemView.context)
            .load(logoUrl)
            .placeholder(R.drawable.placeholder_image)  // Placeholder image while loading
            .error(R.drawable.error_image)             // Error image in case of failure
            .into(holder.countryLogo)
    }

    override fun getItemCount() = countries.size
}
