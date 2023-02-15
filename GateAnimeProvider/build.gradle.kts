plugins {
    id("org.jetbrains.kotlin.android")
}
version = 3
dependencies {
    implementation("androidx.core:core-ktx:+")
}

cloudstream {
    description = ""
    authors = listOf( "ImZaw" )

	language = "ar"
	
    status = 1

    tvTypes = listOf( "Anime", "AnimeMovie", "Cartoon" )

    iconUrl = "https://b.gateanime.cam/wp-content/uploads/2020/12/cropped-Favicon-192x192.png"
}