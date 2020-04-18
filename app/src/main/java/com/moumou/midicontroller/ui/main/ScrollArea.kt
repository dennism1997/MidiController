package com.moumou.midicontroller.ui.main

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View

/**
 * Created by MouMou on 18-04-20.
 */
class ScrollArea(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0, 0)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TOUCH", event.action.toString())
        return super.onTouchEvent(event)
    }

    override fun onTrackballEvent(event: MotionEvent): Boolean {
        Log.d("TRACKBALL", event.action.toString())

        return super.onTrackballEvent(event)
    }
}