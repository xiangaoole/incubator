package com.xiangaoole.android.wanandroid.viewmodel

import androidx.lifecycle.*
import com.xiangaoole.android.wanandroid.data.WanAndroidRepository
import com.xiangaoole.android.wanandroid.http.exception.ExceptionHandler
import com.xiangaoole.android.wanandroid.model.LoginData
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * 存储用户身份信息，是否已登录
 */
class LoginDataViewModel(
    private val repository: WanAndroidRepository
) : ViewModel() {

    /**
     * 正在登录中
     */
    val loading: LiveData<Boolean> get() = _loading
    private val _loading = MutableLiveData(false)

    /**
     * 登录中的异常信息，可能是网络，也可能是密码不匹配等
     */
    val error: LiveData<String?> get() = _error
    private val _error = MutableLiveData<String?>()

    /**
     * 登录成功后保存的用户信息，退出登录后为 null
     */
    val data: LiveData<LoginData?> get() = _data
    private val _data = MutableLiveData<LoginData?>(null)

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.login(username, password)
                if (result.errorCode != 0) {
                    _error.value = result.errorMsg
                } else {
                    _data.value = result.data
                }
            } catch (e: Exception) {
                _error.value = ExceptionHandler.handle(e)
            } finally {
                _loading.value = false
            }
        }
    }

    class Factory(
        private val repository: WanAndroidRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginDataViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginDataViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}