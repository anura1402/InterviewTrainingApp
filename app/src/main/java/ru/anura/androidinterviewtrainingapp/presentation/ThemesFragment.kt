package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentThemesBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

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
            launchTheoryListFragment(Theme.JAVA)
        }
        binding.buttonKotlin.setOnClickListener {
            launchTheoryListFragment(Theme.KOTLIN)
        }
        binding.buttonSQL.setOnClickListener {
            launchTheoryListFragment(Theme.SQL)
        }
        binding.buttonAndroid.setOnClickListener {
            launchTheoryListFragment(Theme.ANDROID)
        }
        binding.buttonThread.setOnClickListener {
            launchTheoryListFragment(Theme.THREADS)
        }
        binding.buttonBase.setOnClickListener {
            launchTheoryListFragment(Theme.BASE)
        }
    }

    private fun launchTheoryListFragment(theme: Theme) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, TheoryListFragment.newInstance(theme))
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