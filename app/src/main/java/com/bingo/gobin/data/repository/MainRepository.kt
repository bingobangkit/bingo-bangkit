package com.bingo.gobin.data.repository

import com.bingo.gobin.data.model.Driver
import com.bingo.gobin.data.model.Order
import com.bingo.gobin.data.model.Type

interface MainRepository {
    fun setOrder(order: Order)
    fun getOrder() : Order
    fun getListOrder() : List<Order>
    fun getType() : Type
    fun getDriver() : Driver
}