package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentQuestionsBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.Test
import ru.anura.androidinterviewtrainingapp.domain.entity.TestResult
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import ru.anura.androidinterviewtrainingapp.presentation.adapters.OptionsAdapter

class QuestionFragment : Fragment() {

    private lateinit var theme: Theme
    private lateinit var mode: Mode
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
    private var test: Test = Test(0, emptyList())

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
        requireArguments().getParcelable<Mode>(KEY_MODE)?.let {
            mode = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.startTest(mode)
        observeViewModel()

    }

    private fun setupRecyclerView() {
        binding.rvOptions.layoutManager = LinearLayoutManager(context)
        optionsAdapter = OptionsAdapter()
        // Установка адаптера для RecyclerView
        binding.rvOptions.adapter = optionsAdapter
    }

    private fun observeViewModel() {
        viewModel.test.observe(viewLifecycleOwner) { it ->
            test = it
            if (test.countOfQuestions != 0) {
                updateUI()
            } else {
                launchNothingContainer()
            }
        }
        viewModel.answerResults.observe(viewLifecycleOwner) { it ->
            optionsAdapter.answerResults = it
            answerResults = it
        }
        viewModel.explanation.observe(viewLifecycleOwner) {
            if (answerResults[answerResults.size-1] == false) {
                binding.explanationTv.isVisible = true
                binding.explanationTv.text = it
            }

        }
        viewModel.testResult.observe(viewLifecycleOwner) { testResult ->
            launchResultFragment(testResult)
        }
    }

    private fun updateUI() {
        var currentIndex = 0
        createTextViews(test.countOfQuestions)
        displayQuestion(test, 0)

        viewModel.clickedTextViewId.observe(viewLifecycleOwner) {
            displayQuestion(test, it)
            currentIndex = it
        }
        binding.buttonNextQuestion.setOnClickListener {
            viewModel.onButtonClicked(currentIndex)
        }
        viewModel.clickedButtonOnQuestionId.observe(viewLifecycleOwner) {
            if (it < test.countOfQuestions - 1) {
                highlightTextView(it + 1)
                displayQuestion(test, it + 1)
                currentIndex++
            } else {
                viewModel.finishTest()
            }
        }
        binding.favTv.setOnClickListener {
            viewModel.isFavTextViewClicked(currentIndex)
        }
        viewModel.clickedFavTextView.observe(viewLifecycleOwner) {
            if (binding.favTv.text == getString(R.string.add_to_fav)) {
                viewModel.changeFavList(it, true)
                updateFavTextView(true)
            } else {
                viewModel.changeFavList(it, false)
                updateFavTextView(false)
            }

        }
    }

