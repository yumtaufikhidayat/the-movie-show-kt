package com.taufik.themovieshow.data

import android.os.Parcel
import android.os.Parcelable

data class MovieShow(
    val id: Int,
    val imagePoster: String?,
    val title: String?,
    val releaseDate: String?,
    val rate: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(imagePoster)
        parcel.writeString(title)
        parcel.writeString(releaseDate)
        parcel.writeDouble(rate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieShow> {
        override fun createFromParcel(parcel: Parcel): MovieShow {
            return MovieShow(parcel)
        }

        override fun newArray(size: Int): Array<MovieShow?> {
            return arrayOfNulls(size)
        }
    }
}
