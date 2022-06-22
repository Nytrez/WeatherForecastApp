package com.example.weatherforecastapp.Ui.forecastfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.NextDays
import com.example.weatherforecastapp.utils.epochToDay
import com.example.weatherforecastapp.utils.epochToDayName

/**
 * Simple adapter for the recycler view in the forecast fragment
 */
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

        // checking if we got the day info
        if (currDay.maxTmp.isNaN()) {
            holder.bodyMaxTmp.visibility = View.GONE
        } else {
            holder.bodyMaxTmp.visibility = View.VISIBLE
            holder.bodyMaxTmp.text = "Max temperature: " + currDay.maxTmp.toString().take(5) + " \u2103"
        }

        if (currDay.morningTmp.isNaN()) {
            holder.bodyMorningTmp.visibility = View.GONE
        } else {
            holder.bodyMorningTmp.visibility = View.VISIBLE
            holder.bodyMorningTmp.text = "Morning: " + currDay.morningTmp.toString().take(5) + " \u2103"
        }

        if (currDay.dayTmp.isNaN()) {
            holder.bodyDayTmp.visibility = View.GONE
        } else {
            holder.bodyDayTmp.visibility = View.VISIBLE
            holder.bodyDayTmp.text = "Day: " + currDay.dayTmp.toString().take(5) + " \u2103"
        }

        if (currDay.nightTmp.isNaN()) {
            holder.bodyNightTmp.visibility = View.GONE
        } else {
            holder.bodyNightTmp.visibility = View.VISIBLE
            holder.bodyNightTmp.text = "Night: " + currDay.nightTmp.toString().take(5) + " \u2103"
        }

        if (currDay.humidity.isNaN()) {
            holder.bodyHumidity.visibility = View.GONE
        } else {
            holder.bodyHumidity.visibility = View.VISIBLE
            holder.bodyHumidity.text = "Avg. humidity: " + currDay.humidity.toString().take(5) + "%"
        }

        if (currDay.modeTmp.isNaN()) {
            holder.bodyModeTmp.visibility = View.GONE
        } else {
            holder.bodyModeTmp.visibility = View.VISIBLE
            holder.bodyModeTmp.text = "Mode temperature: " + currDay.modeTmp.toString().take(5) + " \u2103"
        }


        if (currDay.isExpanded) {
            holder.headerBody.visibility = View.VISIBLE
            holder.headerArrow.setImageResource(R.drawable.arrow_up_icon)
        } else {
            holder.headerBody.visibility = View.GONE
            holder.headerArrow.setImageResource(R.drawable.arrow_down_icon)
        }

        // setting custom image depending on the temperature
        // (i only had 3 images so i thought that selecting intervals -18, 19-21, 22+
        // would be easier to observe in June)
        if (currDay.avgTmp < 18) {
            holder.headerImage.setImageResource(R.drawable.low_temp_icon)
        } else if (currDay.avgTmp < 22) {
            holder.headerImage.setImageResource(R.drawable.normal_temp_icon)
        } else {
            holder.headerImage.setImageResource(R.drawable.high_temp_icon)
        }

        holder.headerLayout.setOnClickListener {
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
        val headerBody: LinearLayout = itemView.findViewById(R.id.body_layout)
        val headerLayout: ConstraintLayout = itemView.findViewById(R.id.headerLayout)
        val headerImage: ImageView = itemView.findViewById(R.id.header_tmp_icon)
        val headerArrow: ImageView = itemView.findViewById(R.id.header_arrow)

        val bodyMaxTmp: TextView = itemView.findViewById(R.id.body_max_tmp)
        val bodyMorningTmp: TextView = itemView.findViewById(R.id.body_morning_tmp)
        val bodyDayTmp: TextView = itemView.findViewById(R.id.body_day_tmp)
        val bodyNightTmp: TextView = itemView.findViewById(R.id.body_night_tmp)
        val bodyModeTmp : TextView = itemView.findViewById(R.id.body_mode_tmp)
        val bodyHumidity: TextView = itemView.findViewById(R.id.body_humidity)


    }
}

