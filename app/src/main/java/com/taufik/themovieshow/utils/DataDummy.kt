package com.taufik.themovieshow.utils

import com.taufik.themovieshow.data.Movie
import com.taufik.themovieshow.data.TvShow

object DataDummy {

    fun generateMovies(): List<Movie> {

        val movies = ArrayList<Movie>()

        movies.add(Movie(
            399566,
            "pgqgaUx1cJb5oZQQ5v0tNARCeBp.jpg",
            "Godzilla vs Kong",
            "2021-03-24",
            8.7
        ))

        movies.add(Movie(
            527774,
            "lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
            "Raya and the Last Dragon",
            "2021-03-03",
            8.7
        ))

        movies.add(Movie(
            587807,
            "6KErczPBROQty7QoIsaa6wJYXZi.jpg",
            "Tom & Jerry",
            "2021-02-11",
            7.4
        ))

        movies.add(Movie(
            464052,
            "8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
            "Wonder Woman 1984",
            "2020-12-16",
            6.8
        ))

        movies.add(Movie(
            458576,
            "1UCOF11QCw8kcqvce8LKOO6pimh.jpg",
            "Monster Hunter",
            "2020-12-03",
            7.1
        ))

        movies.add(Movie(
            544401,
            "pwDvkDyaHEU9V7cApQhbcSJMG1w.jpg",
            "Cherry",
            "2021-02-26",
            7.6
        ))

        movies.add(Movie(
            602269,
            "c7VlGCCgM9GZivKSzBgzuOVxQn7.jpg",
            "The Little Things",
            "2021-01-28",
            6.4
        ))

        movies.add(Movie(
            560144,
            "2W4ZvACURDyhiNnSIaFPHfNbny3.jpg",
            "Skylines",
            "2020-10-25",
            5.9
        ))

        movies.add(Movie(
            651571,
            "13B6onhL6FzSN2KaNeQeMML05pS.jpg",
            "Breach",
            "2020-12-17",
            4.6
        ))

        movies.add(Movie(
            529203,
            "tbVZ3Sq88dZaCANlUcewQuHQOaE.jpg",
            "The Croods: A New Age",
            "2020-11-25",
            7.5
        ))

        return movies
    }

    fun generateTvShow(): List<TvShow> {

        val tvShow = ArrayList<TvShow>()

        tvShow.add(TvShow(
            35624,
            "https://static.episodate.com/images/tv-show/thumbnail/35624.jpg",
        "The Flash",
            "The CW (US)",
        "2014-10-07",
            "Running"
        ))

        tvShow.add(TvShow(
            23455,
            "https://static.episodate.com/images/tv-show/thumbnail/23455.jpg",
        "Game of Thrones",
            "HBO (US)",
        "2011-04-17",
            "Ended"
        ))

        tvShow.add(TvShow(
            29560,
            "https://static.episodate.com/images/tv-show/thumbnail/29560.jpg",
        "Arrow",
            "The CW (US)",
        "2012-10-10",
            "Ended"
        ))

        tvShow.add(TvShow(
            43467,
            "https://static.episodate.com/images/tv-show/thumbnail/43467.com",
        "Lucifer",
            "Netflix (US)",
        "2016-01-25",
            "Running"
        ))

        tvShow.add(TvShow(
            43234,
            "https://static.episodate.com/images/tv-show/thumbnail/43234.jpg",
        "Supergirl",
            "The CW (US)",
            "2015-10-26",
            "Running"
        ))

        tvShow.add(TvShow(
            46692,
            "https://static.episodate.com/images/tv-show/thumbnail/46692.jpg",
        "DC's Legends of Tomorrow",
            "The CW (US)",
        "2016-01-21",
            "Running"
        ))

        tvShow.add(TvShow(
            24010,
            "https://static.episodate.com/images/tv-show/thumbnail/24010.jpg",
        "The Walking Dead",
            "AMC (US)",
        "2010-10-31",
        "Running"
        ))

        tvShow.add(TvShow(
            47145,
            "https://static.episodate.com/images/tv-show/thumbnail/47145.jpg",
        "Dragon Ball Super",
            "Fuji TV (JP)",
        "2015-07-05",
            "Ended"
        ))

        tvShow.add(TvShow(
            46778,
            "https://static.episodate.com/images/tv-show/thumbnail/46778.jpg",
        "Stranger Things",
            "Netflix (US)",
        "2016-07-15",
            "Running"
        ))

        tvShow.add(TvShow(
            33514,
            "https://static.episodate.com/images/tv-show/thumbnail/33514.jpg",
        "The 100",
            "The CW (US)",
        "2014-03-19",
            "Ended"
        ))

        return tvShow
    }
}