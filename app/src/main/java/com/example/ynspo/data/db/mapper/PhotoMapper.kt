package com.example.ynspo.data.db.mapper

import com.example.ynspo.data.db.entity.PhotoEntity
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls

object PhotoMapper {

    fun toEntity(photo: UnsplashPhoto): PhotoEntity {
        return PhotoEntity(
            id = photo.id,
            description = photo.description,
            smallUrl = photo.urls.small,
            regularUrl = photo.urls.regular,
            fullUrl = photo.urls.full
        )
    }

    fun fromEntity(entity: PhotoEntity): UnsplashPhoto {
        return UnsplashPhoto(
            id = entity.id,
            description = entity.description,
            urls = UnsplashPhotoUrls(
                small = entity.smallUrl,
                regular = entity.regularUrl,
                full = entity.fullUrl
            )
        )
    }

    fun fromEntities(entities: List<PhotoEntity>): List<UnsplashPhoto> {
        return entities.map { fromEntity(it) }
    }

    fun toEntities(photos: List<UnsplashPhoto>): List<PhotoEntity> {
        return photos.map { toEntity(it) }
    }
}
