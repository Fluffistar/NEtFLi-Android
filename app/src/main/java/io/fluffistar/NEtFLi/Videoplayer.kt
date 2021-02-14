package io.fluffistar.NEtFLi

import android.R.string
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.Serializer.SelectedSerie
import io.fluffistar.NEtFLi.Serializer.Serie
import okhttp3.OkHttpClient
import okhttp3.Request


class Videoplayer : AppCompatActivity() {
    var  id : Long? = -1
    var season : Long ? = -1
    var serie : Serie? = null
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videoplayer)
    //    setSupportActionBar(findViewById(R.id.toolbar))

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.


        serie = Verwaltung.SelectedSerie





        if(serie != null) {
            val video_view = findViewById<VideoPlayerPro>(R.id.videoplayer)

        /*    serie?.SelectedSeason =  season!!.toInt()-1
            serie?.SelectedEpisode = id!!.toInt() -1
*/
            video_view.setup(  serie)

        }
    }


}