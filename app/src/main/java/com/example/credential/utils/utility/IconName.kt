package com.example.credential.utils.utility

object IconName {
    const val TWITTER = "ic_twitter"
    const val DRIVE = "ic_drive"
    const val DEFAULT = "ic_default"
    const val FACEBOOK = "ic_facebook"
    const val GITHUB = "ic_github"
    const val GMAIL = "ic_gmail"
    const val GOOGLE_COLOR = "ic_google_color"
    const val GOOGLE_PHOTOS = "ic_google_photos"
    const val INSTAGRAM = "ic_instagram"
    const val LINKEDIN = "ic_linkedin"
    const val MESSENGER = "ic_messenger"
    const val WHATSAPP = "ic_whatsapp"
    const val WIFI = "ic_wifi"

    fun getAllIcons(): List<String> {
        return listOf(
            TWITTER, DRIVE, FACEBOOK,
            GITHUB, GMAIL, GOOGLE_COLOR, GOOGLE_PHOTOS,
            INSTAGRAM, LINKEDIN, MESSENGER, WHATSAPP, WIFI
        )
    }
}