package com.bingo.gobin.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import java.io.Serializable


data class Order(
    var id: String = "",
    var id_invoice: String = "",
    var id_driver: String = "",
    var id_user: String = "",
    var address: String = "",
    var amount_plastic: Int? = null,
    var amount_mixed: Int? = null,
    var price_plastic: Int? = null,
    var price_mixed: Int? = null,
    var latitude: String = "",
    var longitude: String = "",
    var status: String = "",
    var date: String = "",
    var total_price: String =""
) : Serializable