package com.shows.franmaric.registerScreen

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
import com.shows.franmaric.MainActivity
import com.shows.franmaric.R
import com.shows.franmaric.databinding.FragmentRegisterBinding
import com.shows.franmaric.extensions.hasInternetConnection
import com.shows.franmaric.loginScreen.LoginViewModel
import com.shows.franmaric.repository.RepositoryViewModelFactory
import com.shows.franmaric.repository.ShowsRepository

const val MIN_PASSWORD_LENGTH = 6

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        RepositoryViewModelFactory((requireActivity() as MainActivity).showsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRegisterObserver()

        initRegisterButton()

        initInputs()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.registerButton.text = if(isLoading) "" else getString(R.string.register)
        binding.loadingIndicator.isVisible = isLoading
        binding.registerButton.isEnabled = !isLoading      // this line shouldn't be here but for some reason loadingIndicator is not visible if loginButton is enabled
    }

    private fun initRegisterObserver() {
        viewModel.getRegistrationResultLiveData()
            .observe(requireActivity()) { isRegisterSuccessful ->
                if (isRegisterSuccessful) {
                    val action = RegisterFragmentDirections.actionRegisterToLogin().setAfterRegister(true)
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, "Not successful registration!", Toast.LENGTH_SHORT)
                        .show()
                    setLoading(false)
                }
            }
    }

    private fun initRegisterButton() {
        binding.registerButton.isEnabled = false
        binding.registerButton.setOnClickListener {
            setLoading(true)
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            val passwordConfirmation = binding.passwordConfirmationField.text.toString()

            viewModel.register(email, password, passwordConfirmation, requireContext().hasInternetConnection())
        }
    }

    private fun initInputs() {
        binding.emailField.doAfterTextChanged { email ->
            val password = binding.passwordField.text.toString()
            val passwordConfirmation = binding.passwordConfirmationField.text.toString()
            if (isValidInput(email.toString(), password, passwordConfirmation)) {
                binding.registerButton.isEnabled = true
            } else if (binding.registerButton.isEnabled) {
                binding.registerButton.isEnabled = false
            }
        }
        binding.passwordField.doAfterTextChanged { password ->
            val email = binding.emailField.text.toString()
            val passwordConfirmation = binding.passwordConfirmationField.text.toString()
            if (isValidInput(email, password.toString(), passwordConfirmation)) {
                binding.registerButton.isEnabled = true
            } else if (binding.registerButton.isEnabled) {
                binding.registerButton.isEnabled = false
            }
        }
        binding.passwordConfirmationField.doAfterTextChanged { passwordConfirmation ->
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            if (isValidInput(email, password, passwordConfirmation.toString())) {
                binding.registerButton.isEnabled = true
            } else if (binding.registerButton.isEnabled) {
                binding.registerButton.isEnabled = false
            }
        }
    }

    private fun isValidInput(email: String?, password: String?, passwordConfirmation: String?) =
        email != null && password != null && isEmailValid(email) && password.length >= MIN_PASSWORD_LENGTH && password.equals(
            passwordConfirmation
        )

    //not gonna lie I found it on stackoverflow
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}