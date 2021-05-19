package com.bingo.gobin.data.model

import android.os.Parcelable


data class Order(
    val id: String,
    val idInvoice : String,
    val idDriver:String,
    val idUser:String,
    val idType:Type,
    val address:String,
    val amount:Int,
    val price:Int,
    val latitude:String,
    val longitude:String,
    val status:String,
    val date:String
)