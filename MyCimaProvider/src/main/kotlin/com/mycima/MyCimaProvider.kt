package com.yourname.cinemana

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*

class CinemanaProvider : MainAPI() {
    override var mainUrl = "https://cinemana.shabakaty.com"
    override var name = "Cinemana"
    override val hasMainPage = true
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$mainUrl/ar/search/$query"
        val document = app.get(url).document

        return document.select(".movie-card").mapNotNull {
            val title = it.selectFirst(".movie-title")?.text() ?: return@mapNotNull null
            val href = it.selectFirst("a")?.attr("href") ?: return@mapNotNull null
            val poster = it.selectFirst("img")?.attr("src")
            val type = if (href.contains("series")) TvType.TvSeries else TvType.Movie

            MovieSearchResponse(
                title,
                fixUrl(href),
                this.name,
                type,
                fixUrl(poster),
                null,
                null
            )
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document
        val title = document.selectFirst("h1")?.text() ?: "No title"
        val poster = document.selectFirst(".poster img")?.attr("src")
        val description = document.selectFirst(".description")?.text()

        val episodes = document.select(".episode-list a").map {
            val name = it.text()
            val link = fixUrl(it.attr("href"))
            Episode(link, name)
        }

        return if (url.contains("series")) {
            TvSeriesLoadResponse(
                title,
                url,
                this.name,
                TvType.TvSeries,
                episodes,
                fixUrl(poster),
                null,
                description,
                null
            )
        } else {
            MovieLoadResponse(
                title,
                url,
                this.name,
                TvType.Movie,
                fixUrl(poster),
                null,
                description,
                null
            )
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val doc = app.get(data).document
        val iframe = doc.selectFirst("iframe")?.attr("src") ?: return
        // ستحتاج لاستخراج الفيديو من داخل iframe لاحقاً

        callback.invoke(
            ExtractorLink(
                this.name,
                "Cinemana Player",
                iframe,
                this.name,
                quality = Qualities.Unknown
            )
        )
    }
}
