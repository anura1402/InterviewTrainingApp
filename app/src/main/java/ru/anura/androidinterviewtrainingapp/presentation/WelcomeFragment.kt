package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.interviewButton.setOnClickListener {
            launchQuestionFragment()
        }
        binding.themeButton.setOnClickListener {
            launchThemesFragment()
        }
        binding.mistakeButton.setOnClickListener {
            launchQuestionFragment()
        }
        binding.favButton.setOnClickListener {
            launchQuestionFragment()
        }
        binding.marathonButton.setOnClickListener {
            launchMarathonFragment()
        }
    }

    private fun launchThemesFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ThemesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun launchQuestionFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun launchMarathonFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MarathonFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}