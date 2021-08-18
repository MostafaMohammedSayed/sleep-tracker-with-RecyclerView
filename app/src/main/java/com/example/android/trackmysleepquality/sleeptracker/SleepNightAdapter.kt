/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<SleepNightAdapter.ViewHolder>(){

    //the list o be displayed
    var data = listOf<SleepNight>()
        set(value) {
            field = value
            //this adapter method notify RV when the list change and make RV re-draw the whole list
            notifyDataSetChanged()
        }
    /*onCreateViewHolder(): creates the viewHolder that the RV will deal with since RV does not deal
    * with data, the arguments: parent which is the RV itself, ViewType is an Int that will be used if more than 1
    * viewHolder is used, TextItemViewHolder is a pre-made viewHolder which u can find in Util.kt file,
    * this fun returns a viewHolder that takes a view as an argument. This function is called when the app starts
    * at first time and when the views displayed on the screen increase */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
        return ViewHolder(view)
    }

    /*onBindViewHolder(): is called to bind an item at a specified position in the list to the
    * viewHolder. This fun also RECYCLES the views as it uses viewholders of items that are no longer on the screen
    * and reset its values with the values of the item that will be displayed on the screen  */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        //set the values of viewHolder properties to the items values
        val res = holder.itemView.resources
        bind(holder,item, res)

    }

    private fun bind(
        holder: ViewHolder,item: SleepNight
        res: Resources
    ) {
        holder.sleepLength.text =
            convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        holder.quality.text = convertNumericQualityToString(item.sleepQuality, res)
        holder.qualityImage.setImageResource(
            when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            }
        )
    }

    //the RV must know how many items will it draw, this fun provide items count
    override fun getItemCount()= data.size

    //customized viewHolder to display the data in our list item layout
    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)
    }
}


