package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentWelcomeBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

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
        with(binding){
            interviewButton.setOnClickListener {
                launchQuestionFragment(Theme.ALL)
            }
            themeButton.setOnClickListener {
                launchThemesFragment()
            }
            mistakeButton.setOnClickListener {
                launchQuestionFragment(Theme.ALL)
            }
            favButton.setOnClickListener {
                launchQuestionFragment(Theme.ALL)
            }
            marathonButton.setOnClickListener {
                launchMarathonFragment()
            }
        }

    }

    private fun launchThemesFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ThemesFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun launchQuestionFragment(theme: Theme) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(theme))
            .addToBackStack(QuestionFragment.NAME)
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