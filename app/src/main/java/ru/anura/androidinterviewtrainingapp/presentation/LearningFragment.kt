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
import ru.anura.androidinterviewtrainingapp.databinding.FragmentCertainThemeBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentMarathonBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import ru.anura.androidinterviewtrainingapp.presentation.adapters.OptionsAdapter
import ru.anura.androidinterviewtrainingapp.presentation.adapters.TheoryAdapter

class LearningFragment : Fragment() {
    private var _binding: FragmentCertainThemeBinding? = null
    private lateinit var theme: Theme
    private lateinit var theoryAdapter: TheoryAdapter
    private var theoryList: List<Theory> = listOf()
    private val binding: FragmentCertainThemeBinding
        get() = _binding ?: throw RuntimeException("FragmentCertainThemeBinding == null")

    private val viewModelByFactory by lazy {
        TheoryViewModelFactory(theme, requireActivity().application)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelByFactory)[TheoryViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertainThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.themeName.text = theme.toString()
        observeViewModel()
        setupOnClickListener()
        binding.buttonStart.setOnClickListener {
            launchQuestionFragment(theme, Mode.THEMES)
        }
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Theme>(KEY_THEME)?.let {
            theme = it
        }
    }
    private fun setupRecyclerView() {
        binding.rvThemes.layoutManager = LinearLayoutManager(context)
        theoryAdapter = TheoryAdapter()
        // Установка адаптера для RecyclerView
        binding.rvThemes.adapter = theoryAdapter
    }
    private fun setupOnClickListener() {
        theoryAdapter.onTheoryItemClickListener = {
            //launchQuestionFragment(theme, Mode.THEMES)
        }
    }
    private fun launchQuestionFragment(theme: Theme, mode: Mode){
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(theme, mode))
            .addToBackStack(null)
            .commit()
    }

    private fun observeViewModel(){
        viewModel.theoryList.observe(viewLifecycleOwner){
            theoryList = it
            theoryAdapter.theoryList = it
            Log.d("LearningFragment","theoryList $theoryList")
        }
    }
//    private fun setTheoryOptions(){
//        theoryAdapter.theoryList = theoryList
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_THEME = "theme"
        const val NAME = "LearningFragment"
        fun newInstance(theme: Theme): LearningFragment {
            return LearningFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_THEME, theme)
                }
            }
        }
    }
}