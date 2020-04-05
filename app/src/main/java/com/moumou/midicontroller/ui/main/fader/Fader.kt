package com.moumou.midicontroller.ui.main.fader

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar
import com.moumou.midicontroller.R

/**
 * Created by MouMou on 04-04-20.
 */

class Fader(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, R.attr.seekBarStyle)


    init {
        this.rotation = -90f
    }



}