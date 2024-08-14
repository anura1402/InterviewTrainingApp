package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentQuestionsBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.presentation.adapters.OptionsAdapter

class QuestionFragment : Fragment() {
    private var tries: MutableList<Int> = mutableListOf()

    private lateinit var theme: Theme

    private lateinit var optionsAdapter: OptionsAdapter

    private var _binding: FragmentQuestionsBinding? = null
    private val binding: FragmentQuestionsBinding
        get() = _binding ?: throw RuntimeException("FragmentQuestionsBinding == null")

    private val viewModelByFactory by lazy {
        QuestionViewModelFactory(theme, requireActivity().application)
    }
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelByFactory)[QuestionViewModel::class.java]
    }

    private var answeredQuestions: MutableList<Boolean> = mutableListOf()
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
            //tries.add(i)
            //Log.d("ClickOption", "tryToAnswer: $tries")
            // Создаем новый TextView
            val textView = TextView(requireActivity().application)
            // Настраиваем TextView
            textView.id = View.generateViewId()

            textView.text = "$i"
            textView.textSize = 12f // размер текста
            textView.setBackgroundResource(R.color.question_number_background)
            textView.setPadding(16, 10, 16, 10) // отступы
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            params.setMargins(12, 16, 12, 16) // отступы
            textView.layoutParams = params
            // Добавляем TextView в контейнер
            container.addView(textView)
            answeredQuestions.add(false)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvOptions.layoutManager = LinearLayoutManager(context)
        optionsAdapter = OptionsAdapter()
        // Установка адаптера для RecyclerView
        binding.rvOptions.adapter = optionsAdapter
    }

    private fun observeViewModel() {
        val nonNullView = requireNotNull(view)
        viewModel.test.observe(viewLifecycleOwner) { test ->
            createTextViews(test.countOfQuestions)
            setQuestionSettings(test, 0)
            for (i in 1..test.countOfQuestions) {
                val textView =
                    nonNullView.findViewById<TextView>(binding.container.getChildAt(i - 1).id)
                textView?.setOnClickListener {
                    setQuestionSettings(test, i - 1)
                }
            }
        }
    }

    private fun setQuestionSettings(test: Test, numberOfQuestion: Int) {
        Log.d("debill", "setQuestionSettings: ${answeredQuestions[numberOfQuestion]}")
        binding.questionText.text = test.questions[numberOfQuestion].text
        val imageName = "example"
        //val imageName = test.questions[numberOfQuestion-1].image
        val resourceId = resources.getIdentifier(
            imageName,
            "drawable",
            requireActivity().packageName
        )
        binding.questionImage.setImageResource(resourceId)
        optionsAdapter.optionsList = (test.questions[numberOfQuestion].options)
        optionsAdapter.answeredQuestions = answeredQuestions
        optionsAdapter.currentAnswerId = numberOfQuestion

        setupOnClickOptionListener(test.questions[numberOfQuestion].answer, numberOfQuestion)
        Log.d("QuestionFragment", "options: ${test.questions[numberOfQuestion].options}")
    }

    private fun setupOnClickOptionListener(answer: String, numberOfQuestion: Int) {
        optionsAdapter.onOptionItemClickListener = {
            //Log.d("QuestionFragment", "From fragment: $it")
            if (it == answer) {
                optionsAdapter.correctAnswer = it
                Log.d("QuestionFragment", "Option is correct")
            } else {
                Log.d("QuestionFragment", "Option is wrong $it $answer")
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