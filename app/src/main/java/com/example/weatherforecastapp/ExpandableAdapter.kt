package com.example.weatherforecastapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.model.NextDays
import com.example.weatherforecastapp.utils.epochToDate
import com.example.weatherforecastapp.utils.epochToDay
import com.example.weatherforecastapp.utils.epochToDayName


class ExpandableAdapter(
    private val branchInformation: NextDays,
    private val savedContext: Context,

) : RecyclerView.Adapter<ExpandableAdapter.ViewHolder>() {

    companion object {
        const val PARENT = 0
        const val CHILD = 1
    }


    private var isExpanded: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            PARENT -> {
                ViewHolder.HeaderViewHolder(
                    layoutInflater.inflate(R.layout.item_header, parent, false)

                )
            }
            else -> {
                ViewHolder.ItemViewHolder(
                    layoutInflater.inflate(R.layout.item_body, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.HeaderViewHolder -> {
                holder.onBind(branchInformation, onHeaderClicked())
            }
            is ViewHolder.ItemViewHolder -> {
                holder.onBind2(branchInformation, savedContext)
            }

        }
    }

    override fun getItemCount(): Int {
        return if (isExpanded) {
            2
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            PARENT
        } else {
            CHILD
        }
    }

    private fun onHeaderClicked() = View.OnClickListener {
        isExpanded = !isExpanded

        if (isExpanded) {
//            it.findViewById<ImageView>(R.id.adapter_arrow).setImageDrawable(
//                ContextCompat.getDrawable(
//                    savedContext, R.drawable
//                        .aok_collapse_arrow
//                )
//            )
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            notifyItemRangeInserted(1, 1)
        } else {
//            it.findViewById<ImageView>(R.id.adapter_arrow).setImageDrawable(
//                ContextCompat.getDrawable(
//                    savedContext, R.drawable
//                        .aok_expand_arrow
//                )
//            )
            notifyItemRangeRemoved(1, 1)
        }

        notifyItemChanged(1)
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class HeaderViewHolder(itemView: View) : ViewHolder(itemView) {
            private val headerAvgTmp = itemView.findViewById<TextView>(R.id.header_avg_tmp)
            private val headerMinTmp = itemView.findViewById<TextView>(R.id.header_min_tmp)
            private val headerDayName = itemView.findViewById<TextView>(R.id.header_day)
            private val headerDayDate = itemView.findViewById<TextView>(R.id.header_day_date)

            fun onBind(
                currDay: NextDays,
                onClickListener: View.OnClickListener,

                ) {
                headerDayDate.text = epochToDay(currDay.epochTime).toString()
                headerAvgTmp.text = currDay.avgTmp.toString().take(5) + " \u2103"
                headerMinTmp.text = currDay.minTmp.toString().take(5) + " \u2103"
                headerDayName.text = epochToDayName(currDay.epochTime).take(3)


                itemView.setOnClickListener {
                    onClickListener.onClick(it)
                }
            }

        }

        class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
            private val branchName = itemView.findViewById<TextView>(R.id.branch_name)
            private val branchStreet = itemView.findViewById<TextView>(R.id.branch_street)
            private val branchZipCity = itemView.findViewById<TextView>(R.id.branch_zip_city)
//            private val branchAdditional = itemView.findViewById<TextView>(R.id.branch_additional_info)
//            private val branchPhone = itemView.findViewById<TextView>(R.id.branch_phone)
//            private val branchFax = itemView.findViewById<TextView>(R.id.branch_fax)
//            private val branchLink = itemView.findViewById<TextView>(R.id.branch_link)
//            private val mondayHours = itemView.findViewById<TextView>(R.id.monday_hours_opened)
//            private val tuesdayHours = itemView.findViewById<TextView>(R.id.tuesday_hours_opened)
//            private val wednesdayHours = itemView.findViewById<TextView>(R.id.wednesday_hours_opened)
//            private val thursdayHours = itemView.findViewById<TextView>(R.id.thursday_hours_opened)
//            private val fridayHours = itemView.findViewById<TextView>(R.id.friday_hours_opened)
//
//            private val phoneLogo = itemView.findViewById<ImageView>(R.id.branch_phone_logo)
//            private val faxLogo = itemView.findViewById<ImageView>(R.id.branch_fax_logo)
//            private val linkLogo = itemView.findViewById<ImageView>(R.id.branch_link_logo)

            fun onBind2(item: NextDays, context: Context) {
                branchName.text = item.epochTime.toString()
                branchStreet.text = item.maxTmp.toString()
                branchZipCity.text = item.humidity.toString()
//
//                //linkify
//                if (Html.fromHtml(item.additionalInfo).isEmpty()) {
//                    branchAdditional.visibility = View.GONE
//                } else {
//                    branchAdditional.text = Html.fromHtml(item.additionalInfo)
//                }
//
//
//                if (item.phone == "") {
//                    phoneLogo.visibility = View.GONE
//                    branchPhone.visibility = View.GONE
//
//                } else {
//                    branchPhone.text = item.phone
//                }
//
//
//                if (item.fax == "") {
//                    faxLogo.visibility = View.GONE
//                    branchFax.visibility = View.GONE
//                } else {
//                    branchFax.text = item.fax
//                }
//
//
//                if (item.link == "") {
//                    linkLogo.visibility = View.GONE
//                    branchLink.visibility = View.GONE
//                } else {
//                    branchLink.setOnClickListener {
//                        try {
//                            val url = Uri.parse(item.link)
//                            val intent = Intent(Intent.ACTION_VIEW, url)
//                            context.startActivity(intent)
//                        } catch (e: Exception) {
//                            //   Log.d("MAIN", "didn't u forgot about http://www.aok.de/?")
//                        } finally {
//                            try {
//                                val url = Uri.parse(context.getString(R.string.aok_url_placeholder).format(item.link))
//                                val intent = Intent(Intent.ACTION_VIEW, url)
//                                context.startActivity(intent)
//                            } catch (e: Exception) {
////                                Log.d("MAIN", item.link)
////                                Log.d("MAIN", "oh no, it's broken :- (")
//
//                            }
//                        }
//                    }
//
//
//                }
//
//                val appointmentReservation = SpannableString(context.getString(R.string.termin_reservation))
//                appointmentReservation.setSpan(UnderlineSpan(), 0, 18, 0)
//                appointmentReservation.setSpan(
//                    ForegroundColorSpan(
//                        ContextCompat.getColor(
//                            context,
//                            R.color.aok_green
//                        )
//                    ), 0, 18, 0
//                )
//                branchLink.text = appointmentReservation
//
//
//                val days = listOf<TextView>(mondayHours, tuesdayHours, wednesdayHours, thursdayHours, fridayHours)
//                for ((i, day) in days.withIndex()) {
//                    if (item.openDays[i] == "") {
//                        day.text = context.getString(R.string.closed)
//                    } else {
//                        day.text = item.openDays[i]
//                    }
//                }
//
//            }
//
            }

        }

    }
}