package com.bingo.gobin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bingo.gobin.data.model.User
import com.bingo.gobin.data.repository.MainRepositoryImpl
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AuthViewModel : ViewModel() {
    private val repositoryImpl by lazy { MainRepositoryImpl() }
    val _latlng = MutableLiveData<LatLng>().apply { value = null }
    val last_latlng = MutableLiveData<LatLng>().apply { value = null }


    fun register(email: String, password:String, user: User): LiveData<out Boolean> {
        return repositoryImpl.register(email, password, user)
    }

    fun login(email: String, password: String): LiveData<out Boolean> {
        return repositoryImpl.login(email, password)
    }
}