package com.galfinilhami.destinasipariwisata1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var destinasiAdapter: DestinasiAdapter
    private lateinit var destinasiList: MutableList<Destinasi>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Firestore
        db = Firebase.firestore

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView)
        fabAdd = findViewById(R.id.fabAdd)

        // Setup RecyclerView
        destinasiList = mutableListOf()
        destinasiAdapter = DestinasiAdapter(destinasiList) { destinasi ->
            showOptionsDialog(destinasi)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = destinasiAdapter

        // Setup FloatingActionButton
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditDestinasiActivity::class.java)
            startActivity(intent)
        }

        // Load data from Firebase
        loadDestinasi()
    }

    override fun onResume() {
        super.onResume()
        loadDestinasi()
    }

    private fun loadDestinasi() {
        db.collection("destinasi")
            .get()
            .addOnSuccessListener { documents ->
                destinasiList.clear()
                for (document in documents) {
                    val destinasi = document.toObject(Destinasi::class.java)
                    destinasi.id = document.id
                    destinasiList.add(destinasi)
                }
                destinasiAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error loading data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showOptionsDialog(destinasi: Destinasi) {
        val options = arrayOf("Edit", "Delete", "View Details")
        AlertDialog.Builder(this)
            .setTitle("Options for ${destinasi.nama}")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> editDestinasi(destinasi)
                    1 -> deleteDestinasi(destinasi)
                    2 -> viewDetails(destinasi)
                }
            }
            .show()
    }

    private fun editDestinasi(destinasi: Destinasi) {
        val intent = Intent(this, AddEditDestinasiActivity::class.java)
        intent.putExtra("destinasi_id", destinasi.id)
        intent.putExtra("destinasi_nama", destinasi.nama)
        intent.putExtra("destinasi_lokasi", destinasi.lokasi)
        intent.putExtra("destinasi_deskripsi", destinasi.deskripsi)
        intent.putExtra("destinasi_rating", destinasi.rating)
        intent.putExtra("destinasi_harga", destinasi.hargaTiket)
        startActivity(intent)
    }

    private fun deleteDestinasi(destinasi: Destinasi) {
        AlertDialog.Builder(this)
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete ${destinasi.nama}?")
            .setPositiveButton("Delete") { _, _ ->
                db.collection("destinasi").document(destinasi.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Destinasi deleted successfully", Toast.LENGTH_SHORT).show()
                        loadDestinasi()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error deleting destinasi: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun viewDetails(destinasi: Destinasi) {
        val intent = Intent(this, DetailDestinasiActivity::class.java)
        intent.putExtra("destinasi_id", destinasi.id)
        intent.putExtra("destinasi_nama", destinasi.nama)
        intent.putExtra("destinasi_lokasi", destinasi.lokasi)
        intent.putExtra("destinasi_deskripsi", destinasi.deskripsi)
        intent.putExtra("destinasi_rating", destinasi.rating)
        intent.putExtra("destinasi_harga", destinasi.hargaTiket)
        startActivity(intent)
    }
}