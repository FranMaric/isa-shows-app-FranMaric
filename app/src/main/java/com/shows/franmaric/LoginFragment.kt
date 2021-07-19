package com.shows.franmaric

import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.shows.franmaric.databinding.FragmentLoginBinding

const val MIN_PASSWORD_LENGTH = 6

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.button_enabled))
                setTextColor(Color.parseColor("#52368C"))
            } else {
                setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.button_disabled))
                setTextColor(Color.WHITE)
            }
        }
    }

    private fun login() {
        //TODO: use actions to navigate to ShowsFragment
        /*val intent = Intent(this, ShowsActivity::class.java)
        val name = binding.emailField.text.toString().split("@").first()
        intent.putExtra("name", name)

        startActivity(intent)*/
    }

    private fun isValidInput(email: String?, password: String?) =
         email!=null && password!=null && isEmailValid(email) && password.length >= MIN_PASSWORD_LENGTH

    //not gonna lie I found it on stackoverflow
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}