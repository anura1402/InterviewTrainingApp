package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentThemesBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentTheoryBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import ru.anura.androidinterviewtrainingapp.presentation.adapters.TheoryAdapter

class TheoryFragment : Fragment() {
    private var _binding: FragmentTheoryBinding? = null
    private lateinit var theory: Theory
    private var theoryList: List<Theory> = listOf()
    private val binding: FragmentTheoryBinding
        get() = _binding ?: throw RuntimeException("FragmentTheoryBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTheoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.theoryName.text = theory.name
        binding.theoryText.text = theory.text
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun parseArgs() {
        requireArguments().getParcelable<Theory>(KEY_THEORY)?.let {
             theory = it
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_THEORY = "theory"
        const val NAME = "TheoryFragment"
        fun newInstance(theory: Theory): TheoryFragment {
            return TheoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_THEORY, theory)
                }
            }
        }
    }
}