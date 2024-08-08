package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentThemesBinding

class ThemesFragment : Fragment() {
    private var _binding: FragmentThemesBinding? = null
    private val binding: FragmentThemesBinding
        get() = _binding ?: throw RuntimeException("FragmentThemesBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonJava.setOnClickListener {
            launchLearningFragment()
        }
        binding.buttonKotlin.setOnClickListener {
            launchLearningFragment()
        }
        binding.buttonSQL.setOnClickListener {
            launchLearningFragment()
        }
        binding.buttonAndroid.setOnClickListener {
            launchLearningFragment()
        }
        binding.buttonThread.setOnClickListener {
            launchLearningFragment()
        }
        binding.buttonBase.setOnClickListener {
            launchLearningFragment()
        }
    }

    private fun launchLearningFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, LearningFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "ThemesFragment"
        fun newInstance(): ThemesFragment {
            return ThemesFragment()
        }
    }
}