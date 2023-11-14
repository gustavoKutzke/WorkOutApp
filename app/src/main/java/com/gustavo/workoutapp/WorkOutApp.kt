package com.gustavo.workoutapp

import android.app.Application

class WorkOutApp:Application() {

    val db by lazy {
        HistoryDatabase.getIntance(this)
    }

}