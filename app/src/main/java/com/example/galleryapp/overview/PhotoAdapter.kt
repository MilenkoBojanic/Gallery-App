package com.example.galleryapp.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.galleryapp.databinding.GridViewItemBinding
import com.example.galleryapp.network.PhotoData
import com.example.imageslibrary.ImageLoader

class PhotoAdapter() :
    ListAdapter<PhotoData, PhotoAdapter.MarsPhotoViewHolder>(DiffCallBack) {

    class MarsPhotoViewHolder(
        private var binding: GridViewItemBinding,
        private val context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photoData: PhotoData) {
            val imageLoader = ImageLoader.getInstance(context)
            val bitmap = imageLoader.getImage(photoData.imageUrl, photoData.id)
            if (bitmap != null) {
                binding.bitmap = bitmap
            } else {
                binding.photoUrl = photoData.imageUrl
            }
            binding.imageId = photoData.id
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarsPhotoViewHolder {
        return MarsPhotoViewHolder(
            GridViewItemBinding.inflate(LayoutInflater.from(parent.context)),
            context = parent.context
        )
    }

    override fun onBindViewHolder(holder: MarsPhotoViewHolder, position: Int) {
        val marsPhoto = getItem(position)
        holder.bind(marsPhoto)
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<PhotoData>() {
        override fun areItemsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }
    }
}