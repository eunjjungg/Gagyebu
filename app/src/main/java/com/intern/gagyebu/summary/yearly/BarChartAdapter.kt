package com.intern.gagyebu.summary.yearly

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intern.gagyebu.databinding.RecyclerBarChartBinding
import com.intern.gagyebu.summary.util.BarChartInfo

class BarChartAdapter: RecyclerView.Adapter<BarChartAdapter.ViewHolder>() {
    var barChartInfo = mutableListOf<BarChartInfo>()

    private var listener: BarChartClickListener? = null

    fun setOnItemClickListener(_listener: BarChartClickListener) {
        this.listener = _listener
    }

    override fun getItemCount(): Int {
        return barChartInfo.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerBarChartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(barChartInfo[position])
    }

    inner class ViewHolder(private val binding: RecyclerBarChartBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BarChartInfo) {
            if(position != RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClicked(item.month)
                }
            }

            Log.d("adapter ccheck", item.toString())
            binding.tvMonth.text = YearlySummaryViewModel.months[item.month]
            binding.viewPercentage.setPercentage(item.percentage)
        }
    }


}