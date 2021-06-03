package com.bingo.gobin.data.content

import com.bingo.gobin.R

object DataContent {
    fun getContent() : List<Content> {
        val listContent = arrayListOf<Content>()
        listContent.addAll(
            listOf(
            Content(
                name = "PETE",
                type = "Can Recyclable widely",
                description = "Properties : Clear Strong and Lightweight",
                commonUses = listOf(
                    CommonUses(name = "Scissor", image = R.drawable.pete1),
                    CommonUses(name = "Plastic Bottle", image = R.drawable.pete2),
                    CommonUses(name = "Soft Drink", image = R.drawable.pete3),
                    CommonUses(name = "Oil Bottle", image = R.drawable.pete4),
                )

            ),
            Content(
                name = "HDPE",
                type = "Can Recyclable widely",
                description = "Properties : Stiff and hard wearing hard to break down in sunlight",
                commonUses = listOf(
                    CommonUses(name = "Shampoo", image = R.drawable.hdpe1),
                    CommonUses(name = "Spray Bottle", image = R.drawable.hdpe2),
                    CommonUses(name = "Soap Bottle", image = R.drawable.hdpe3),
                    CommonUses(name = "Oil Motor Bottle", image = R.drawable.hdpe4),
                )

            ),
            Content(
                name = "PVC",
                type = "Often Not Recyclable",
                description = "Properties : Can be rigin or soft via plasticizers used in construction, healthcare, electronic",
                commonUses = listOf(
                    CommonUses(name = "Pipe PVC", image = R.drawable.pvc1),
                    CommonUses(name = "Water Hose", image = R.drawable.pvc2),

                )

            ),
            Content(
                name = "LDPE",
                type = "Not Recyclable",
                description = "Properties : Lightweight, low-cost, versatile fails under mechanical and thermal stress",
                commonUses = listOf(
                    CommonUses(name = "Plastic Bag", image = R.drawable.ldpe1),
                    CommonUses(name = "Plastic Bag", image = R.drawable.ldpe2),
                    CommonUses(name = "Plastic Bag", image = R.drawable.ldpe3),
                    CommonUses(name = "Plastic Bag", image = R.drawable.ldpe4),
                )

            ),
            Content(
                name = "PP",
                type = "Often Not Recyclable",
                description = "Properties : Tough and resistant; effective barrier against water and chemicals",                commonUses = listOf(
                    CommonUses(name = "Rope", image = R.drawable.pp1),
                    CommonUses(name = "Plastic", image = R.drawable.pp2),
                    CommonUses(name = "Food Container", image = R.drawable.pp3),
                    CommonUses(name = "Diapers", image =R.drawable.pp3),
                )

            ),
            Content(
                name = "PS",
                type = "Not Recyclable",
                description = "Properties : Lightweight; structurally weak easy dispersed",
                commonUses = listOf(
                    CommonUses(name = "Food container", image = R.drawable.ps1),
                    CommonUses(name = "Coffee Cup", image = R.drawable.ps2),
                )

            ),
            Content(
                name = "OTHERS",
                type = "Not Recyclable",
                description = "Properties : Diverse in nature with various properties",
                commonUses = listOf(
                    CommonUses(name = "CD/DVD", image = R.drawable.other1),
                    CommonUses(name = "Plastic Eye glasses", image = R.drawable.other2),
                )

            ),
        ))
        return listContent
    }
}