package com.shows.franmaric.loginScreen

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shows.franmaric.MainActivity
import com.shows.franmaric.PREFS_REMEMBER_ME_KEY
import com.shows.franmaric.R
import com.shows.franmaric.databinding.FragmentLoginBinding
import com.shows.franmaric.extensions.hasInternetConnection
import com.shows.franmaric.repository.RepositoryViewModelFactory

const val MIN_PASSWORD_LENGTH = 6

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    val args: LoginFragmentArgs by navArgs()

    private val viewModel: LoginViewModel by viewModels {
        RepositoryViewModelFactory((requireActivity() as MainActivity).showsRepository)
    }

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

        initLoginObserver()
    }

    private fun initLoginObserver() {
        viewModel.getLoginResultLiveData().observe(requireActivity()) { isLoginSuccessful ->
            val prefs = activity?.getPreferences(Context.MODE_PRIVATE)

            if (isLoginSuccessful && prefs != null) {
                with(prefs.edit()){
                    putBoolean(PREFS_REMEMBER_ME_KEY, binding.rememberMeCheckBox.isChecked)
                    apply()
                }

                val action = LoginFragmentDirections.actionLoginToShows()
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, getString(R.string.login_failed_please_try_again), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initRegisterButton() {
        if (args.afterRegister) {
            binding.registerButton.isVisible = false
            binding.loginTextView.text = getString(R.string.registration_successful)
        }
        binding.registerButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginToRegister()
            findNavController().navigate(action)
        }
    }

    private fun checkRememberMe() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        if (sharedPref.getBoolean(PREFS_REMEMBER_ME_KEY, false)) {
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
                setTextColor(resources.getColor(R.color.purple))
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
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        if(prefs == null) {
            Toast.makeText(
                context,
                getString(R.string.login_failed_please_try_again),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val email = binding.emailField.text.toString()
        val password = binding.passwordField.text.toString()
        viewModel.login(
            email,
            password,
            prefs,
            requireContext().hasInternetConnection()
        )
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