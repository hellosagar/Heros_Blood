package com.nishant.herosblood.ui

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.nishant.herosblood.R
import com.nishant.herosblood.databinding.ActivityLoginBinding
import com.nishant.herosblood.util.Resource
import com.nishant.herosblood.viewmodels.AuthViewModel

class LoginActivity : AppCompatActivity(), View.OnKeyListener, View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        binding.layoutBackground.setOnClickListener(this)
        binding.txtLogo.setOnClickListener(this)
        binding.txtSignIn.setOnClickListener(this)
        binding.txtNewUser.setOnClickListener(this)
        binding.txtCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            loginUser(
                binding.edtUsernameEditText.text.toString(),
                binding.edtPasswordEditText.text.toString()
            )
        }

        viewModel.loginStatus.observe(this, { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoadingBar()
                }
                is Resource.Success -> {
                    hideLoadingBar()
                    startActivity(Intent(this, UserDashboardActivity::class.java))
                }
                is Resource.Error -> {
                    hideLoadingBar()
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun showLoadingBar() {
        binding.layoutBackground.alpha = 0.4F
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingBar() {
        binding.layoutBackground.alpha = 1F
        binding.progressBar.visibility = View.GONE
    }

    private fun loginUser(email: String, password: String) {
        viewModel.loginUser(email, password)
    }

    override fun onKey(p0: View?, i: Int, keyEvent: KeyEvent?): Boolean {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent?.action == KeyEvent.ACTION_DOWN) {
            viewModel.loginUser(
                binding.edtUsernameEditText.text.toString(),
                binding.edtPasswordEditText.text.toString()
            )
        }
        return false
    }

    override fun onClick(view: View?) {
        view?.let { it ->
            if (it.id == R.id.layout_background || it.id == R.id.txt_logo || it.id == R.id.txt_signIn || it.id == R.id.txt_newUser) {
                currentFocus.let { focus ->
                    val inputMethodManager =
                        ContextCompat.getSystemService(this, InputMethodManager::class.java)
                    inputMethodManager?.hideSoftInputFromWindow(focus?.windowToken, 0)
                }
            }

        }
    }
}