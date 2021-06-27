package com.xiangaoole.android.wanandroid.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.constant.Constant
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.ext.getPref
import com.xiangaoole.android.wanandroid.http.exception.ErrorCode
import com.xiangaoole.android.wanandroid.http.exception.ExceptionHandler
import com.xiangaoole.android.wanandroid.model.UserInfoBody
import kotlinx.coroutines.launch
import timber.log.Timber

class WanAndroidViewModel(
    private val repository: WanAndroidRepository,
    application: Application
) : AndroidViewModel(application) {
    val isLogin: LiveData<Boolean> get() = _isLogin
    private val _isLogin = MutableLiveData<Boolean>(false)

    val username: LiveData<String> get() = _username
    private val _username = MutableLiveData<String>("")

    val userDataInfo: LiveData<UserInfoBody?> get() = _userDataInfo
    private val _userDataInfo = MutableLiveData<UserInfoBody?>()

    /**
     * 异常信息，可能是网络连接问题等
     */
    val error: LiveData<String?> get() = _error
    private val _error = MutableLiveData<String?>()

    val logoutSuccess: LiveData<Boolean?> get() = _logoutSuccess
    private val _logoutSuccess = MutableLiveData<Boolean?>()

    init {
        loadLoginData()
    }

    fun loadLoginData() {
        with(getApplication<Application>()) {
            val isLogin = getPref(Constant.LOGIN_KEY, false)
            val username = getPref(Constant.USERNAME_KEY, "")
            val token = getPref(Constant.TOKEN_KEY, "")
            if (isLogin) {

            }
            _isLogin.value = isLogin
            _username.value = username
            Timber.d("isLogin: $isLogin, username: $username")
        }
    }

    private fun updateUserDataInfo(isLogin: Boolean) {
        viewModelScope.launch {
            try {
                val result = repository.getUserInfo()
                if (result.errorCode != ErrorCode.SUCCESS) {
                    _error.value = result.errorMsg
                } else {
                    _userDataInfo.value = result.data
                }
            } catch (e: Exception) {
                _error.value = ExceptionHandler.handle(e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _logoutSuccess.value = true
                loadLoginData()
            } catch (e: Exception) {
                _error.value = ExceptionHandler.handle(e)
                _logoutSuccess.value = false
            }
        }
    }

    fun logoutSuccessDone() {
        _logoutSuccess.value = null
    }

    fun errorDone() {
        _error.value = null
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