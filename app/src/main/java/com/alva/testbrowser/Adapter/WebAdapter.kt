package com.alva.testbrowser.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.alva.testbrowser.*
import com.alva.testbrowser.databinding.CellBookmarkBinding
import com.alva.testbrowser.databinding.DialogEditWebBinding
import com.alva.testbrowser.test.Bookmarktest
import com.alva.testbrowser.test.WebViewModel

class BookmarkAdapter(private val viewModel: WebViewModel) :
    RecyclerView.Adapter<BookmarkViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        CellBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            return BookmarkViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val webs = viewModel.allBookmark.value!![position]
        if (webs.name.isEmpty()) {
            holder.viewBinding.textView.text = webs.url
            holder.viewBinding.textView.textSize = 20F
        } else {
            holder.viewBinding.textView.text = webs.name
        }
        holder.itemView.setOnClickListener {
            // TODO: 2021/7/16：传递网页给MainActivity
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("webUrl", webs.url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(it.context, MainActivity::class.java)
            it.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            val menu = PopupMenu(it.context, it)
            val v = View.inflate(it.context, R.layout.dialog_edit_web, null)
            val binding = DialogEditWebBinding.bind(v)
            binding.editTextName.setText(webs.name)
            binding.editTextUrl.setText(webs.url)
            menu.menuInflater.inflate(R.menu.menu_bookmark, menu.menu)
            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.editItem -> {
                        val builder: AlertDialog =
                            AlertDialog.Builder(it.context)
                                .setTitle(R.string.dialog_edit_title)
                                .setView(v)
                                .setPositiveButton(R.string.dialog_positive_message) { _, _ ->
                                    val web = Bookmarktest(
                                        binding.editTextName.text.toString(),
                                        binding.editTextUrl.text.toString()
                                    )
                                    web.id = webs.id
                                    viewModel.updateWebs(web)
                                }
                                .setNegativeButton(R.string.dialog_negative_message) { dialog, _ ->
                                    dialog.cancel()
                                }
                                .show()
                        builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
                        builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK)
                        binding.editTextName.requestFocus()
                        (it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                            binding.editTextName,
                            0
                        )
                        binding.editTextUrl.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
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
                    R.id.deleteItem -> viewModel.deleteWebs(webs)
                }
                return@setOnMenuItemClickListener false
            }
            menu.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return viewModel.allBookmark.value!!.size
    }
}

class BookmarkViewHolder(val viewBinding: CellBookmarkBinding) :
    RecyclerView.ViewHolder(viewBinding.root)

class HistoryAdapter(private val viewModel: WebViewModel) :
    RecyclerView.Adapter<HistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        CellBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
            return HistoryViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val webs = viewModel.allHistory.value!![position]
        holder.viewBinding.textView.text = webs.name
        holder.itemView.setOnClickListener {
            // TODO: 2021/7/16：传递网页给MainActivity
            val intent = Intent()
            intent.putExtra("webUrl", webs.url)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(it.context, MainActivity::class.java)
            it.context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            val menu = PopupMenu(it.context, it)
            menu.menuInflater.inflate(R.menu.menu_history, menu.menu)
            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.deleteItem -> viewModel.deleteWebs(webs)
                }
                return@setOnMenuItemClickListener false
            }
            menu.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return viewModel.allHistory.value!!.size
    }
}

class HistoryViewHolder(val viewBinding: CellBookmarkBinding) :
    RecyclerView.ViewHolder(viewBinding.root)