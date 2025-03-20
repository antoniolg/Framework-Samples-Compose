package com.antonioleiva.frameworksamples.fragments.basic

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.antonioleiva.frameworksamples.R
import com.antonioleiva.frameworksamples.databinding.FragmentDynamicBinding

/**
 * A Fragment that demonstrates receiving arguments and lifecycle events
 * This fragment is added dynamically through code
 */
class DynamicFragment : Fragment() {

    private lateinit var binding: FragmentDynamicBinding

    companion object {
        private const val ARG_MESSAGE = "message"
        private const val TAG = "DynamicFragment"

        /**
         * Factory method to create a new instance of this fragment with arguments
         */
        fun newInstance(message: String): DynamicFragment {
            return DynamicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")
        binding = FragmentDynamicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        
        // Display the argument passed to the fragment
        val message = arguments?.getString(ARG_MESSAGE) ?: ""
        binding.tvArgument.text = getString(R.string.fragment_argument_label, message)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }
} 