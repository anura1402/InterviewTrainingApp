package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import ru.anura.androidinterviewtrainingapp.R

import ru.anura.androidinterviewtrainingapp.databinding.FragmentResultBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Mode
import ru.anura.androidinterviewtrainingapp.domain.entity.TestResult
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme

class ResultFragment : Fragment() {
    private lateinit var testResult: TestResult
    private lateinit var mode: Mode
    private var _binding: FragmentResultBinding? = null
    private val binding: FragmentResultBinding
        get() = _binding ?: throw RuntimeException("FragmentResultBinding == null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkResult()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    returnToMainMenu()
                }
            })

    }

    private fun checkResult() {
        val goodResult = (testResult.countOfQuestions * 0.85)
        val itsOkay = (testResult.countOfQuestions * 0.6)
        val countText = requireActivity().getString(
            R.string.count_text,
            (testResult.countOfQuestions - testResult.countOfRightAnswers).toString(),
            testResult.countOfQuestions.toString()
        )
        if (testResult.countOfRightAnswers >= goodResult) {
            setBindings(R.drawable.result_success, "Поздравляем!", countText)
        } else if (testResult.countOfRightAnswers >= itsOkay) {
            setBindings(R.drawable.result_middle, "Может быть лучше!", countText)
        } else {
            setBindings(R.drawable.result_fail, "Надо тренироваться!", countText)
        }
    }

    private fun setBindings(resultImageRes: Int, resultText: String, countText: String) {
        if (resultImageRes == R.drawable.result_success) {
            Glide.with(this)
                .asGif()
                .load(resultImageRes)
                .override(900, 1100)
                .into(binding.resultImage)
        } else {
            binding.resultImage.setImageResource(resultImageRes)
        }
        binding.tvResult.text = resultText
        binding.tvCounts.text = countText
        binding.buttonReturn.setOnClickListener {
            returnToMainMenu()
        }
        binding.buttonMistakes.setOnClickListener {
            launchQuestionFragment(Theme.ALL, Mode.MISTAKES)
        }
        binding.buttonAgain.setOnClickListener {
            launchQuestionFragment(Theme.ALL, mode)
        }

        // addOnGlobalLayoutListener - Этот слушатель отслеживает изменения в глобальной раскладке (layout) всех элементов на экране
        binding.scrollView.viewTreeObserver.addOnGlobalLayoutListener {
            _binding?.let { safeBinding ->
                //если scrollView не нужен и элементы вместились в экран:
                if (!safeBinding.scrollView.canScrollVertically(1)) {
                    safeBinding.buttonReturn.visibility = View.VISIBLE
                } else {
                    val view =
                        safeBinding.scrollView.getChildAt(safeBinding.scrollView.childCount - 1)
                    val diff =
                        (view.bottom - (safeBinding.scrollView.height + safeBinding.scrollView.scrollY))

                    if (diff == 0) {
                        // Пользователь достиг нижней части ScrollView
                        safeBinding.buttonReturn.visibility = View.VISIBLE
                        safeBinding.scrollView.scrollTo(
                            0,
                            safeBinding.scrollView.getChildAt(0).height
                        )
                    } else {
                        // Пользователь еще не достиг нижней части
                        safeBinding.buttonReturn.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun parseArgs() {
        requireArguments().getParcelable<TestResult>(KEY_RESULT)?.let {
            testResult = it
        }
        requireArguments().getParcelable<Mode>(KEY_MODE)?.let {
            mode = it
        }
    }

    private fun returnToMainMenu() {
        requireActivity().supportFragmentManager.popBackStack(
            QuestionFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )

    }

    private fun launchQuestionFragment(theme: Theme, mode: Mode) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, QuestionFragment.newInstance(theme, mode))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "ResultFragment"
        private const val KEY_RESULT = "result"
        private const val KEY_MODE = "mode"
        fun newInstance(testResult: TestResult, mode: Mode): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, testResult)
                    putSerializable(KEY_MODE, mode)
                }
            }
        }
    }
}