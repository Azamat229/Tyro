package com.student.tyro

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InstructorsDetailsActivityViewModel: ViewModel() {//Azamat
    private lateinit var timer: CountDownTimer
    private val _seconds = MutableLiveData<Int>()

    fun startTimer(){
        timer = object : CountDownTimer(10000, 1000){
            override fun onFinish() {
                TODO("Not yet implemented")
            }

            override fun onTick(millisUntilFinished: Long) {
                val timerLeft = millisUntilFinished/100
                _seconds.value = timerLeft.toInt()
            }
        }.start()
    }
    fun stopTimer(){
        timer.cancel()
    }
}