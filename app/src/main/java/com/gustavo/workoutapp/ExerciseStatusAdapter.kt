package com.gustavo.workoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gustavo.workoutapp.databinding.ActivityExerciseBinding
import com.gustavo.workoutapp.databinding.ItemExcerciseStatusBinding

class ExerciseStatusAdapter(val items:ArrayList<ExerciseModel>):RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemExcerciseStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model:ExerciseModel = items[position]
        holder.txtItem.text = model.getId().toString()

        when{
            model.getIsSelected() ->{
                holder.txtItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_thin_color_accent_border)
                holder.txtItem.setTextColor(Color.parseColor("#212121"))
            }
            model.getIsCompleted() ->{
                holder.txtItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_accent_background)
                holder.txtItem.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else ->{
                holder.txtItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_color_gray_background)
                holder.txtItem.setTextColor(Color.parseColor("#212121"))
            }
        }


    }

    inner class ViewHolder(binding: ItemExcerciseStatusBinding):RecyclerView.ViewHolder(binding.root){
        val txtItem = binding.txtItem


    }
}