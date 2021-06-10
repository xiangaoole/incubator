package com.xiangaoole.android.wanandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.xiangaoole.android.wanandroid.Injection
import com.xiangaoole.android.wanandroid.R
import com.xiangaoole.android.wanandroid.databinding.ActivityLoginBinding
import com.xiangaoole.android.wanandroid.ext.showToast
import com.xiangaoole.android.wanandroid.model.LoginData
import com.xiangaoole.android.wanandroid.util.Preference
import com.xiangaoole.android.wanandroid.viewmodel.LoginDataViewModel

/**
 * Activity for Login
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginDataViewModel by viewModels {
        val repository = Injection.provideWanAndroidRepository(this)
        LoginDataViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { login() }

        // toolbar
        binding.includedToolbar.run {
            setSupportActionBar(toolbar)
            title = getString(R.string.login)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // 错误信息
        viewModel.error.observe(this) {
            if (!it.isNullOrEmpty()) {
                showToast(it)
            }
        }
        // 用户信息
        viewModel.data.observe(this) {
            if (it != null) {
                loginSuccess(it)
                setResult(RESULT_OK)
                finish() // 登录成功后退出
            }
        }
    }


    /**
     * 登陆
     */
    private fun login() = with(binding) {
        if (validate()) {
            viewModel.login(etUsername.text.toString(), etPassword.text.toString())
        }
    }

    private fun validate(): Boolean = with(binding) {
        var valid = true
        val username: String = etUsername.text.toString()
        val password: String = etPassword.text.toString()

        if (username.isEmpty()) {
            valid = false
            etUsername.error = getString(R.string.username_not_empty)
        }

        if (password.isEmpty()) {
            valid = false
            etPassword.error = getString(R.string.password_not_empty)
        }
        valid
    }

    private fun loginSuccess(data: LoginData) {
        showToast(getString(R.string.login_success))
        Preference.saveLoginData(data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}