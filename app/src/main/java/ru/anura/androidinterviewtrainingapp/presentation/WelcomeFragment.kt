package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentWelcomeBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

class WelcomeFragment : Fragment() {
    private lateinit var viewModel: WelcomeViewModel
    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding
        get() = _binding ?: throw RuntimeException("FragmentWelcomeBinding == null")
    private var count:Int = 0

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
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]
        viewModel.getCountOfQuestions()
        viewModel.getCorrectAnsweredCount()
        viewModel.countOfQuestions.observe(viewLifecycleOwner) { countOfQuestions ->
            count = countOfQuestions
            viewModel.correctAnsweredCount.observe(viewLifecycleOwner) { correctAnsweredCount ->
                binding.countOfQuestions.text = requireActivity().getString(
                    R.string.countOfQuestions,
                    correctAnsweredCount.toString(),
                    countOfQuestions.toString()
                )
                val progress = (correctAnsweredCount.toFloat() / countOfQuestions * 100).toInt()
                binding.progressBar1.progress = progress
            }
        }
        viewModel.getCountOfPassedThemes()
        viewModel.countOfPassedThemes.observe(viewLifecycleOwner) {
            binding.countOfThemes.text = requireActivity().getString(
                R.string.countOfThemes, it.toString()
            )
            val progress = (it.toFloat() / 6 * 100).toInt()
            binding.progressBar.progress = progress
        }
        with(binding) {
            interviewButton.setOnClickListener {
                launchQuestionFragment(Theme.ALL, Mode.INTERVIEW)
            }
            themeButton.setOnClickListener {
                launchThemesFragment()
            }
            mistakeButton.setOnClickListener {
                launchQuestionFragment(Theme.ALL, Mode.MISTAKES)
            }
            favButton.setOnClickListener {
                launchQuestionFragment(Theme.ALL, Mode.FAVORITES)
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

    private fun launchQuestionFragment(theme: Theme, mode: Mode) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(theme, mode))
            .addToBackStack(QuestionFragment.NAME)
            .commit()
    }

    private fun launchMarathonFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MarathonFragment.newInstance(count))
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}