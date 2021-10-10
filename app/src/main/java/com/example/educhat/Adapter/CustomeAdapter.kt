package com.example.educhat.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.educhat.Model.Model
import com.example.educhat.R


class CustomeAdapter(val ctx: Context, var clickListener: OnChapterClick, var chapterlist:ArrayList<Model>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : Model = chapterlist[position]
       holder.textViewName.text=user.fileName
        holder.textViewChapter.text=user.date


//        val url =user.icon
//
//        Glide.with(getApplicationContext()).load(url).into(holder.iconView)

     holder.initilise(chapterlist.get(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chapterlist.size
         }

}

class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    val textViewName = itemView.findViewById(R.id.textView) as TextView
    val textViewChapter = itemView.findViewById(R.id.textView2) as TextView

    var iconView=itemView.findViewById(R.id.imageView1) as ImageView

    fun initilise(list: Model, action: OnChapterClick){
        textViewName.text=list.fileName
        textViewChapter.text=list.date
//        iconView=list.icon

        itemView.setOnClickListener{
            action.onItemClick(list,adapterPosition)
        }

    }
}

interface OnChapterClick {
    fun onItemClick(list: Model, position: Int )

}
