package com.broood.drawingapp

import android.app.Dialog
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawingView)
        drawingView?.setSizeForBrush(20.toFloat())

        val ib_brush: ImageButton = findViewById<ImageButton>(R.id.ib_brush)
        ib_brush.setOnClickListener{
            showBrushSizeChooserDialog()
        }
        val ib_color: ImageButton = findViewById(R.id.ib_color)
        ib_color.setOnClickListener {
            showColorChooserDialog()
        }


    }
    private fun showBrushSizeChooserDialog() {
        var brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.activity_dialog_brush_size)
        brushDialog.setTitle("Brush size: ")
        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small)
        smallBtn.setOnClickListener{
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium)
        mediumBtn.setOnClickListener{
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }
        val largeBtn = brushDialog.findViewById<ImageButton>(R.id.ib_large)
        largeBtn.setOnClickListener{
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }
    private fun showColorChooserDialog() {
        var colorDialog = Dialog(this)
        colorDialog.setContentView(R.layout.activity_dialog_color)
        colorDialog.setTitle("Color: ")
        val greenBtn = colorDialog.findViewById<ImageButton>(R.id.ib_green)
        greenBtn.setOnClickListener{
            drawingView?.setColor("#008000")
            colorDialog.dismiss()
        }
        val blueBtn = colorDialog.findViewById<ImageButton>(R.id.ib_blue)
        blueBtn.setOnClickListener{
            drawingView?.setColor("#0000FF")
            colorDialog.dismiss()
        }
        val redBtn = colorDialog.findViewById<ImageButton>(R.id.ib_red)
        redBtn.setOnClickListener{
            drawingView?.setColor("#FF0000")
            colorDialog.dismiss()
        }
        colorDialog.show()
    }
}