package com.intern.gagyebu.main

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intern.gagyebu.App
import com.intern.gagyebu.R
import com.intern.gagyebu.produce.ProduceActivity
import com.intern.gagyebu.databinding.RecyclerviewItemBinding
import com.intern.gagyebu.room.ItemEntity
import com.intern.gagyebu.room.ItemRepo
import com.intern.gagyebu.room.data.UpdateDate

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

                //수입, 지출에 따른 색상 변경
                when (itemList.category) {
                    "수입" -> color.setBackgroundColor(ContextCompat.getColor(App.context(), R.color.income))

                    else -> color.setBackgroundColor(ContextCompat.getColor(App.context(), R.color.spend))
                }

                //년-월-일 항목을 하나로 합쳐 표현
                date.text = this.root.context.getString(
                    R.string.show_date_full,
                    itemList.year,
                    itemList.month,
                    itemList.day
                )

                //수정, 삭제 Dialog 표시
                this.root.setOnLongClickListener {

                    val array = arrayOf("수정", "삭제")

                    val builder = AlertDialog.Builder(this.root.context)
                    builder.setItems(array) { dialog, which ->
                        when (which) {
                            /**
                             * 수정 요청시 항목을 UpdateDate.class parcel -> ProduceActivity 전달
                            */
                            0 -> {
                                val updateData = UpdateDate(id = itemList.id,
                                    date = date.text as String, title = itemList.title, amount = itemList.amount, category = itemList.category)

                                val intent = Intent(this.root.context, ProduceActivity::class.java).apply {
                                    putExtra("updateData", updateData)
                                }

                                this.root.context.startActivity(intent)
                            }

                            /**
                             * 삭제 요청시 해당 항목 id 를 통해 삭제 요청
                             */

                            1 -> AlertDialog.Builder(this.root.context).apply {
                                this.setTitle("삭제")
                                this.setMessage("정말 삭제할까요?")
                                this.setPositiveButton("삭제") { _, _ ->
                                    ItemRepo.deleteItem(itemList.id)
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