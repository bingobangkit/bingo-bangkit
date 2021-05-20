package com.bingo.gobin.vo

sealed class Resources<T>(var data:T?=null, var message:String?=""){
    class Success<T>(data: T?) : Resources<T>(data)
    class Error<T>(data: T?,message: String?) : Resources<T>(data,message = message)
    class Loading<T> : Resources<T>()
}