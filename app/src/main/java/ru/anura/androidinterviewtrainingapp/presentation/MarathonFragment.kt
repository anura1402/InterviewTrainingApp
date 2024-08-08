package ru.anura.androidinterviewtrainingapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.anura.androidinterviewtrainingapp.databinding.FragmentMarathonBinding
import ru.anura.androidinterviewtrainingapp.databinding.FragmentQuestionsBinding

class MarathonFragment : Fragment() {
    private var _binding: FragmentMarathonBinding? = null
    private val binding: FragmentMarathonBinding
        get() = _binding ?: throw RuntimeException("FragmentMarathonBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarathonBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "MarathonFragment"
        fun newInstance(): MarathonFragment {
            return MarathonFragment()
        }
    }
}