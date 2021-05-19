package com.bingo.gobin.data.model

import java.io.Serializable

data class Driver(
    var address: String = "",
    var email: String = "",
    var jml_pickup:String = "",
    var latitude:String = "",
    var longitude:String = "",
    var name:String = "",
    var phone:String = "",
): Serializable