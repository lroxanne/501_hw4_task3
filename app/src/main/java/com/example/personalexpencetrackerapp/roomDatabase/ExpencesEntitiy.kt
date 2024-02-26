package com.example.personalexpencetrackerapp.roomDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expencesTable")
data class ExpencesEntitiy(

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "amount")
    var amount: String? = null,

    @ColumnInfo(name = "category")
    var category: String? = null,

    ) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null

}