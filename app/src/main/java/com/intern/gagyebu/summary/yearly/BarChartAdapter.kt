package com.intern.gagyebu.summary.yearly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intern.gagyebu.databinding.RecyclerBarChartBinding
import com.intern.gagyebu.summary.util.BarChartInfo

class BarChartAdapter : RecyclerView.Adapter<BarChartAdapter.ViewHolder>() {
    var barChartInfo = mutableListOf<BarChartInfo>()

    private var listener: BarChartClickListener? = null

    //customView - bar chart의 bar 부분 클릭 리스너 달아주는 부분
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

    inner class ViewHolder(private val binding: RecyclerBarChartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BarChartInfo) {
            //해당 연도에 데이터가 있는 월이 하나라도 있어서 쭉 나열은 되지만
            //데이터가 없는 월에는 클릭 리스너를 달아주고 싶지 않아서
            //item.percentage > 0f일 때만 클릭이 되도록 설정
            if (position != RecyclerView.NO_POSITION && item.percentage > 0f) {
                itemView.setOnClickListener {
                    listener?.onItemClicked(item.month)
                }
            }

            binding.tvMonth.text = YearlySummaryViewModel.months[item.month]
            //이전에 실행되던 커스텀뷰를 재사용하기 때문에
            //새로 percentage를 지정해주기 전에
            //애니메이션이 있는 경우 destroy()를 해주어야 정상 작동함
            binding.viewPercentage.destroyAnimator()
            binding.viewPercentage.setPercentage(item.percentage)
        }
    }


}