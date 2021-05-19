package com.bingo.gobin.data.model

import android.os.Parcelable
import java.io.Serializable


data class Order(
    var id: String ="",
    var idInvoice : String ="",
    var idDriver:String ="",
    var idUser:String ="",
    var idType:Type = Type(),
    var address:String ="",
    var amount:Int =0,
    var price:Int =0,
    var latitude:String ="",
    var longitude:String ="",
    var status:String ="",
    var date:String =""
): Serializable