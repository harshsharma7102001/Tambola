package com.world4tech.tambola.util

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import com.world4tech.tambola.R


var audioState = 0
fun playBtnClickMusic(context: Context){
    if(audioState==0){
        val mp1: MediaPlayer = MediaPlayer.create(context, R.raw.btnclick)
        mp1.start()
        Handler().postDelayed(object :Runnable{
            override fun run() {
                mp1.release()
            }
        },1000)
    }
}

fun playWinnerMusic(context:Context){
    if(audioState==0){
        val mp1: MediaPlayer = MediaPlayer.create(context, R.raw.sucess)
        mp1.start()
        Handler().postDelayed(object :Runnable{
            override fun run() {
                mp1.release()
            }
        },1000)
    }
}

fun playFailureMusic(context:Context){
    if(audioState==0){
        val mp1:MediaPlayer = MediaPlayer.create(context,R.raw.fail)
        mp1.start()
        Handler().postDelayed(object :Runnable{
            override fun run() {
                mp1.release()
            }
        },1000)
    }
}