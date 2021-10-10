package com.example.educhat.Model

import android.os.Parcel
import android.os.Parcelable

data class McqsModel(
    val optionA:String=" ",val optionB:String=" ",val optionC:String=" ",val optionD:String=" ",val correctOption:String=" ",val question:String=" "):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(optionA)
        parcel.writeString(optionB)
        parcel.writeString(optionC)
        parcel.writeString(optionD)
        parcel.writeString(correctOption)
        parcel.writeString(question)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<McqsModel> {
        override fun createFromParcel(parcel: Parcel): McqsModel {
            return McqsModel(parcel)
        }

        override fun newArray(size: Int): Array<McqsModel?> {
            return arrayOfNulls(size)
        }
    }
}