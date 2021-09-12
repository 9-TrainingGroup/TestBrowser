package com.alva.testbrowser.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.alva.testbrowser.R
import com.alva.testbrowser.activity.NewsActivity
import com.alva.testbrowser.database.Bookmark
import com.alva.testbrowser.util.RecordViewModel
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.databinding.FragmentWebBinding
import com.alva.testbrowser.webview.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class WebFragment : Fragment() {
    private var _binding: FragmentWebBinding? = null
    private val binding get() = _binding!!
    private lateinit var webView: WebViewExt
    private val args by navArgs<WebFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by viewModels<RecordViewModel>()
        webView = WebViewExt(requireContext())
        webView.init({ _, _ -> }, 1, ImageButton(requireContext()), ImageButton(requireContext()))
        args.newsUrl?.let { webView.loadUrl(it) }
        binding.webView.addView(webView)
        (requireActivity() as NewsActivity).binding.refreshNews.setOnClickListener {
            val v = View.inflate(it.context, R.layout.dialog_edit_web, null)
            val dialogBinding = DialogEditWebBinding.bind(v)
            dialogBinding.editTextName.setText(webView.title)
            dialogBinding.editTextUrl.setText(webView.url)
            val dialog: AlertDialog = MaterialAlertDialogBuilder(it.context)
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
                .create()
            dialog.window?.attributes?.gravity = Gravity.BOTTOM
            dialog.show()
            dialogBinding.editTextName.requestFocus()
            ViewCompat.getWindowInsetsController(v)?.show(WindowInsetsCompat.Type.ime())
            dialogBinding.editTextUrl.doAfterTextChanged { editable ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    editable.toString().isNotBlank()
            }
        }
        (requireActivity() as NewsActivity).binding.searchEdit.doAfterTextChanged {
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