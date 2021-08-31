package com.shows.franmaric.splashScreen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shows.franmaric.PREFS_REMEMBER_ME_KEY
import com.shows.franmaric.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

const val WAIT_TIME = 2000L

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animate()

        Handler(Looper.getMainLooper()).postDelayed({
            checkRememberMe()
            },
            WAIT_TIME
        )
    }

    private fun animate() {
        with(binding.triangleImageView) {
            translationY = -800f
            animate()
                .translationY(0f)
                .setDuration(1000)
                .setInterpolator(BounceInterpolator())
                .start()
        }
        with(binding.showsTextView) {
            scaleX = 0f
            scaleY = 0f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }

    private fun checkRememberMe() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        if(sharedPref == null) {
            navigateToLogin()
            return
        }

        if (sharedPref.getBoolean(PREFS_REMEMBER_ME_KEY, false))
            navigateToShows()
        else
            navigateToLogin()
    }

    private fun navigateToLogin() {
        val action = SplashFragmentDirections.actionSplashToLogin()
        findNavController().navigate(action)
    }

    private fun navigateToShows() {
        val action = SplashFragmentDirections.actionSplashToShows()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}