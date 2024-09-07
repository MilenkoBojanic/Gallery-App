package com.example.galleryapp.overview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.galleryapp.R
import com.example.galleryapp.network.PhotoData

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("bitmap")
fun bindImage(imgView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        imgView.load(bitmap) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<PhotoData>?) {
    val adapter = recyclerView.adapter as PhotoAdapter
    adapter.submitList(data)
}

@BindingAdapter("photoApiStatus")
fun bindStatus(statusImageView: ImageView, status: PhotoApiStatus) {
    when (status) {
        PhotoApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }

        PhotoApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }

        PhotoApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("imageId")
fun bindText(txtView: TextView, imgId: Int?) {
    imgId?.let {
        txtView.text = "ID $it"
    }
}