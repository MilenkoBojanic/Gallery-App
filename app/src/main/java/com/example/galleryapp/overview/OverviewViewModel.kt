package com.example.galleryapp.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.network.PhotoData
import com.example.galleryapp.network.PhotosApi
import kotlinx.coroutines.launch

enum class PhotoApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    private val _status = MutableLiveData<PhotoApiStatus>()
    val status: LiveData<PhotoApiStatus> = _status

    private val _photos = MutableLiveData<List<PhotoData>>()
    val photos: LiveData<List<PhotoData>> = _photos

    init {
        getPhotos()
    }

    private fun getPhotos() {
        viewModelScope.launch {
            _status.value = PhotoApiStatus.LOADING
            try {
                _photos.value = PhotosApi.retrofitService.getPhotos()
                _status.value = PhotoApiStatus.DONE
            } catch (e: Exception) {
                _status.value = PhotoApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }
}