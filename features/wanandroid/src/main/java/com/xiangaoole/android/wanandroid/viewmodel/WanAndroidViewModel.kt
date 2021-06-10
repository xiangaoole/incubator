package com.xiangaoole.android.wanandroid.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.Constant
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.ext.getPref
import timber.log.Timber

class WanAndroidViewModel(
    private val repository: WanAndroidRepository,
    application: Application
) : AndroidViewModel(application) {
    val isLogin: LiveData<Boolean> get() = _isLogin
    private val _isLogin = MutableLiveData<Boolean>(false)

    val username: LiveData<String> get() = _username
    private val _username = MutableLiveData<String>("")


    init {
        loadLoginData()
    }

    fun loadLoginData() {
        with(getApplication<Application>()) {
            val isLogin = getPref(Constant.LOGIN_KEY, false)
            val username = getPref(Constant.USERNAME_KEY, "")
            val token = getPref(Constant.TOKEN_KEY, "")
            _isLogin.value = isLogin
            _username.value = username
            Timber.d("isLogin: $isLogin, username: $username")
        }
    }

    fun logout() {

    }

    class Factory(
        private val repository: WanAndroidRepository,
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WanAndroidViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WanAndroidViewModel(repository, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}