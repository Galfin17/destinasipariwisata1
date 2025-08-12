package com.galfinilhami.destinasipariwisata1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddEditDestinasiActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etLokasi: EditText
    private lateinit var etDeskripsi: EditText
    private lateinit var etHarga: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var tvRating: TextView
    private lateinit var spinnerKategori: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button

    private lateinit var db: FirebaseFirestore
    private var destinasiId: String? = null
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_destinasi)

        // Initialize Firebase Firestore
        db = Firebase.firestore

        // Initialize views
        initViews()

        // Setup spinner
        setupKategoriSpinner()

        // Check if editing existing destinasi
        checkEditMode()

        // Setup listeners
        setupListeners()
    }

    private fun initViews() {
        etNama = findViewById(R.id.etNama)
        etLokasi = findViewById(R.id.etLokasi)
        etDeskripsi = findViewById(R.id.etDeskripsi)
        etHarga = findViewById(R.id.etHarga)
        ratingBar = findViewById(R.id.ratingBar)
        tvRating = findViewById(R.id.tvRating)
        spinnerKategori = findViewById(R.id.spinnerKategori)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
    }

    private fun setupKategoriSpinner() {
        val categories = arrayOf(
            "Pantai", "Gunung", "Air Terjun", "Danau",
            "Candi", "Museum", "Taman Nasional", "Kota Wisata", "Lainnya"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKategori.adapter = adapter
    }

    private fun checkEditMode() {
        destinasiId = intent.getStringExtra("destinasi_id")

        if (destinasiId != null && destinasiId!!.isNotEmpty()) {
            isEditMode = true
            title = "Edit Destinasi"
            btnSave.text = "Update"

            // Populate fields with existing data
            etNama.setText(intent.getStringExtra("destinasi_nama"))
            etLokasi.setText(intent.getStringExtra("destinasi_lokasi"))
            etDeskripsi.setText(intent.getStringExtra("destinasi_deskripsi"))
            etHarga.setText(intent.getLongExtra("destinasi_harga", 0).toString())
            ratingBar.rating = intent.getFloatExtra("destinasi_rating", 0.0f)
            tvRating.text = String.format("%.1f", ratingBar.rating)
        } else {
            isEditMode = false
            title = "Tambah Destinasi"
            btnSave.text = "Simpan"
        }
    }

    private fun setupListeners() {
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            tvRating.text = String.format("%.1f", rating)
        }

        btnSave.setOnClickListener {
            saveDestinasi()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveDestinasi() {
        val nama = etNama.text.toString().trim()
        val lokasi = etLokasi.text.toString().trim()
        val deskripsi = etDeskripsi.text.toString().trim()
        val hargaStr = etHarga.text.toString().trim()
        val rating = ratingBar.rating
        val kategori = spinnerKategori.selectedItem.toString()

        // Validation
        if (nama.isEmpty()) {
            etNama.error = "Nama destinasi harus diisi"
            return
        }
        if (lokasi.isEmpty()) {
            etLokasi.error = "Lokasi harus diisi"
            return
        }
        if (deskripsi.isEmpty()) {
            etDeskripsi.error = "Deskripsi harus diisi"
            return
        }
        if (hargaStr.isEmpty()) {
            etHarga.error = "Harga tiket harus diisi"
            return
        }

        val harga = try {
            hargaStr.toLong()
        } catch (e: NumberFormatException) {
            etHarga.error = "Harga harus berupa angka"
            return
        }

        val destinasi = mapOf(
            "nama" to nama,
            "lokasi" to lokasi,
            "deskripsi" to deskripsi,
            "rating" to rating,
            "harga_tiket" to harga,
            "kategori" to kategori,
            "created_at" to if (isEditMode) null else Timestamp.now()
        ).filterValues { it != null }

        if (isEditMode) {
            // Update existing destinasi
            db.collection("destinasi").document(destinasiId!!)
                .update(destinasi)
                .addOnSuccessListener {
                    Toast.makeText(this, "Destinasi berhasil diupdate", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // Add new destinasi
            db.collection("destinasi")
                .add(destinasi)
                .addOnSuccessListener {
                    Toast.makeText(this, "Destinasi berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}