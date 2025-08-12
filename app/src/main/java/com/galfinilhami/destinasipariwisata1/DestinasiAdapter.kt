package com.galfinilhami.destinasipariwisata1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.*

class DestinasiAdapter(
    private val destinasiList: List<Destinasi>,
    private val onItemClick: (Destinasi) -> Unit
) : RecyclerView.Adapter<DestinasiAdapter.DestinasiViewHolder>() {

    class DestinasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvLokasi: TextView = itemView.findViewById(R.id.tvLokasi)
        val tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvHarga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinasiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destinasi, parent, false)
        return DestinasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinasiViewHolder, position: Int) {
        val destinasi = destinasiList[position]

        holder.tvNama.text = destinasi.nama
        holder.tvLokasi.text = "📍 ${destinasi.lokasi}"
        holder.tvDeskripsi.text = destinasi.deskripsi
        holder.ratingBar.rating = destinasi.rating
        holder.tvRating.text = "(${String.format("%.1f", destinasi.rating)})"

        // Format harga dalam Rupiah
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        holder.tvHarga.text = formatter.format(destinasi.hargaTiket)

        holder.itemView.setOnClickListener {
            onItemClick(destinasi)
        }
    }

    override fun getItemCount(): Int = destinasiList.size
}