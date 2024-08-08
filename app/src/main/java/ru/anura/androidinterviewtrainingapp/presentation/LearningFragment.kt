package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.databinding.FragmentCertainThemeBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentMarathonBinding

class LearningFragment : Fragment() {
    private var _binding: FragmentCertainThemeBinding? = null
    private val binding: FragmentCertainThemeBinding
        get() = _binding ?: throw RuntimeException("FragmentCertainThemeBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertainThemeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "LearningFragment"
        fun newInstance(): LearningFragment {
            return LearningFragment()
        }
    }
}