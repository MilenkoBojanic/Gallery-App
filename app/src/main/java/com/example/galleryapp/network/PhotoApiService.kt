package com.example.galleryapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://zipoapps-storage-test.nyc3.digitaloceanspaces.com/"
private const val IMAGES_JSON = "image_list.json"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface PhotosApiService {
    @GET(IMAGES_JSON)
    suspend fun getPhotos(): List<PhotoData>
}

object PhotosApi {
    /**
     * lazy -> (instantiation) object creation is purposely delayed until you
     * actually need that object to avoid unnecessary computation
     */
    val retrofitService: PhotosApiService by lazy {
        retrofit.create(PhotosApiService::class.java)
    }
}