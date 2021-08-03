package com.alva.testbrowser.fragment

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alva.testbrowser.R
import com.alva.testbrowser.database.Bookmark
import com.alva.testbrowser.database.NewsItem
import com.alva.testbrowser.util.RecordViewModel
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.databinding.FragmentWebBinding
import com.alva.testbrowser.webview.*
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebFragment : Fragment() {
    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebViewExt

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<RecordViewModel>()
        webView = WebViewExt(requireContext())
        webView.init({ _, _ -> }, 1)
        webView.loadUrl(arguments?.getParcelable<NewsItem>("NEWS_POSITION")!!.url)
        binding.webView.addView(webView)
        requireActivity().findViewById<ImageButton>(R.id.refreshNews).setOnClickListener {
            val v = View.inflate(it.context, R.layout.dialog_edit_web, null)
            val dialogBinding = DialogEditWebBinding.bind(v)
            dialogBinding.editTextName.setText(webView.title)
            dialogBinding.editTextUrl.setText(webView.url)
            val builder: AlertDialog = AlertDialog.Builder(it.context)
                .setTitle(R.string.dialog_add_title)
                .setView(v)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    viewModel.insertBookmark(
                        Bookmark(
                            dialogBinding.editTextName.text.toString(),
                            dialogBinding.editTextUrl.text.toString()
                        )
                    )
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.dialog_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            dialogBinding.editTextName.requestFocus()
            viewLifecycleOwner.lifecycleScope.launch {
                delay(100)
                (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                    dialogBinding.editTextName,
                    0
                )
            }
            dialogBinding.editTextUrl.addTextChangedListener { editable ->
                if (editable.toString().isBlank()) {
                    builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                        setTextColor(Color.GRAY)
                        isEnabled = false
                    }
                } else {
                    builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                        setTextColor(Color.BLACK)
                        isEnabled = true
                    }
                }
            }
        }
        requireActivity().findViewById<MaterialAutoCompleteTextView>(R.id.searchEdit)
            .addTextChangedListener {
                webView.findAllAsync(it.toString().trim())
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.removeAllViews()
        webView.destroy()
        _binding = null
    }
}