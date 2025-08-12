package com.galfinilhami.destinasipariwisata1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*

class DetailDestinasiActivity : AppCompatActivity() {

    private lateinit var tvNama: TextView
    private lateinit var tvLokasi: TextView
    private lateinit var tvDeskripsi: TextView
    private lateinit var tvHarga: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvRating: TextView
    private lateinit var btnDirection: Button
    private lateinit var btnShare: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_destinasi)

        initViews()
        loadData()
        setupListeners()
    }

    private fun initViews() {
        tvNama = findViewById(R.id.tvNama)
        tvLokasi = findViewById(R.id.tvLokasi)
        tvDeskripsi = findViewById(R.id.tvDeskripsi)
        tvHarga = findViewById(R.id.tvHarga)
        ratingBar = findViewById(R.id.ratingBar)
        tvRating = findViewById(R.id.tvRating)
        btnDirection = findViewById(R.id.btnDirection)
        btnShare = findViewById(R.id.btnShare)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun loadData() {
        val nama = intent.getStringExtra("destinasi_nama") ?: ""
        val lokasi = intent.getStringExtra("destinasi_lokasi") ?: ""
        val deskripsi = intent.getStringExtra("destinasi_deskripsi") ?: ""
        val rating = intent.getFloatExtra("destinasi_rating", 0.0f)
        val harga = intent.getLongExtra("destinasi_harga", 0)

        title = nama
        tvNama.text = nama
        tvLokasi.text = "📍 $lokasi"
        tvDeskripsi.text = deskripsi
        ratingBar.rating = rating
        tvRating.text = "(${String.format("%.1f", rating)})"

        // Format harga dalam Rupiah
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvHarga.text = "Harga Tiket: ${formatter.format(harga)}"
    }

    private fun setupListeners() {
        btnDirection.setOnClickListener {
            openMaps()
        }

        btnShare.setOnClickListener {
            shareDestinasi()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun openMaps() {
        val lokasi = intent.getStringExtra("destinasi_lokasi") ?: ""
        val uri = Uri.parse("geo:0,0?q=$lokasi")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Fallback to web browser
            val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$lokasi")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            startActivity(webIntent)
        }
    }

    private fun shareDestinasi() {
        val nama = intent.getStringExtra("destinasi_nama") ?: ""
        val lokasi = intent.getStringExtra("destinasi_lokasi") ?: ""
        val deskripsi = intent.getStringExtra("destinasi_deskripsi") ?: ""
        val rating = intent.getFloatExtra("destinasi_rating", 0.0f)
        val harga = intent.getLongExtra("destinasi_harga", 0)

        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        val shareText = """
            🏖️ Destinasi Wisata Recommended!
            
            📍 $nama
            🗺️ Lokasi: $lokasi
            ⭐ Rating: ${String.format("%.1f", rating)}/5
            💰 Harga Tiket: ${formatter.format(harga)}
            
            📝 Deskripsi:
            $deskripsi
            
            #DestinasiWisata #TravelIndonesia
        """.trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Bagikan destinasi melalui:"))
    }
}