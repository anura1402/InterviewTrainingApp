package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentMarathonBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

class MarathonFragment : Fragment() {
    private var _binding: FragmentMarathonBinding? = null
    private val binding: FragmentMarathonBinding
        get() = _binding ?: throw RuntimeException("FragmentMarathonBinding == null")
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarathonBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun parseArgs() {
        requireArguments().getInt(KEY_COUNT)?.let {
            count = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvInfo.text = requireActivity().getString(
            R.string.marathon_text,
            count.toString()
        )
        binding.buttonStartTest.setOnClickListener {
            launchQuestionFragment(Theme.ALL, Mode.MARATHON)
        }
    }

    private fun launchQuestionFragment(theme: Theme, mode: Mode) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(theme, mode))
            .addToBackStack(QuestionFragment.NAME)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "MarathonFragment"
        const val KEY_COUNT = "count"
        fun newInstance(countOfQuestions:Int): MarathonFragment {
            return MarathonFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_COUNT, countOfQuestions)
                }
            }
        }
    }
}