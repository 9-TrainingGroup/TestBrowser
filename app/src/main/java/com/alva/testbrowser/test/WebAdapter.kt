package com.alva.testbrowser.test

import android.content.Intent
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alva.testbrowser.R
import com.alva.testbrowser.activity.MainActivity
import com.alva.testbrowser.databinding.CellBookmarkBinding
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BookmarkAdapter(private val viewModel: WebViewModel) :
    ListAdapter<Bookmarks, WebViewHolder>(object : DiffUtil.ItemCallback<Bookmarks>() {
        override fun areItemsTheSame(oldItem: Bookmarks, newItem: Bookmarks) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Bookmarks, newItem: Bookmarks) =
            oldItem == newItem
    }) {
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
            menu.forEach { item ->
                item.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.editItem -> {
                            val v = View.inflate(view.context, R.layout.dialog_edit_web, null)
                            val binding = DialogEditWebBinding.bind(v)
                            binding.editTextName.setText(getItem(holder.absoluteAdapterPosition).name)
                            binding.editTextUrl.setText(getItem(holder.absoluteAdapterPosition).url)
                            val dialog: AlertDialog = MaterialAlertDialogBuilder(view.context)
                                .setTitle(R.string.dialog_add_title)
                                .setView(v)
                                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                                    val web = Bookmarks(
                                        id = getItem(holder.absoluteAdapterPosition).id,
                                        name = binding.editTextName.text.toString().trim(),
                                        url = binding.editTextUrl.text.toString().trim()
                                    )
                                    viewModel.updateWebs(web)
                                }.setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                                    dialog.cancel()
                                }
                                .create()
                            dialog.window?.attributes?.gravity = Gravity.BOTTOM
                            dialog.show()
                            binding.editTextName.requestFocus()
                            ViewCompat.getWindowInsetsController(v)
                                ?.show(WindowInsetsCompat.Type.ime())
                            binding.editTextUrl.doAfterTextChanged {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                                    it.toString().isNotBlank()
                            }
                        }
                        R.id.deleteItem -> viewModel.deleteWebs(getItem(holder.absoluteAdapterPosition))
                    }
                    return@setOnMenuItemClickListener false
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: WebViewHolder, position: Int) {
        holder.viewBinding.textViewName.apply {
            text = getItem(position).name
            if (getItem(position).name.isEmpty()) {
                text = "未知网站"
            }
        }
        holder.viewBinding.textViewUrl.text = getItem(position).url
    }
}

class HistoryAdapter(private val viewModel: WebViewModel) :
    ListAdapter<Histories, WebViewHolder>(object : DiffUtil.ItemCallback<Histories>() {
        override fun areItemsTheSame(oldItem: Histories, newItem: Histories) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Histories, newItem: Histories) =
            oldItem == newItem
    }) {
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
        holder.viewBinding.textViewName.text = getItem(position).name
        holder.viewBinding.textViewUrl.text = getItem(position).url
    }
}

class WebViewHolder(val viewBinding: CellBookmarkBinding) :
    RecyclerView.ViewHolder(viewBinding.root)