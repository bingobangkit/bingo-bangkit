package com.bingo.gobin.data.model

import java.io.Serializable

data class Type(
    val id: String = "",
    val name: String="",
    val price: Int=0
): Serializable