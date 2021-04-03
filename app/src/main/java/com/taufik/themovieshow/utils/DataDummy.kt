package com.taufik.themovieshow.utils

import com.taufik.themovieshow.data.MovieShow

object DataDummy {

    fun generateMovies(): List<MovieShow> {

        val movies = ArrayList<MovieShow>()

        movies.add(MovieShow(
            399566,
            "pgqgaUx1cJb5oZQQ5v0tNARCeBp.jpg",
            "Godzilla vs Kong",
            "2021-03-24",
            8.7
        ))

        movies.add(MovieShow(
            527774,
            "lPsD10PP4rgUGiGR4CCXA6iY0QQ.jpg",
            "Raya and the Last Dragon",
            "2021-03-03",
            8.7
        ))

        movies.add(MovieShow(
            587807,
            "6KErczPBROQty7QoIsaa6wJYXZi.jpg",
            "Tom & Jerry",
            "2021-02-11",
            7.4
        ))

        movies.add(MovieShow(
            464052,
            "8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg",
            "Wonder Woman 1984",
            "2020-12-16",
            6.8
        ))

        movies.add(MovieShow(
            458576,
            "1UCOF11QCw8kcqvce8LKOO6pimh.jpg",
            "Monster Hunter",
            "2020-12-03",
            7.1
        ))

        movies.add(MovieShow(
            544401,
            "pwDvkDyaHEU9V7cApQhbcSJMG1w.jpg",
            "Cherry",
            "2021-02-26",
            7.6
        ))

        movies.add(MovieShow(
            602269,
            "c7VlGCCgM9GZivKSzBgzuOVxQn7.jpg",
            "The Little Things",
            "2021-01-28",
            6.4
        ))

        movies.add(MovieShow(
            560144,
            "2W4ZvACURDyhiNnSIaFPHfNbny3.jpg",
            "Skylines",
            "2020-10-25",
            5.9
        ))

        movies.add(MovieShow(
            651571,
            "13B6onhL6FzSN2KaNeQeMML05pS.jpg",
            "Breach",
            "2020-12-17",
            4.6
        ))

        movies.add(MovieShow(
            529203,
            "tbVZ3Sq88dZaCANlUcewQuHQOaE.jpg",
            "The Croods: A New Age",
            "2020-11-25",
            7.5
        ))

        return movies
    }

    fun generateTvShow(): List<MovieShow> {

        val tvShow = ArrayList<MovieShow>()

        tvShow.add(MovieShow(
            88396,
            "6kbAMLteGO8yyewYau6bJ683sw7.jpg",
            "The Falcon and the Winter Soldier",
            "2021-03-19",
            7.8
        ))

        tvShow.add(MovieShow(
            60735,
            "lJA2RCMfsWoskqlQhXPSLFQGXEJ.jpg",
            "The Flash",
            "2014-10-07",
            7.7
        ))

        tvShow.add(MovieShow(
            69050,
            "wRbjVBdDo5qHAEOVYoMWpM58FSA.jpg",
            "Riverdale",
            "2017-01-26",
            8.6
        ))

        tvShow.add(MovieShow(
            71712,
            "6tfT03sGp9k4c0J3dypjrI8TSAI.jpg",
            "The Good Doctor",
            "2017-09-25",
            8.6
        ))

        tvShow.add(MovieShow(
            95057,
            "6SJppowm7cdQgLkvoTlnTUSbjr9.jpg",
            "Superman & Lois",
            "2021-02-23",
            8.3
        ))

        tvShow.add(MovieShow(
            1416,
            "clnyhPqj1SNgpAdeSS6a6fwE6Bo.jpg",
            "Grey's Anatomy",
            "2005-03-27",
            8.2
        ))

        tvShow.add(MovieShow(
            1402,
            "rqeYMLryjcawh2JeRpCVUDXYM5b.jpg",
            "The Walking Dead",
            "2010-10-31",
            8.0
        ))

        tvShow.add(MovieShow(
            85271,
            "glKDfE6btIRcVB5zrjspRIs4r52.jpg",
            "WandaVision",
            "2021-01-15",
            8.5
        ))

        tvShow.add(MovieShow(
            63174,
            "4EYPN5mVIhKLfxGruy7Dy41dTVn.jpg",
            "Lucifer",
            "2014-03-19",
            8.5
        ))

        tvShow.add(MovieShow(
            117023,
            "uTFX9V2dct1nKjG6zhNiThPm8Tp.jpg",
            "Sky Rojo",
            "2021-03-19",
            7.9
        ))

        return tvShow
    }
}