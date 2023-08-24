package com.sajal.university.app.sphotos
import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.sajal.university.app.sphotos.adapter.ImageAdapter
import com.sajal.university.app.sphotos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imagePaths : ArrayList<String>

    companion object {
        private const val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
        private const val READ_MEDIA_IMAGES_PERMISSION_PERMISSION_REQUEST_CODE = 0
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)
        imagePaths = ArrayList()
        checkPermissions()

    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show()
            getImagePaths()
        }else{
            Toast.makeText(this,"Request the Permisseion request",Toast.LENGTH_SHORT).show()
            requestPermission()
            Log.d("permissionRelated","request the permission")
        }

    }
    private fun getImagePaths() {

        // first check if there have any sd card in the device
        val isSdPresent : Boolean = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)
        if (isSdPresent){
            // if the sd card is present we are creating a new line list in which we are getting our images data with
            // their id's
            val columns  = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID)
            // here we are creating a new string to order our images by their ids
            val orderBy = MediaStore.Images.Media._ID
            // this method will store all the images from the gallery in a cursore
            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            columns,
                null,
                null,
                orderBy
            )
            // below line is to get the total number of images
            val count = cursor?.count ?: 0
            // below line we are running a loop to add the image file path to our arraylist
                for (i in 0 until count){
                    // moving our cursor position
                    cursor?.moveToPosition(i)
                    // getting the image file path
                    val dataColumnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
                        // we got the image path now add that path to our arraylist
                    dataColumnIndex.let {
                        val imagePath = dataColumnIndex?.let { it1 -> cursor?.getString(it1) }
                        if (imagePath != null) {
                            imagePaths.add(imagePath)
                        }
                    }

                }
            Log.d("getImagePaths",imagePaths.toString())

            prepareRecyclerView()
            imageAdapter.notifyDataSetChanged()
            cursor?.close()
        }
    }
    private fun requestPermission() {
        Toast.makeText(this,"Permisseion request",Toast.LENGTH_SHORT).show()
        if (android.os.Build.VERSION.SDK_INT >= 30){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                READ_MEDIA_IMAGES_PERMISSION_PERMISSION_REQUEST_CODE
            )
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
                )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("requestCode",requestCode.toString())
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_MEDIA_IMAGES_PERMISSION_PERMISSION_REQUEST_CODE || requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getImagePaths()
            Toast.makeText(this,"Permission granted...%$requestCode",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Permission not granted..",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun prepareRecyclerView() {
        activityMainBinding.idRVImages.layoutManager = GridLayoutManager(this,5)
         imageAdapter = ImageAdapter(this,imagePaths)
        activityMainBinding.idRVImages.adapter=imageAdapter
    }
}