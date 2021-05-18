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
                    CommonUses(name = "Plastic Cup", image = R.drawable.plastic_cup),
                    CommonUses(name = "Plastic Bottle", image = R.drawable.plastic_bottle),
                    CommonUses(name = "Oil Bottle", image = R.drawable.oil_bottle),
                    CommonUses(name = "Soft Drink", image = R.drawable.soft_drink),
                )

            ),
            Content(
                name = "HDPE",
                type = "Can Recyclable widely",
                description = "Properties : Stiff and hard wearing hard to break down in sunlight",
                commonUses = listOf(
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                )

            ),
            Content(
                name = "PVC",
                type = "Often Not Recyclable",
                description = "Properties : Can be rigin or soft via plasticizers used in construction, healthcare, electronic",
                commonUses = listOf(
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                )

            ),
            Content(
                name = "LDPE",
                type = "Not Recyclable",
                description = "Properties : Lightweight, low-cost, versatile fails under mechanical and thermal stress",
                commonUses = listOf(
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                )

            ),
            Content(
                name = "PP",
                type = "Often Not Recyclable",
                description = "Properties : Tough and resistant; effective barrier against water and chemicals",                commonUses = listOf(
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image =R.drawable.plastic_cup ,),
                )

            ),
            Content(
                name = "PS",
                description = "Properties : Lightweight; structurally weak easy dispersed",
                commonUses = listOf(
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                )

            ),
            Content(
                name = "OTHERS",
                type = "Not Recyclable",
                description = "Properties : Diverse in nature with various properties",
                commonUses = listOf(
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                    CommonUses(name = "", image = R.drawable.plastic_cup,),
                )

            ),
        ))
        return listContent
    }
}