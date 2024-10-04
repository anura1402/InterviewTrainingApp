package ru.anura.androidinterviewtrainingapp.presentation

import android.app.Application
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
import ru.anura.androidinterviewtrainingapp.di.DaggerApplicationComponent
import ru.anura.androidinterviewtrainingapp.di.ThemeModule
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory
import ru.anura.androidinterviewtrainingapp.presentation.adapters.TheoryAdapter
import javax.inject.Inject

class TheoryListFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: TheoryListViewModel
    private var _binding: FragmentCertainThemeBinding? = null
    private val component by lazy {
        val themeModule = ThemeModule(theme) // Используем тему из аргументов фрагмента
        DaggerApplicationComponent.factory()
            .create(requireActivity().application as Application, themeModule)
    }

    private lateinit var theme: Theme
    private lateinit var theoryAdapter: TheoryAdapter
    private var theoryList: List<Theory> = listOf()
    private val binding: FragmentCertainThemeBinding
        get() = _binding ?: throw RuntimeException("FragmentCertainThemeBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        Log.d("Dagger", "THEORY_LIST theme: $theme ")
        component.inject(this)
    }
    private fun parseArgs() {
        requireArguments().getParcelable<Theme>(KEY_THEME)?.let {
            theme = it
        }
        Log.d("Dagger", "Parsed theme: $theme")
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
        viewModel = ViewModelProvider(this, viewModelFactory)[TheoryListViewModel::class.java]
        Log.d("Dagger", "Parsed theme: $theme")
        setupRecyclerView()
        binding.themeName.text = theme.toString()
        observeViewModel()
        setupOnClickListener()
        binding.buttonStart.setOnClickListener {
            launchQuestionFragment(theme, Mode.THEMES)
        }
    }

    private fun observeViewModel() {
        viewModel.theoryList.observe(viewLifecycleOwner) {
            theoryList = it
            theoryAdapter.theoryList = it
            Log.d("LearningFragment", "theoryList $theoryList")
        }
    }
    private fun setupRecyclerView() {
        binding.rvThemes.layoutManager = LinearLayoutManager(context)
        theoryAdapter = TheoryAdapter()
        // Установка адаптера для RecyclerView
        binding.rvThemes.adapter = theoryAdapter
    }

    private fun setupOnClickListener() {
        theoryAdapter.onTheoryItemClickListener = { _, position ->
            launchTheoryFragment(theoryList[position])
        }
    }

    private fun launchQuestionFragment(theme: Theme, mode: Mode) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(theme, mode))
            .addToBackStack(null)
            .commit()
    }

    private fun launchTheoryFragment(theory: Theory) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, TheoryFragment.newInstance(theory))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_THEME = "theme"
        const val NAME = "LearningFragment"
        fun newInstance(theme: Theme): TheoryListFragment {
            return TheoryListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_THEME, theme)
                }
            }
        }
    }
}