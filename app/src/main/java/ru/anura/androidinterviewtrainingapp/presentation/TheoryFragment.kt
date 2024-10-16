package ru.anura.androidinterviewtrainingapp.presentation

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.LineBackgroundSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.R
import ru.anura.androidinterviewtrainingapp.databinding.FragmentTheoryBinding
import ru.anura.androidinterviewtrainingapp.domain.entity.Theory

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
        val result = splitText(theory.text)
        binding.theoryText.setText( result, TextView.BufferType.SPANNABLE)
        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }
    private fun splitText(input: String): SpannableStringBuilder {
        val spannableText = SpannableStringBuilder()
        val str = mutableListOf<String>()
        var remainingText = input
        if (!remainingText.contains("<pre><code>")) {
            spannableText.append(remainingText)
        }
        while (remainingText.contains("<pre><code>")) {
            // Извлекаем текст между тегами
            val beforeTag = remainingText.substringBefore("<pre><code>") + "\n"
            val betweenTags = remainingText.substringAfter("<pre><code>").substringBefore("</code></pre>") +"\n"
            spannableText.append(beforeTag)
            spannableText.append(highlightCode(betweenTags))
            // Добавляем извлеченный текст в результат
            str.add(beforeTag)
            str.add(betweenTags)

            // Обновляем оставшийся текст
            remainingText = remainingText.substringAfter("</code></pre>")
        }
        return spannableText
    }

    private fun highlightCode(
        code: String
    ): SpannableStringBuilder {
        val spannableText = SpannableStringBuilder()

        // Добавляем код на Java с фоном и monospace шрифтом
        val startCode = spannableText.length
        spannableText.append(code)
        spannableText.setSpan(
            CodeBackgroundSpan(
                ContextCompat.getColor(requireContext(), R.color.code_background), 20
            ),  // Прямоугольный фон с padding
            startCode, spannableText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableText.setSpan(
            TypefaceSpan("monospace"),
            startCode,
            spannableText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableText
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
    inner class CodeBackgroundSpan(private val backgroundColor: Int, private val padding: Int) :
        LineBackgroundSpan {
        override fun drawBackground(
            canvas: Canvas, paint: Paint, left: Int, right: Int,
            top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int,
            lineNumber: Int
        ) {
            // Сохраняем старые параметры кисти
            val oldColor = paint.color

            // Устанавливаем цвет фона
            paint.color = backgroundColor

            // Рисуем прямоугольник вокруг строки
            canvas.drawRect(
                left.toFloat() - padding,    // Левее начала текста на padding
                top.toFloat() - padding / 2, // Выше строки на половину отступа
                right.toFloat() + padding,   // Правее конца текста на padding
                bottom.toFloat() + padding / 2, // Ниже строки на половину отступа
                paint
            )

            // Восстанавливаем старый цвет кисти
            paint.color = oldColor
        }
    }
}