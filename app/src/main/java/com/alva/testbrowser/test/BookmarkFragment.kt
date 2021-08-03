package com.alva.testbrowser.test

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.databinding.FragmentBookmarkBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel by activityViewModels<WebViewModel>()
        BookmarkAdapter(viewModel).apply {
            binding.recyclerView.adapter = this
            viewModel.allBookmark.observe(viewLifecycleOwner, {
                submitList(it)
            })
        }
        binding.deleteButton.setOnClickListener {
            val builder: AlertDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_bookmark_title)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    viewModel.deleteAllBookmark()
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }
        binding.addButton.setOnClickListener {
            val v = View.inflate(it.context, R.layout.dialog_edit_web, null)
            val dialogBinding = DialogEditWebBinding.bind(v)
            val builder: AlertDialog = AlertDialog.Builder(it.context)
                .setTitle(R.string.dialog_add_title)
                .setView(v)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    val web = Bookmarktest(
                        dialogBinding.editTextName.text.toString().trim(),
                        dialogBinding.editTextUrl.text.toString().trim()
                    )
                    viewModel.insertWebs(web)
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .show()
            builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColor(Color.GRAY)
                isEnabled = false
            }
            builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
            dialogBinding.editTextName.requestFocus()
            lifecycleScope.launch {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}