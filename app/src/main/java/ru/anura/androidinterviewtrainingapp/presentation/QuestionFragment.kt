package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentQuestionsBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.presentation.adapters.OptionsAdapter

class QuestionFragment : Fragment() {

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
    private var previousId = 0
    private lateinit var pastTextView: TextView
    private var answerResults: Map<Int, Boolean> = emptyMap()
    private var isAlreadyClicked = false


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
            if (i == 1) {
                pastTextView = textView
            }
            // Настраиваем TextView
            textView.id = View.generateViewId()
            textView.text = "$i"
            textView.textSize = 14f // размер текста
            textView.setBackgroundResource(R.color.question_number_background)
            textView.setPadding(24, 16, 24, 16) // отступы
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
            params.setMargins(12, 16, 12, 16) // отступы
            textView.layoutParams = params
            // Добавляем TextView в контейнер
            container.addView(textView)
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
                    if (answerResults[i - 1] == null) {
                        isAlreadyClicked = false
                    }
                    Log.d("checkAnswer", "answerResults[i - 1]: ${answerResults[i - 1]} isAlreadyClicked: $isAlreadyClicked")
//                    Log.d(
//                        "checkAnswer",
//                        "textView: ${textView.text}  i-1: ${i - 1} previousId: $previousId answerResults: $answerResults answerResults[previousId] ${answerResults[previousId]}"
//                    )
                    if (answerResults[previousId] == true) {
                        pastTextView.setBackgroundResource(R.drawable.border_for_tv_correct)
                    } else if (answerResults[previousId] == false) {
                        pastTextView.setBackgroundResource(R.drawable.border_for_tv_wrong)
                    } else {
                        pastTextView.setBackgroundResource(R.color.question_number_background)
                    }
                    //Log.d("checkAnswer","textView: ${textView.text} previousId: $previousId i-1: ${i-1}")
//                    if(previousId != i-1){
//                        pastTextView.setBackgroundResource(R.color.question_number_background)
//                    }
                    textView.setBackgroundResource(R.drawable.border_for_tv)

                    setQuestionSettings(test, i - 1)

                    previousId = i - 1
                    pastTextView = textView

                }

            }
        }
    }

    private fun setQuestionSettings(test: Test, numberOfQuestion: Int) {
        binding.questionText.text = test.questions[numberOfQuestion].text
        val imageName = "example"
        //val imageName = test.questions[numberOfQuestion-1].image
        val resourceId = resources.getIdentifier(
            imageName,
            "drawable",
            requireActivity().packageName
        )
        binding.questionImage.setImageResource(resourceId)
        optionsAdapter.items = (test.questions[numberOfQuestion].options)


        val savedPosition =
            viewModel.getSelectedOptionForQuestion(numberOfQuestion) ?: RecyclerView.NO_POSITION
        optionsAdapter.setSelectedPosition(savedPosition)
        optionsAdapter.currentQuestionId = numberOfQuestion


        Log.d("checkAnswer", "numberOfQuestion: $numberOfQuestion isAlreadyClicked: $isAlreadyClicked")
        if (!isAlreadyClicked){
            optionsAdapter.setOnItemClickListener { selectedPosition ->
                // Сохранение выбранной позиции в ViewModel
                viewModel.selectOptionForQuestion(numberOfQuestion, selectedPosition)
                viewModel.checkAnswer(
                    numberOfQuestion,
                    optionsAdapter.items[selectedPosition],
                    test.questions[numberOfQuestion].answer
                )
                viewModel.answerResults.observe(viewLifecycleOwner) { it ->
                    optionsAdapter.answerResults = it
                    answerResults = it


                }
            }
            isAlreadyClicked = true
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