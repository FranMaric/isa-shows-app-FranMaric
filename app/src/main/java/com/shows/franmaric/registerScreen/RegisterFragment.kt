package com.shows.franmaric.registerScreen

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shows.franmaric.R
import com.shows.franmaric.databinding.FragmentRegisterBinding

const val MIN_PASSWORD_LENGTH = 6

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

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

    private fun initRegisterObserver() {
        viewModel.getRegistrationResultLiveData()
            .observe(requireActivity()) { isRegisterSuccessful ->
                if (isRegisterSuccessful) {
                    val action = RegisterFragmentDirections.actionRegisterToLogin().setAfterRegister(true)
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, "Not successful registration!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            val passwordConfirmation = binding.passwordConfirmationField.text.toString()

            viewModel.register(email, password, passwordConfirmation)
        }
    }

    private fun initInputs() {
        binding.emailField.doAfterTextChanged { email ->
            val password = binding.passwordField.text.toString()
            val passwordConfirmation = binding.passwordConfirmationField.text.toString()
            if (isValidInput(email.toString(), password, passwordConfirmation)) {
                setButtonEnabled(true)
            } else if (binding.registerButton.isEnabled) {
                setButtonEnabled(false)
            }
        }
        binding.passwordField.doAfterTextChanged { password ->
            val email = binding.emailField.text.toString()
            val passwordConfirmation = binding.passwordConfirmationField.text.toString()
            if (isValidInput(email, password.toString(), passwordConfirmation)) {
                setButtonEnabled(true)
            } else if (binding.registerButton.isEnabled) {
                setButtonEnabled(false)
            }
        }
        binding.passwordConfirmationField.doAfterTextChanged { passwordConfirmation ->
            val email = binding.emailField.text.toString()
            val password = binding.passwordField.text.toString()
            if (isValidInput(email, password, passwordConfirmation.toString())) {
                setButtonEnabled(true)
            } else if (binding.registerButton.isEnabled) {
                setButtonEnabled(false)
            }
        }
    }

    private fun setButtonEnabled(enabled: Boolean) {
        binding.registerButton.apply {
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