package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
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
                textView.setBackgroundResource(R.drawable.border_for_tv)
            }
            // Настраиваем TextView
            textView.id = View.generateViewId()
            textView.text = "$i"
            textView.textSize = 14f // размер текста
            if (i != 1) textView.setBackgroundResource(R.color.question_number_background)
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
            var currentIndex = 0
            for (i in 1..test.countOfQuestions) {
                val index = i - 1
                val textView =
                    nonNullView.findViewById<TextView>(binding.container.getChildAt(index).id)
                textView?.setOnClickListener {
                    //выставление цвета на выбранный вопрос в TextView
                    textView.setBackgroundResource(R.drawable.border_for_tv)
                    setQuestionSettings(test, index)

                    changeTextViewBackground(index, textView)

                    currentIndex = index
                }
                binding.buttonNextQuestion.setOnClickListener {
                    binding.buttonNextQuestion.isVisible = false
                    if (currentIndex < test.countOfQuestions - 1) {
                        currentIndex++
                        if (currentIndex==test.countOfQuestions-1) binding.buttonNextQuestion.text = "Завершить"
                        val nextTextView =
                            nonNullView.findViewById<TextView>(
                                binding.container.getChildAt(
                                    currentIndex
                                ).id
                            )
                        nextTextView.setBackgroundResource(R.drawable.border_for_tv)

                        setQuestionSettings(test, currentIndex)


                        changeTextViewBackground(currentIndex, nextTextView)
                        Log.d("QuestionFragment", "currentIndex: $currentIndex")

                    } else{
                        launchResultFragment()
                    }
                    binding.scrollQuestionsFragment.scrollTo(0, 0)

                }
            }
        }
    }

    private fun changeTextViewBackground(index: Int, textView: TextView) {
        //выставление цвета на отвеченный вопрос в TextView
        if (answerResults[previousId] == true) {
            pastTextView.setBackgroundResource(R.drawable.border_for_tv_correct)
        } else if (answerResults[previousId] == false) {
            pastTextView.setBackgroundResource(R.drawable.border_for_tv_wrong)
        } else {
            pastTextView.setBackgroundResource(R.color.question_number_background)
        }
        previousId = index
        pastTextView = textView
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


        // Получаем сохраненную позицию для текущего вопроса из ViewModel.
        // Если для этого вопроса еще не был выбран ответ, устанавливаем позицию как NO_POSITION.
        val savedPosition =
            viewModel.getSelectedOptionForQuestion(numberOfQuestion) ?: RecyclerView.NO_POSITION
        // Устанавливаем сохраненную позицию в адаптере, чтобы выделить ранее выбранный ответ.
        optionsAdapter.setSelectedPosition(savedPosition)
        // Устанавливаем идентификатор текущего вопроса в адаптере, чтобы привязать действия к этому вопросу.
        optionsAdapter.currentQuestionId = numberOfQuestion
        // Определяем, можно ли выбирать опцию для текущего вопроса.
        // Если ответ уже был выбран ранее (isOptionSelectedForQuestion вернет true), выбор блокируется.
        optionsAdapter.setIsOptionSelectable(!viewModel.isOptionSelectedForQuestion(numberOfQuestion))

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
            binding.scrollQuestionsFragment.post {
                binding.scrollQuestionsFragment.scrollTo(0, binding.scrollQuestionsFragment.getChildAt(0).height)
            }

            binding.buttonNextQuestion.isVisible = true
        }
    }

    private fun launchResultFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ResultFragment.newInstance())
            .addToBackStack(null)
            .commit()
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