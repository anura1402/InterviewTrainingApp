package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentQuestionsBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentThemesBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

class QuestionFragment : Fragment() {

    private lateinit var theme: Theme
    //private lateinit var optionsAdapter: OptionsAdapter
    private var _binding: FragmentQuestionsBinding? = null
    private val binding: FragmentQuestionsBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionsBinding == null")

    private val viewModelByFactory by lazy {
        QuestionViewModelFactory(theme, requireActivity().application)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelByFactory)[QuestionViewModel::class.java]
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
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Theme>(KEY_THEME)?.let {
            theme = it
        }
    }
    private fun createTextViews(numberOfTextViews: Int) {
        val container: LinearLayout = binding.container
        for (i in 1..numberOfTextViews) {
            // Создаем новый TextView
            val textView = TextView(requireActivity().application)
            // Настраиваем TextView
            textView.id = View.generateViewId()

            textView.text = "$i"
            textView.textSize = 12f // размер текста
            textView.setPadding(16, 16, 16, 16) // отступы
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            // Добавляем TextView в контейнер
            container.addView(textView)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupRecyclerView()
        createTextViews(3)
        observeViewModel()
    }

//    private fun setupRecyclerView() {
//        val nonNullView = requireNotNull(view)
//        val rvQuestions = nonNullView.findViewById<RecyclerView>(R.id.rvOptions)
//        with(rvQuestions) {
//            optionsAdapter = OptionsAdapter()
//            // Установка адаптера для RecyclerView
//            this?.adapter = optionsAdapter
//
//        }
//    }

    private fun observeViewModel() {
        val nonNullView = requireNotNull(view)
        viewModel.test.observe(viewLifecycleOwner) { test ->
            for (i in 1..test.countOfQuestions) {
                val textView =
                    view?.findViewById<TextView>(binding.container.getChildAt(i - 1).id)
//                textView.setOnClickListener {
//                    binding.questionText.text = test.questions[i-1].text
//                    Glide.with(requireActivity())
//                        .load("https://img.desktopwallpapers.ru/animals/pics/wide/1920x1200/ba0b90228c8dd06d24bb6cfb3777f5e5.jpg")
//                        //.load(test.questions[i-1].image)
//                        .into(binding.questionImage)
//                   // optionsAdapter.submitList(test.questions[i-1].options)
//                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_THEME = "theme"
        const val NAME = "QuestionFragment"
        fun newInstance(theme: Theme): QuestionFragment {
            return QuestionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_THEME, theme)
                }
            }
        }
    }
}