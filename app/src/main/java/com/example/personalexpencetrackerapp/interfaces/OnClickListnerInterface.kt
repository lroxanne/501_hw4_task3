package com.example.personalexpencetrackerapp.interfaces

import com.example.personalexpencetrackerapp.roomDatabase.ExpencesEntitiy

interface OnClickListnerInterface {
    fun onClickListner(expencesEntitiy: ExpencesEntitiy, position: Int)
}