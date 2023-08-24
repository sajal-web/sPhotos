package com.sajal.university.app.sphotos.adapter

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sajal.university.app.sphotos.ImageDetails
import com.sajal.university.app.sphotos.R
import com.squareup.picasso.Picasso
import java.io.File
import java.lang.Exception

class ImageAdapter(private val context: Context,private var imageUrls : ArrayList<String>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){


    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageIV  = itemView.findViewById<ImageView>(R.id.idIVImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
       return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
       val currentUrl = imageUrls[position]
        val imgFile  = File(currentUrl)
        Log.d("Picasso",imgFile.toString())

//        if (imgFile.exists()){
//            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            holder.imageIV.setImageBitmap(bitmap)
//
//        }

        if (imgFile.exists()){
            Glide.with(context)
                .load(imgFile)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageIV)
            Log.d("imgFilePaths12",imgFile.toString())



        }

        holder.itemView.setOnClickListener {
            val intent  = Intent(context,ImageDetails::class.java)
            intent.putExtra("imgPath",imageUrls[position])
            context.startActivity(intent)

//            Picasso.get().load(imgFile)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_foreground)
//                .into(holder.imageIV)

        }

    }

}