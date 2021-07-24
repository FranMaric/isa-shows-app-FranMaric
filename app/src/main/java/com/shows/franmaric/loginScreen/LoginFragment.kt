package com.shows.franmaric.loginScreen

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shows.franmaric.R
import com.shows.franmaric.databinding.FragmentLoginBinding

const val MIN_PASSWORD_LENGTH = 6

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    val args: LoginFragmentArgs by navArgs()

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

        checkRememberMe()

        initRegisterButton()

        initLoginButton()

        initInputs()
    }

    private fun initRegisterButton() {
        if(args.afterRegister){
            binding.registerButton.isVisible = false
            binding.loginTextView.text = "Registration successful!"
        }
        binding.registerButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToRegister()
            findNavController().navigate(action)
        }
    }

    private fun checkRememberMe() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        if (sharedPref.getBoolean(getString(R.string.prefs_remember_me), false)) {
            val action = LoginFragmentDirections.actionLoginToShows()
            findNavController().navigate(action)
        }
    }

    private fun initLoginButton() {
        binding.loginButton.setEnabled(false)
        binding.loginButton.setOnClickListener {
            login()
        }
    }

    private fun initInputs() {
        binding.emailField.doAfterTextChanged { email ->
            val password = binding.passwordField.text.toString()
            if (isValidInput(email.toString(), password)) {
                setButtonEnabled(true)
            } else if (binding.loginButton.isEnabled) {
                setButtonEnabled(false)
            }
        }
        binding.passwordField.doAfterTextChanged { password ->
            val email = binding.emailField.text.toString()
            if (isValidInput(email, password.toString())) {
                setButtonEnabled(true)
            } else if (binding.loginButton.isEnabled) {
                setButtonEnabled(false)
            }
        }
    }

    private fun setButtonEnabled(enabled: Boolean) {
        binding.loginButton.apply {
            setEnabled(enabled)
            if (enabled) {
                setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.button_enabled
                    )
                )
                setTextColor(Color.parseColor("#52368C"))
            } else {
                setBackgroundTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.button_disabled
                    )
                )
                setTextColor(Color.WHITE)
            }
        }
    }

    private fun login() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.prefs_email), binding.emailField.text.toString())
            putBoolean(getString(R.string.prefs_remember_me), binding.rememberMeCheckBox.isChecked)
            apply()
        }

        val action = LoginFragmentDirections.actionLoginToShows()
        findNavController().navigate(action)
    }

    private fun isValidInput(email: String?, password: String?) =
        email != null && password != null && isEmailValid(email) && password.length >= MIN_PASSWORD_LENGTH

    //not gonna lie I found it on stackoverflow
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}