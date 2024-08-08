package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.databinding.FragmentQuestionsBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentThemesBinding

class QuestionFragment : Fragment() {
    private var _binding: FragmentQuestionsBinding? = null
    private val binding: FragmentQuestionsBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionsBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "QuestionFragment"
        fun newInstance(): QuestionFragment {
            return QuestionFragment()
        }
    }
}