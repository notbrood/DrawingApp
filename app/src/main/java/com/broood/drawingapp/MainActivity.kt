package com.broood.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null
    val openGalleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == RESULT_OK && result.data!=null){
            val imageBackGround:ImageView = findViewById(R.id.iv_background)
            imageBackGround.setImageURI(result.data?.data)
        }
    }
    val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value

                if(isGranted){
                    if(permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE)(Toast.makeText(this@MainActivity, "Permission granted, now you can write storage Files", Toast.LENGTH_LONG).show())
                    else(Toast.makeText(this@MainActivity, "Permission granted, now you can read storage files", Toast.LENGTH_LONG).show())
                    val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)
                }
                else{
                    if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this@MainActivity, "YOU DENIED NIGGA", Toast.LENGTH_LONG).show()
                    }
                    if(permissionName == Manifest.permission.WRITE_EXTERNAL_STORAGE){
                        Toast.makeText(this@MainActivity, "YOU DENIED write NIGGA", Toast.LENGTH_LONG).show()
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
        val ibUndo: ImageButton = findViewById<ImageButton>(R.id.ib_undo)
        ibUndo.setOnClickListener{
            drawingView?.onClickUndo()
        }
        val ibSave: ImageButton = findViewById<ImageButton>(R.id.ib_save)
        ibSave.setOnClickListener{

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
            ))
        }
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )){
            showRationalDialog("Kids Drawing App", "Kids Drawing App " + "write needs to Access your Storage")
        }
        else{
            requestPermission.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
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
    private fun getBitmapFromView(view: View) : Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if(bgDrawable != null){
            bgDrawable.draw(canvas)
        }else{
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
}