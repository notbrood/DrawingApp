package com.broood.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null
    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if(isGranted){
                    Toast.makeText(this@MainActivity, "Permission granted, now you can read storage files", Toast.LENGTH_LONG).show()
                }
                else{
                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this@MainActivity, "YOU DENIED NIGGA", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }



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
        val ibGallery: ImageButton = findViewById(R.id.ib_gallery)
        ibGallery.setOnClickListener {
            requestStoragePermission()
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
    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
        )){
            showRationalDialog("Kids Drawing App", "Kids Drawing App " + "needs to Access your Storage")
        }
        else{
            requestPermission.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            // TODO - Add writing storage permission
            ))
        }
    }
    private fun showRationalDialog(
        title: String, message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _ -> dialog.dismiss()}
        builder.create().show()
    }
}