package com.shows.franmaric

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doAfterTextChanged
import com.shows.franmaric.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setEnabled(false)
        binding.loginButton.setOnClickListener{
            login()
        }

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
        binding.loginButton.setEnabled(enabled)
        if(enabled){
            binding.loginButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.button_enabled))
            binding.loginButton.setTextColor(0x52368C)
        }else{
            binding.loginButton.setBackgroundTintList(this.getResources().getColorStateList(R.color.button_disabled))
            binding.loginButton.setTextColor(0xFFFFFF)
        }
    }

    private fun login() {
        val intent = Intent(this, WelcomeActivity::class.java)
        val name = binding.emailField.text.toString().split("@").first()
        intent.putExtra("name", name)
        startActivity(intent)
    }

    private fun isValidInput(email: String?, password: String?): Boolean {
        if(email!=null && password!=null && isEmailValid(email) && password.length >= 6)
            return true
        return false
    }

    //not gonna lie I found it on stackoverflow
    private fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}