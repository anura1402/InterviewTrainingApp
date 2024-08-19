package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.anura.androidinterviewtrainingapp.databinding.FragmentMarathonBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentNothingBinding

class NothingFragment: Fragment() {
    private var _binding: FragmentNothingBinding? = null
    private val binding: FragmentNothingBinding
        get() = _binding ?: throw RuntimeException("FragmentNothingBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNothingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonReturn.setOnClickListener {
            returnBack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    returnBack()
                }
            })
    }

    private fun returnBack() {
        requireActivity().supportFragmentManager.popBackStack(
            QuestionFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }
    companion object {

        const val NAME = "NothingFragment"
        fun newInstance(): NothingFragment {
            return NothingFragment()
        }
    }
}