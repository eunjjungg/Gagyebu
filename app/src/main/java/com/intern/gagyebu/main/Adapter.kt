package com.intern.gagyebu.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intern.gagyebu.App
import com.intern.gagyebu.R
import com.intern.gagyebu.databinding.RecyclerviewItemBinding
import com.intern.gagyebu.room.AppDatabase
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Adapter : ListAdapter<ItemEntity, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ViewHolder).bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class ViewHolder(
        private val binding: RecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemList: ItemEntity) {
            binding.apply {
                item = itemList
                Log.d("itme", itemList.category)

                when (itemList.category) {
                    "수입" -> color.setBackgroundColor(Color.BLUE)

                    else -> color.setBackgroundColor(Color.RED)
                }

                date.text = this.root.context.getString(
                    R.string.show_date_full,
                    itemList.year,
                    itemList.month,
                    itemList.day
                )

                this.root.setOnLongClickListener {

                    val array = arrayOf("수정", "삭제")

                    val builder = AlertDialog.Builder(this.root.context)
                    builder.setItems(array) { dialog, which ->
                        when (which) {
                            0 -> {
                                Log.d("long", "수정")
                            }

                            1 -> AlertDialog.Builder(this.root.context).apply {
                                this.setTitle("삭제")
                                this.setMessage("정말 삭제할까요?")
                                this.setPositiveButton("삭제") { _, _ ->
                                    val database = AppDatabase.getDatabase(App.context())
                                    CoroutineScope(Dispatchers.IO).launch {
                                        database.itemDao().deleteItem(itemList.id)
                                    }
                                }
                                this.setNegativeButton("취소") { _, _ ->
                                    dialog.cancel()
                                }
                            }.show()
                        }
                    }
                    builder.create()
                    builder.show()

                    return@setOnLongClickListener (false)
                }
                executePendingBindings()
            }
        }
    }
}

private class ItemDiffCallback : DiffUtil.ItemCallback<ItemEntity>() {

    override fun areItemsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemEntity, newItem: ItemEntity): Boolean {
        return oldItem == newItem
    }
}