    private fun updateFavTextView(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favTv.text = getString(R.string.delete_from_fav)
            val drawable =
                ContextCompat.getDrawable(requireActivity(), R.drawable.full_star_resized)
            binding.favTv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        } else {
            binding.favTv.text = getString(R.string.add_to_fav)
            val drawable = ContextCompat.getDrawable(requireActivity(), R.drawable.add_star_resized)
            binding.favTv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    private fun highlightTextView(index: Int) {
        val nonNullView = requireNotNull(view)
        val nextTextView =
            nonNullView.findViewById<TextView>(
                binding.container.getChildAt(
                    index
                ).id
            )
        scrollToTextView(nextTextView)
        nextTextView.setBackgroundResource(R.drawable.border_for_tv)
        updateTextViewBackground(index, nextTextView)
    }


    private fun createTextViews(numberOfTextViews: Int) {
        val container: LinearLayout = binding.container
        for (i in 1..numberOfTextViews) {
            val textView = createTextView(i)
            // Добавляем TextView в контейнер
            container.addView(textView)
            if (i == 1) pastTextView = textView
        }
    }

    private fun createTextView(index: Int): TextView {
        val textView = TextView(requireActivity().application)
        textView.apply {
            id = View.generateViewId()
            text = "$index"
            textSize = 14f
            setPadding(24, 16, 24, 16)
            layoutParams = createTextViewLayoutParams()
            setBackgroundResource(if (index == 1) R.drawable.border_for_tv else R.color.question_number_background)
            setOnClickListener {
                viewModel.onTextViewClicked(index - 1)
                highlightTextView(index - 1)
            }
        }
        return textView
    }

    private fun createTextViewLayoutParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(12, 16, 12, 16) }
    }

    private fun updateTextViewBackground(index: Int, textView: TextView) {
        when (answerResults[previousId]) {
            true -> pastTextView.setBackgroundResource(R.drawable.border_for_tv_correct)
            false -> pastTextView.setBackgroundResource(R.drawable.border_for_tv_wrong)
            else -> pastTextView.setBackgroundResource(R.color.question_number_background)
        }
        previousId = index
        pastTextView = textView
    }

    private fun displayQuestion(test: Test, numberOfQuestion: Int) {
        binding.explanationTv.isVisible = false
        if (answerResults[numberOfQuestion] == false) {
            binding.explanationTv.isVisible = true
        }
        scrollToQuestionPosition(0, 0)
        binding.questionText.text = test.questions[numberOfQuestion].text
        val imageName = "example"
        //val imageName = test.questions[numberOfQuestion-1].image
        val resourceId = resources.getIdentifier(
            imageName,
            "drawable",
            requireActivity().packageName
        )
        binding.questionImage.setImageResource(resourceId)

        setOptionsAdapterSettings(numberOfQuestion, test)

        viewModel.favList.observe(viewLifecycleOwner) {
            if (viewModel.favList.value?.get(numberOfQuestion) == true) {
                updateFavTextView(true)
            } else {
                updateFavTextView(false)
            }
        }

        binding.buttonNextQuestion.isVisible = true
        if (numberOfQuestion == test.countOfQuestions - 1) {
            binding.buttonNextQuestion.text = "Завершить"
        }

    }

    private fun setOptionsAdapterSettings(numberOfQuestion: Int, test: Test) {
        // Получаем сохраненную позицию для текущего вопроса из ViewModel.
        // Если для этого вопроса еще не был выбран ответ, устанавливаем позицию как NO_POSITION.
        val savedPositions: Set<Int> =
            viewModel.getSelectedOptionForQuestion(numberOfQuestion)
                ?: setOf(RecyclerView.NO_POSITION)
        optionsAdapter.apply {
            items = (test.questions[numberOfQuestion].options)
            // Устанавливаем сохраненную позицию в адаптере, чтобы выделить ранее выбранный ответ.
            setSelectedPosition(savedPositions, numberOfQuestion)
            // Устанавливаем идентификатор текущего вопроса в адаптере, чтобы привязать действия к этому вопросу.
            currentQuestionId = numberOfQuestion
            // Определяем, можно ли выбирать опцию для текущего вопроса.
            // Если ответ уже был выбран ранее (isOptionSelectedForQuestion вернет true), выбор блокируется.
            setIsOptionSelectable(!viewModel.isOptionSelectedForQuestion(numberOfQuestion))
            setOnItemClickListener { selectedPosition ->
                // Сохранение выбранной позиции в ViewModel
                viewModel.selectOptionForQuestion(numberOfQuestion, selectedPosition)
                viewModel.checkAnswer(
                    numberOfQuestion,
                    optionsAdapter.items[selectedPosition],
                    test.questions[numberOfQuestion].answer
                )
                scrollToQuestionPosition(
                    0,
                    binding.scrollQuestionsFragment.getChildAt(0).height
                )
            }
        }
    }


    private fun scrollToQuestionPosition(x: Int, y: Int) {
        binding.scrollQuestionsFragment.post {
            binding.scrollQuestionsFragment.scrollTo(x, y)
        }
    }

    private fun scrollToTextView(textView: TextView) {
        binding.scrollContainer.post {
            binding.scrollContainer.scrollTo(textView.left, 0)
        }
    }

    private fun launchResultFragment(testResult: TestResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ResultFragment.newInstance(testResult))
            .addToBackStack(null)
            .commit()
    }

    private fun launchNothingContainer() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, NothingFragment.newInstance())
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
        private const val KEY_MODE = "mode"
        fun newInstance(theme: Theme, mode: Mode): QuestionFragment {
            return QuestionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_THEME, theme)
                    putSerializable(KEY_MODE, mode)
                }
            }
        }
    }
}