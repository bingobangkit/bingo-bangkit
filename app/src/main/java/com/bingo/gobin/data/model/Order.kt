package com.bingo.gobin.data.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.io.Serializable


data class Order(
    var id : String = "",
    var id_invoice: String? = "",
    var id_driver: String? = "",
    var id_user: String? = "",
    var address: String? = "",
    var id_type: String? = "",
    var amount: String? = "",
    var latitude: String? = "",
    var longitude: String? = "",
    var status: String? = "",
    var date: String? = "",
    var total_price: String? ="0"
) : Serializable