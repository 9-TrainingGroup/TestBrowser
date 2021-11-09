package com.alva.testbrowser.test

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.alva.testbrowser.R
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.databinding.FragmentBookmarkBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookmarkFragment : Fragment() {
    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<WebViewModel>()
    private val bookmarkAdapter by lazy { BookmarkAdapter(viewModel) }

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

        binding.recyclerView.apply {
            adapter = bookmarkAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        viewModel.allBookmark.observe(viewLifecycleOwner) {
            bookmarkAdapter.submitList(it)
        }
        binding.deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.dialog_delete_bookmark_title)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    viewModel.deleteAllBookmark()
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .create()
                .show()
        }
        binding.addButton.setOnClickListener {
            val v = View.inflate(it.context, R.layout.dialog_edit_web, null)
            val dialogBinding = DialogEditWebBinding.bind(v)
            val dialog: AlertDialog = MaterialAlertDialogBuilder(it.context)
                .setTitle(R.string.dialog_add_title)
                .setView(v)
                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                    val web = Bookmarks(
                        name = dialogBinding.editTextName.text.toString().trim(),
                        url = dialogBinding.editTextUrl.text.toString().trim()
                    )
                    viewModel.insertWebs(web)
                }
                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                    dialog.cancel()
                }
                .create()
            dialog.window?.attributes?.gravity = Gravity.BOTTOM
            dialog.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            dialogBinding.editTextName.requestFocus()
            ViewCompat.getWindowInsetsController(v)?.show(WindowInsetsCompat.Type.ime())
            dialogBinding.editTextUrl.doAfterTextChanged { editable ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                    editable.toString().isNotBlank()
            }
        }
    }

/*    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.editItem -> {
            }
        }
        return super.onContextItemSelected(item)
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}