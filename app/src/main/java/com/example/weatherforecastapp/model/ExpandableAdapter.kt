package com.example.weatherforecastapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.utils.epochToDay
import com.example.weatherforecastapp.utils.epochToDayName


class ExpandableAdapter(
    private val daysInfo: List<NextDays>
    ) : RecyclerView.Adapter<ExpandableAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
        return ViewHolder(layoutInflater)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currDay = daysInfo[position]
        holder.headerDayDate.text = epochToDay(currDay.epochTime).toString()
        holder.headerAvgTmp.text = currDay.avgTmp.toString().take(5) + " \u2103"
        holder.headerMinTmp.text = currDay.minTmp.toString().take(5) + " \u2103"
        holder.headerDayName.text = epochToDayName(currDay.epochTime).take(3)
        if(currDay.isExpanded){
            holder.headerBody.visibility = View.VISIBLE
            holder.headerArrow.setImageResource(R.drawable.arrow_up_icon)
        } else {
            holder.headerBody.visibility = View.GONE
            holder.headerArrow.setImageResource(R.drawable.arrow_down_icon)
        }

        if(currDay.avgTmp < 18){
            holder.headerImage.setImageResource(R.drawable.low_temp_icon)
        } else if(currDay.avgTmp < 22){
            holder.headerImage.setImageResource(R.drawable.normal_temp_icon)
        } else {
            holder.headerImage.setImageResource(R.drawable.high_temp_icon)
        }

        holder.headerLayout.setOnClickListener{
            currDay.isExpanded = !currDay.isExpanded
            notifyItemChanged(position)
        }

    }

    override fun getItemCount(): Int {
        return daysInfo.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerAvgTmp: TextView = itemView.findViewById(R.id.header_avg_tmp)
        val headerMinTmp: TextView = itemView.findViewById(R.id.header_min_tmp)
        val headerDayName: TextView = itemView.findViewById(R.id.header_day)
        val headerDayDate: TextView = itemView.findViewById(R.id.header_day_date)
        val headerBody: LinearLayout =  itemView.findViewById(R.id.body_layout)
        val headerLayout : ConstraintLayout = itemView.findViewById(R.id.headerLayout)
        val headerImage : ImageView = itemView.findViewById(R.id.header_tmp_icon)
        val headerArrow: ImageView = itemView.findViewById(R.id.header_arrow)

    }
}

