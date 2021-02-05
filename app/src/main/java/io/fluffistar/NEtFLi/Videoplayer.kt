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
import okhttp3.OkHttpClient
import okhttp3.Request


class Videoplayer : AppCompatActivity() {
    var  id : Long? = -1
    var serie : SelectedSerie? = null
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
        actionBar?.hide()
        id = intent.extras?.getLong("ID");
        serie = intent.extras!!.getSerializable("Serie") as SelectedSerie?





        if(serie != null) {
            val video_view = findViewById<VideoView>(R.id.videoplayer)

            val mediaController = MediaController(this)
            mediaController.setAnchorView(video_view)

            Thread{
                val url = Verwaltung.getKey(Verwaltung.main + serie!!.SeasonsList[0].episodes[id!!.toInt()].links[0].link)

                val client = OkHttpClient()

                val token = "SSTOSESSION=viagchs10q0aq03gofo4104iab"

                val request = Request.Builder()
                    .url(url)
                    .addHeader("Cookie", token)
                    .get()
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body()!!.string()
                Log.d("URL==", (responseBody))
                val input = vivo(responseBody)
        //source: ' // ',

                runOnUiThread {
                    video_view.setVideoPath("$input")
                    video_view.setMediaController(mediaController)
                    video_view.requestFocus()

                    video_view.start()
                }

            }.start()


        }
    }

    fun vivo(str: String):String{

      val regex =  """(?<=source: ')(.*)(?=',)""".toRegex()
        val cryptlink = regex.find(str)?.value.orEmpty()
        var input : String = cryptlink
        input = input.replace("%5E", "-");
        input = input.replace("i", ":");


        input = input.replace("-", "/");

        // input = input.replace("G%405", "vod");
        input = input.replace("%5D", ".");
        input = input.replace("%40", "o");
        input = input.replace("%5C", "-");


        input = input.replace("%3A", "i");
        input = input.replace("%3F", "n");
        input = input.replace("%3D", "l");


        input = input.replace("%2A", "Y");
        input = input.replace("%2B", "Z");
        input = input.replace("%29", "X");
        input = input.replace("%28", "W");
        input = input.replace("%24", "S");
        input = input.replace("%22", "Q");
        input = input.replace("%21", "P");
        input = input.replace("%26", "U");
        input = input.replace("%3B", "j");
        input = input.replace("%7D", "N");

        input = input.replace("%27", "V");
        input = input.replace("%3C", "k");
        input = input.replace("%7E", "O");
        input = input.replace("%7C", "M");
        input = input.replace("%7B", "L");
        input = input.replace("%25", "T");
        input = input.replace("%3E", "m");
        input = input.replace("%23", "R");
        input = Swap(input, "F", "u");
        input = input.replace("%60", "1");
        input = Swap(input, "I", "x");
        input = Swap(input, "D", "s");
        input = Swap(input, "C", "r");
        input = Swap(input, "A", "p");
        input = Swap(input, "K", "z");
        input = Swap(input, "y", "J");
        input = Swap(input, "B", "q");
        input = Swap(input, "_", "0");
        input = Swap(input, "E", "t");
        input = Swap(input, "2", "a");
        input = Swap(input, "H", "w");
        input = Swap(input, "G", "v");
        input = Swap(input, "b", "3");
        input = Swap(input, "d", "5");
        input = Swap(input, "e", "6");
        input = Swap(input, "f", "7");
        input = Swap(input, "g", "8");
        input = Swap(input, "h", "9");
        input = Swap(input, "c", "4");
        Log.d("URL==2", input)
        return  input
    }
    fun Swap(main: String, a: String, b: String): String {
        var x = main
        val p: String = "~"
        x = x.replace(a, p)
        x = x.replace(b, a)
        x = x.replace(p, b)
        return x
    }
}