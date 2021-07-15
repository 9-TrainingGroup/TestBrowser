package com.alva.testbrowser

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.databinding.FragmentBookmarkBinding

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
            builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
            builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}