package com.example.photoeditor

import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoeditor.MainActivity.Main.IMAGE_NAME
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.FilterPack
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.imageprocessors.Filter
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.utils.ThumbnailItem
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.utils.ThumbnailsManager
import com.example.photoeditor.`interface`.FilterListFragmentListener
import com.example.photoeditor.adapters.ThumbnailAdapters
import com.example.photoeditor.utils.BitmapUtils
import com.example.photoeditor.utils.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_filter_list.*


class FilterListFragment : Fragment(), FilterListFragmentListener {

    private var listener: FilterListFragmentListener? = null
    internal lateinit var adapter: ThumbnailAdapters
    internal lateinit var thumbnailItemList: MutableList<ThumbnailItem>

    fun setListener(listener: FilterListFragmentListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_list, container, false)

        thumbnailItemList = ArrayList()
        adapter = activity?.let { ThumbnailAdapters(it, thumbnailItemList, this) }!!

        recycler_view.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.itemAnimator = DefaultItemAnimator()
        val space =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics)
                .toInt()
        recycler_view.addItemDecoration(SpaceItemDecoration(space))
        recycler_view.adapter = adapter

        displayImage(null)


        return view;
    }

    private fun displayImage(bitmap: Bitmap?) {
        val r = Runnable {
            val thumbImage: Bitmap? = if (bitmap == null) {
                activity?.let { BitmapUtils.getBitmapFromAssets(it, IMAGE_NAME, 100, 100) }
            } else {
                Bitmap.createScaledBitmap(bitmap, 100, 100, false)
            }
            if (thumbImage == null)
                return@Runnable

            ThumbnailsManager.clearThumbs()
            thumbnailItemList.clear()

            //add normal Bitmap First
            val thumbnailItem = ThumbnailItem()
            thumbnailItem.image = thumbImage
            thumbnailItem.filterName = "Normal"
            ThumbnailsManager.addThumb(thumbnailItem)

            //Add Filter Pack
            val filters = FilterPack.getFilterPack(activity)

            for (filter in filters) {
                val item = ThumbnailItem()
                item.image = thumbImage
                item.filter = filter
                item.filterName = filter.name
                ThumbnailsManager.addThumb(item)
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(activity))
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }

        }
        Thread(r).start()
    }


    override fun onFilterSelected(filter: Filter) {
        if (listener != null) {
            listener!!.onFilterSelected(filter)
        }
    }
}