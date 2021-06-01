package com.bingo.gobin.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bingo.gobin.data.model.User
import com.bingo.gobin.data.repository.MainRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.asTask

@ExperimentalCoroutinesApi
class AuthViewModel : ViewModel() {
    private val repositoryImpl by lazy { MainRepositoryImpl() }

    fun register(email: String, password:String, user: User): LiveData<out Boolean> {
        return repositoryImpl.register(email, password, user)
    }

    fun login(email: String, password: String): LiveData<out Boolean> {
        return repositoryImpl.login(email, password)
    }
}