package com.sajal.university.app.sphotos

import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sajal.university.app.sphotos.databinding.ActivityImageDetailsBinding
import java.io.File

 class ImageDetails : AppCompatActivity() {
    public lateinit var imageDetailsBinding: ActivityImageDetailsBinding
    private lateinit var scaleGestureDetector: ScaleGestureDetector

     private var mScaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageDetailsBinding = ActivityImageDetailsBinding.inflate(layoutInflater)
        val view = imageDetailsBinding.root
        setContentView(view)

        var url = intent.getStringExtra("imgPath")
        // Initialize ScaleGestureDetector
        scaleGestureDetector = ScaleGestureDetector(this, SelectListener(imageDetailsBinding.idIVImage))

        val imgFile = File(url)
        Log.d("Glide", imgFile.toString())
        if (imgFile.exists()) {


            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imageDetailsBinding.idIVImage.setImageBitmap(bitmap)


//            Glide.with(this)
//                .load(imgFile)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_foreground)
//                .into(imageDetailsBinding.idIVImage)


        }

    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            scaleGestureDetector.onTouchEvent(event)
        }
        return true
    }
}
class SelectListener(private val imageView: ImageView) : ScaleGestureDetector.OnScaleGestureListener {
    private var mScaleFactor = 1.0f
    private var mFocusX = 0f
    private var mFocusY = 0f
    private var matrix = Matrix()

    override fun onScale(detector: ScaleGestureDetector): Boolean {

        mScaleFactor *= detector.scaleFactor
        // Limit the scale factor to a reasonable range
        mScaleFactor = mScaleFactor.coerceIn(0.1f, 10.0f)


        // Apply scaling transformation to the image view
        imageView.scaleX = mScaleFactor
        imageView.scaleY = mScaleFactor

        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        mFocusX = detector.focusX // Update mFocusX
        mFocusY = detector.focusY // Update mFocusY
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        // Handle any cleanup or additional actions after scaling ends
    }
}




