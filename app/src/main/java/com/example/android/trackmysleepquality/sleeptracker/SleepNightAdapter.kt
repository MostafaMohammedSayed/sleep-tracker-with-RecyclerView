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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SleepNightAdapter(val clickListener: SleepNightListener): ListAdapter<DataItem,RecyclerView.ViewHolder>(SleepNightDiffCallback()){

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header-> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem-> ITEM_VIEW_TYPE_ITEM
        }
    }

    /*onCreateViewHolder(): creates the viewHolder that the RV will deal with since RV does not deal
        * with data, the arguments: parent which is the RV itself, ViewType is an Int that will be used if more than 1
        * viewHolder is used, TextItemViewHolder is a pre-made viewHolder which u can find in Util.kt file,
        * this fun returns a viewHolder that takes a view as an argument. This function is called when the app starts
        * at first time and when the views displayed on the screen increase */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    /*onBindViewHolder(): is called to bind an item at a specified position in the list to the
    * viewHolder. This fun also RECYCLES the views as it uses viewholders of items that are no longer on the screen
    * and reset its values with the values of the item that will be displayed on the screen  */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        //set the values of viewHolder properties to the items values
        holder.bind(item, clickListener)

    }


    //customized viewHolder to display the data in our list item layout
    class ViewHolder private constructor(val binding: ListItemSleepNightBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: SleepNight,
            clickListener: SleepNightListener
        ) {
            binding.sleep = item
            /*plumping work is happening as the adapter class takes a constructor arg : SleepNightListener
            * and passing it to onBindViewHolder() and to the viewHolder class to make the binding */
            binding.clickListener = clickListener
            binding.executePendingBindings()//slightly faster to bind the views
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class SleepNightDiffCallback: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class SleepNightListener(val clickListener: (nightId: Long)-> Unit){
    fun onClick(night: SleepNight)= clickListener(night.nightId)
}

sealed class DataItem{
    data class SleepNightItem(val sleepNight: SleepNight): DataItem() {
        override val id = sleepNight.nightId
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}


