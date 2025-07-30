package com.example.ynspo.data.model

//Interfaz común para unificar UnsplashPhoto y UserPin en el feed
interface InspirationItem {
    val id: String
    val description: String?
    val urls: InspirationUrls
    val isUserPin: Boolean
}

interface InspirationUrls {
    val small: String
    val regular: String
    val full: String
}

//Convertir UnsplashPhoto a InspirationItem
fun UnsplashPhoto.toInspirationItem(): InspirationItem = object : InspirationItem {
    override val id: String = this@toInspirationItem.id
    override val description: String? = this@toInspirationItem.description
    override val urls: InspirationUrls = object : InspirationUrls {
        override val small: String = this@toInspirationItem.urls.small
        override val regular: String = this@toInspirationItem.urls.regular
        override val full: String = this@toInspirationItem.urls.full
    }
    override val isUserPin: Boolean = false
}

//Convertir UserPin a InspirationItem
fun UserPin.toInspirationItem(): InspirationItem = object : InspirationItem {
    override val id: String = this@toInspirationItem.id
    override val description: String? = this@toInspirationItem.description
    override val urls: InspirationUrls = object : InspirationUrls {
        override val small: String = this@toInspirationItem.urls.small
        override val regular: String = this@toInspirationItem.urls.regular
        override val full: String = this@toInspirationItem.urls.full
    }
    override val isUserPin: Boolean = true
}

//Convertir InspirationItem a UnsplashPhoto
fun InspirationItem.toUnsplashPhoto(): UnsplashPhoto {
    return UnsplashPhoto(
        id = this.id,
        description = this.description,
        urls = UnsplashPhotoUrls(
            small = this.urls.small,
            regular = this.urls.regular,
            full = this.urls.full
        )
    )
} 