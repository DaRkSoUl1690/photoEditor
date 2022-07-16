package com.example.photoeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.photoeditor.`interface`.EditImageFragmentListener
import kotlinx.android.synthetic.main.fragment_edit_image.*

class EditImageFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private var listener: EditImageFragmentListener? = null


    fun resetController(){
        seekbar_brightness.progress = 100
        seekbar_contraint.progress = 0
        seekbar_saturation.progress = 10
    }


    fun setListener(listener: EditImageFragmentListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_image, container, false)

        seekbar_brightness.max = 200
        seekbar_brightness.progress = 100

        seekbar_contraint.max = 200
        seekbar_contraint.progress = 0

        seekbar_saturation.max = 30
        seekbar_saturation.progress = 10

        seekbar_saturation.setOnSeekBarChangeListener(this)
        seekbar_contraint.setOnSeekBarChangeListener(this)
        seekbar_brightness.setOnSeekBarChangeListener(this)

        return view
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        var progress: Int = progress
        if (listener != null) {
            if (seekBar != null) {
                if (seekBar.id == R.id.seekbar_brightness) {
                    listener!!.onBrightnessChanged(progress - 100)

                } else if (seekBar.id == R.id.seekbar_contraint) {
                    progress+=10
                    val floatVal = 10f*progress
                    listener!!.onConstraintChanged(floatVal)

                } else if (seekBar.id == R.id.seekbar_saturation) {
                    val floatVal = 10f*progress
                    listener!!.onSaturationChanged(floatVal)

                }
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
      if(listener != null)
          listener!!.onEditStarted()
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if(listener != null)
            listener!!.onEditCompleted()
    }

}