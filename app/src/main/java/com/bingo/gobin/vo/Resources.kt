package com.bingo.gobin.vo

sealed class Resources<T>(var data:T?=null, var message:String?=""){
    class Success<T>(data: T?) : Resources<T>(data)
    class Error<T>(message: String?) : Resources<T>(message = message)
    class Loading<T> : Resources<T>()
}