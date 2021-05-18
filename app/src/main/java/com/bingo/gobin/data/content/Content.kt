package com.bingo.gobin.data.content

data class Content(
    var name: String = "",
    var commonUses: List<CommonUses> = listOf(),
    var type: String = "",
    var description: String = ""
    )
