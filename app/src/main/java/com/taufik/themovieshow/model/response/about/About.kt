package com.taufik.themovieshow.model.response.about

data class About(
    val id: AboutAction,
    val imgAbout: Int,
    val titleAbout: String,
    val descAbout: String
)

sealed class AboutAction {
    object LinkedIn : AboutAction()
    object GitHub : AboutAction()
    object Email : AboutAction()
    object GooglePlay : AboutAction()
    object LanguageSetting : AboutAction()
    object NoOp : AboutAction()
}
