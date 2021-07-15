package com.shows.franmaric

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.setBackgroundTintList
import androidx.core.widget.doAfterTextChanged
import com.shows.franmaric.databinding.ActivityLoginBinding
import java.util.regex.Pattern

const val MIN_LENGTH = 6

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initLoginButton()

        initInputs()
    }

    private fun initLoginButton(){
        binding.loginButton.setEnabled(false)
        binding.loginButton.setOnClickListener{
            login()
        }
    }

    private fun initInputs(){
        binding.emailField.doAfterTextChanged { email->
            val password = binding.passwordField.text.toString()
            if(isValidInput(email.toString(), password)){
                setButtonEnabled(true)
            } else if(binding.loginButton.isEnabled){
                setButtonEnabled(false)
            }
        }
        binding.passwordField.doAfterTextChanged { password->
            val email = binding.emailField.text.toString()
            if(isValidInput(email, password.toString())){
                setButtonEnabled(true)
            } else if(binding.loginButton.isEnabled){
                setButtonEnabled(false)
            }
        }
    }

    private fun setButtonEnabled(enabled:Boolean){
        binding.loginButton.apply {
            setEnabled(enabled)
            if(enabled){
                setBackgroundTintList(ContextCompat.getColorStateList(this@LoginActivity, R.color.button_enabled))
                setTextColor(Color.parseColor("#52368C"))
            } else {
                setBackgroundTintList(ContextCompat.getColorStateList(this@LoginActivity, R.color.button_disabled))
                setTextColor(Color.WHITE)
            }
        }
    }

    private fun login() {
        val intent = Intent(this, ShowsActivity::class.java)
        val name = binding.emailField.text.toString().split("@").first()
        intent.putExtra("name", name)
        startActivity(intent)
    }

    private fun isValidInput(email: String?, password: String?) =
         email!=null && password!=null && isEmailValid(email) && password.length >= MIN_LENGTH

    //not gonna lie I found it on stackoverflow
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}