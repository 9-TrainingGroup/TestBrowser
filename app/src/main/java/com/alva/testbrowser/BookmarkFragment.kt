package com.alva.testbrowser

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alva.testbrowser.Adapter.BookmarkAdapter
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

        val viewModel by activityViewModels<BookmarkViewModel>()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.allWebsLive.observe(viewLifecycleOwner, {
            binding.recyclerView.adapter = BookmarkAdapter(viewModel)
        })
        binding.deleteButton.setOnClickListener {
            val builder: AlertDialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_bookmark_title)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    viewModel.deleteAllWebs()
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
            val builder: AlertDialog =
                AlertDialog.Builder(it.context)
                    .setTitle(R.string.dialog_add_title)
                    .setView(v)
                    .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                        val web = Bookmark(
                            dialogBinding.editTextName.text.toString(),
                            dialogBinding.editTextUrl.text.toString()
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
            dialogBinding.editTextUrl.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isNotEmpty()) {
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                            setTextColor(Color.BLACK)
                            isEnabled = true
                        }
                    } else {
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                            setTextColor(Color.GRAY)
                            isEnabled = false
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}