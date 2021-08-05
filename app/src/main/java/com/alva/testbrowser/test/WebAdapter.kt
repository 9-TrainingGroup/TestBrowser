package com.alva.testbrowser.test

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alva.testbrowser.R
import com.alva.testbrowser.activity.MainActivity
import com.alva.testbrowser.databinding.CellBookmarkBinding
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BookmarkAdapter(private val viewModel: WebViewModel) :
    ListAdapter<Bookmarktest, WebViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewHolder {
        val holder = WebViewHolder(
            CellBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("webUrl", getItem(holder.absoluteAdapterPosition).url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(parent.context, MainActivity::class.java)
            parent.context.startActivity(intent)
        }
        holder.itemView.setOnCreateContextMenuListener { menu, view, _ ->
            MenuInflater(view.context).inflate(R.menu.menu_bookmark, menu)
            menu[0].setOnMenuItemClickListener {
                val v = View.inflate(view.context, R.layout.dialog_edit_web, null)
                val binding = DialogEditWebBinding.bind(v)
                binding.editTextName.setText(getItem(holder.absoluteAdapterPosition).name)
                binding.editTextUrl.setText(getItem(holder.absoluteAdapterPosition).url)
                val builder = MaterialAlertDialogBuilder(view.context)
                    .setTitle(R.string.dialog_add_title)
                    .setView(v)
                    .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                        val web = Bookmarktest(
                            binding.editTextName.text.toString().trim(),
                            binding.editTextUrl.text.toString().trim()
                        )
                        web.id = getItem(holder.absoluteAdapterPosition).id
                        viewModel.updateWebs(web)
                    }.setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
                binding.editTextName.requestFocus()
                viewModel.viewModelScope.launch {
                    delay(100)
                    (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                        binding.editTextName,
                        0
                    )
                }
                binding.editTextUrl.addTextChangedListener {
                    builder.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        it.toString().isNotBlank()
                }
                return@setOnMenuItemClickListener false
            }
            menu[1].setOnMenuItemClickListener {
                viewModel.deleteWebs(getItem(holder.absoluteAdapterPosition))
                return@setOnMenuItemClickListener false
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: WebViewHolder, position: Int) {
        holder.viewBinding.textView.apply {
            if (getItem(position).name.isEmpty()) {
                text = getItem(position).url
                textSize = 20F
            } else {
                text = getItem(position).name
                textSize = 34F
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Bookmarktest>() {
        override fun areItemsTheSame(oldItem: Bookmarktest, newItem: Bookmarktest) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Bookmarktest, newItem: Bookmarktest) =
            oldItem == newItem
    }
}

class HistoryAdapter(private val viewModel: WebViewModel) :
    ListAdapter<Historytest, WebViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewHolder {
        val holder = WebViewHolder(
            CellBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        holder.itemView.setOnClickListener {
            val intent = Intent()
            intent.putExtra("webUrl", getItem(holder.absoluteAdapterPosition).url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(parent.context, MainActivity::class.java)
            parent.context.startActivity(intent)
        }
        holder.itemView.setOnCreateContextMenuListener { menu, view, _ ->
            MenuInflater(view.context).inflate(R.menu.menu_history, menu)
            menu[0].setOnMenuItemClickListener {
                viewModel.deleteWebs(getItem(holder.absoluteAdapterPosition))
                return@setOnMenuItemClickListener false
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: WebViewHolder, position: Int) {
        holder.viewBinding.textView.text = getItem(position).name
    }

    object DiffCallback : DiffUtil.ItemCallback<Historytest>() {
        override fun areItemsTheSame(oldItem: Historytest, newItem: Historytest) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Historytest, newItem: Historytest) =
            oldItem == newItem
    }
}

class WebViewHolder(val viewBinding: CellBookmarkBinding) :
    RecyclerView.ViewHolder(viewBinding.root)