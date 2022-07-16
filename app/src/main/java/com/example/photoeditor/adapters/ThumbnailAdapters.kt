package com.example.photoeditor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.utils.ThumbnailItem
import com.example.photoeditor.R
import com.example.photoeditor.`interface`.FilterListFragmentListener
import kotlinx.android.synthetic.main.thumbnail_list_item.view.*

class ThumbnailAdapters(
    private val context: Context,
    private val thumbnailItemList: List<ThumbnailItem>,
    private val listener: FilterListFragmentListener
) :
    RecyclerView.Adapter<ThumbnailAdapters.MyViewHolder>() {

    private var selectedIndex = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.thumbnail_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val thumbnailItem = thumbnailItemList[position]
        holder.thumbNail.setImageBitmap(thumbnailItem.image)
        holder.thumbNail.setOnClickListener {
            listener.onFilterSelected(thumbnailItem.filter)
            selectedIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return thumbnailItemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var thumbNail: ImageView
        var filterName: TextView

        init {
            thumbNail = itemView.thumbnail
            filterName = itemView.filter_name
        }

    }

}