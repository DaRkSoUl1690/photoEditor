package com.example.photoeditor

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.imageprocessors.Filter
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.example.photoeditor.PhotoEditor.java.com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import com.example.photoeditor.`interface`.EditImageFragmentListener
import com.example.photoeditor.`interface`.FilterListFragmentListener
import com.example.photoeditor.adapters.ViewPagerAdapter
import com.example.photoeditor.utils.BitmapUtils
import com.example.photoeditor.utils.NonSwipableViewPager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), FilterListFragmentListener, EditImageFragmentListener {

    internal var originalImage: Bitmap? = null
    internal lateinit var filteredImage: Bitmap
    internal lateinit var finalImage: Bitmap

    internal lateinit var filterListFragment: FilterListFragment
    internal lateinit var editImageFragment: EditImageFragment

    internal var brightnessFinal = 0
    internal var saturationFinal = 1.10f
    internal var contrastFinal = 1.0f

    object Main {
        val IMAGE_NAME = "flash.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set Toolbar
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Instagram Filters"

        loadImage()
        setupViewPager(viewpager)
        tabs.setupWithViewPager(viewpager)

    }

    private fun setupViewPager(viewpager: NonSwipableViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add filter list fragment
        filterListFragment = FilterListFragment()
        filterListFragment.setListener(this)


        //add edit Image fragment
        editImageFragment = EditImageFragment()
        editImageFragment.setListener(this)

        adapter.addFragment(filterListFragment,"Filters")
        adapter.addFragment(editImageFragment,"Edit")

        viewpager!!.adapter = adapter

    }

    private fun loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this, Main.IMAGE_NAME, 300, 300)
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.setImageBitmap(originalImage)
    }

    override fun onFilterSelected(filter: Filter) {
        resetController()
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.setImageBitmap((filter.processFilter(filteredImage)))
        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun resetController() {
        if (editImageFragment != null)
            editImageFragment.resetController()
        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal = 1.0f
    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.setImageBitmap(
            myFilter.processFilter(
                finalImage.copy(
                    Bitmap.Config.ARGB_8888,
                    true
                )
            )
        )
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.setImageBitmap(
            myFilter.processFilter(
                finalImage.copy(
                    Bitmap.Config.ARGB_8888,
                    true
                )
            )
        )
    }

    override fun onConstraintChanged(constraint: Float) {
        contrastFinal = constraint
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(constraint))
        image_preview.setImageBitmap(
            myFilter.processFilter(
                finalImage.copy(
                    Bitmap.Config.ARGB_8888,
                    true
                )
            )
        )
    }

    override fun onEditStarted() {
        TODO("Not yet implemented")
    }

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        finalImage = myFilter.processFilter(bitmap)
    }

}