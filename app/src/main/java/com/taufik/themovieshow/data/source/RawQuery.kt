package com.taufik.themovieshow.data.source

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.taufik.themovieshow.data.local.entity.movie.FavoriteMovieEntity

class RawQuery private constructor(val value: SupportSQLiteQuery){

    companion object{
        class Builder {
            private val query = StringBuilder()
            fun selectAll(): Builder {
                query.append("SELECT * ")
                return this
            }

            fun from(tableName: String): Builder {
                query
                    .append("FROM ")
                    .append(tableName)
                    .append(" ")
                return this
            }

            fun orderBy(columnName: String): Builder {
                query
                    .append("ORDER BY ")
                    .append(columnName)
                    .append(" ")
                return this
            }

            fun <Predicate> where(action: (table: FavoriteMovieEntity) -> Predicate): Builder {
                query.append(action.invoke(FavoriteMovieEntity()))
                return this
            }

            fun build(): RawQuery {
                val simpleSQLiteQuery =  SimpleSQLiteQuery(query.toString())
                return RawQuery(simpleSQLiteQuery)
            }
        }
    }
}