package com.galfinilhami.destinasipariwisata1

import com.google.firebase.firestore.PropertyName

data class Destinasi(
    @PropertyName("id") var id: String = "",
    @PropertyName("nama") var nama: String = "",
    @PropertyName("lokasi") var lokasi: String = "",
    @PropertyName("deskripsi") var deskripsi: String = "",
    @PropertyName("rating") var rating: Float = 0.0f,
    @PropertyName("harga_tiket") var hargaTiket: Long = 0,
    @PropertyName("kategori") var kategori: String = "",
    @PropertyName("created_at") var createdAt: com.google.firebase.Timestamp? = null
) {
    constructor() : this("", "", "", "", 0.0f, 0, "", null)
}