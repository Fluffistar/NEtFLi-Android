package io.fluffistar.NEtFLi

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.webkit.*
import android.widget.*
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.Serializer.Links
import io.fluffistar.NEtFLi.Serializer.SelectedSerie
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * TODO: document your custom view class.
 */
class VideoPlayerPro : RelativeLayout {

    var _webview : WebView

    var _play : ImageView
    var _top : RelativeLayout
    var _bar : RelativeLayout
    var _skipforward : ImageView
    var _skipbackward : ImageView
    var _backbtn : ImageView
    var _title : TextView
    var _next : ImageView
    var _maxtime : TextView
    var _curtime : TextView
    var _languagecombo : Spinner
    var _hostercombo : Spinner
    var _timeline : SeekBar
    lateinit var _serie:SelectedSerie
    var SelectedEpisodes :Int
        get () =  _serie.SelectedEpisode
        set  (value)  { _serie.SelectedEpisode = value }
  var SelectedSeason : Int
    get() =  _serie.SelectedSeason
    set(value) { _serie.SelectedSeason = value;  }


     class MyJavaScriptInterface {
        @SuppressWarnings("unused")
        @JavascriptInterface
        fun processHTML(html: String?) {
           Log.d("MYHTML" , html!!)
            VideoPlayerPro.html  =html
            var src = vivo(html)
             if(src!= ""){
              VideoPlayerPro.loaded = true
              Log.d("MYSRC" ,src)
                 _video.setVideoURI(Uri.parse(src))
                 _video.start()
          }
        }
    }

        constructor(context: Context, attrs: AttributeSet):super(context, attrs){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.sample_video_player_pro, this)
        _webview = WebView(context)

        _top = findViewById(R.id.topbar)
        _bar = findViewById(R.id.bottombar)
        _video = findViewById(R.id.videoplayer2)
        _play = findViewById(R.id.playbtn)
        _skipbackward = findViewById(R.id.skipbackward)
        _skipforward = findViewById(R.id.skipforward)
        _backbtn = findViewById(R.id.backbtn)
        _title = findViewById(R.id.title_videoplayer)
        _next = findViewById(R.id.nextbtn)
        _maxtime = findViewById(R.id.secondtext)
        _curtime = findViewById(R.id.firsttext)
        _languagecombo = findViewById(R.id.languagecombo)
        _hostercombo = findViewById(R.id.Hostercombo)
        _timeline = findViewById(R.id.seekbar)

            val webSettings: WebSettings = _webview.getSettings()
            _webview.addJavascriptInterface(MyJavaScriptInterface(), "HTMLOUT")
            webSettings.javaScriptEnabled = true
            _webview.setWebViewClient(object : WebViewClient() {


                override fun onPageFinished(view: WebView, url: String) {
                    Log.d("MYHTML", "FINISHED")



                        view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")


                }
            })




        _backbtn.setOnClickListener {

            back()

        }
        _top.setOnClickListener { _top.visibility = VISIBLE
        _bar.visibility = VISIBLE
            count = 0
        }
        _bar.setOnClickListener { _top.visibility = VISIBLE
            _bar.visibility = VISIBLE}
        _video.setOnClickListener { _top.visibility = VISIBLE
            _bar.visibility = VISIBLE
        count = 0
        }
        _skipforward.setOnClickListener { _video.seekTo(_video.currentPosition + 10000)
        _video.start()
        }
        _skipbackward.setOnClickListener {_video.seekTo(_video.currentPosition - 10000)
            _video.start()}

        _play.setOnClickListener {

            if(_video.isPlaying) {
                _video.pause()
                _play.setImageResource(R.drawable.ic_play)
            }
            else {
                _video.start()
                _play.setImageResource(R.drawable.ic_pause)
            }
        }
            _video.setOnPreparedListener {  total_duration = _video.getDuration()
                _maxtime.setText(timeConversion(total_duration.toLong()))
                _curtime.setText(timeConversion(current_pos.toLong()))
                _timeline.setMax(total_duration)
                _video.start()
                _play.setImageResource(R.drawable.ic_pause)
                _video.setOnCompletionListener { next() }
            }



    }

    var avaibleHoster : List<Links> = mutableListOf()
    var SelectedLanguage : List<Links> = mutableListOf()
    var GermanLanguage : List<Links> = mutableListOf()
    var EnglishLanguage : List<Links> = mutableListOf()
    var SubLanguage : List<Links> = mutableListOf()
    var allLanguages : MutableList<List<Links>> = mutableListOf()
    private  var lags : MutableList<String> = mutableListOf()
    private  var host : MutableList<String> = mutableListOf()
    fun init(){
        _hostercombo.setSelection(0)
        avaibleHoster = _serie.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].links
        current_pos = _video.getCurrentPosition()

        _title.text = if(_serie.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].german != "") _serie.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].german else _serie.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].english
Thread {
    GermanLanguage =
        avaibleHoster.filter { it.language == 1 && Verwaltung._hosternames.contains(it.hosterTitle) }
    EnglishLanguage =
        avaibleHoster.filter { it.language == 2 && Verwaltung._hosternames.contains(it.hosterTitle) }
    SubLanguage =
        avaibleHoster.filter { it.language == 3 && Verwaltung._hosternames.contains(it.hosterTitle) }
    if (SubLanguage.isNotEmpty()) {
        allLanguages.add(GermanLanguage)
        lags.add("German")
    }
    if (SubLanguage.isNotEmpty()) {
        allLanguages.add(EnglishLanguage)
        lags.add("English")
    }
    if (SubLanguage.isNotEmpty()) {
        allLanguages.add(SubLanguage)
        lags.add("German Sub")
    }
    if (allLanguages.size == 0) (context as Activity).finish()
    SelectedLanguage = allLanguages[0]

    for (i in SelectedLanguage)
        host.add(i.hosterTitle)
    (context as Activity).runOnUiThread {
        _hostercombo.setSelection(0)
        _languagecombo.setSelection(0)
    }
    (context as Activity).runOnUiThread {
        _languagecombo.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, lags)
        _hostercombo.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, host)
        CookieManager.getInstance().setCookie(
            Verwaltung.main,
            "SSTOSESSION=viagchs10q0aq03gofo4104iab"
        );
        Log.d("URLNOW", Verwaltung.getKey(Verwaltung.main + SelectedLanguage[0].link))
        _webview.loadUrl(Verwaltung.getKey(Verwaltung.main + SelectedLanguage[0].link))
    }

}.start()
        //display video duration


        updateinit()
    }
    var count : Int = 0
    fun updateinit(){
        val activity = context as Activity
        Thread {

            if(_video.isPlaying)
                try {
                    activity.runOnUiThread() {
                        //does actions on Ui-Thread u neeed it because Ui-elements can only be edited in Main/Ui-Thread
                        count ++
                        current_pos = _video.getCurrentPosition()
                        _curtime.setText(timeConversion(current_pos.toLong()))
                        _timeline.setProgress(current_pos)
                        if(count > 6){
                            _bar.visibility = GONE
                            _top.visibility = GONE
                        }

                    }

                } catch (ed: IllegalStateException) {
                    ed.printStackTrace()
                }

            Thread.sleep(500)
            updateinit()

        }.start()

    }
    fun back(){


        (context as Activity).finish()

    }
    fun finished(html: String){
        var src = ""
        _video.setVideoURI(Uri.parse(src))
        _video.start()
        _play.setImageResource(R.drawable.ic_pause)

    }
    fun play(src : String){
        _video.setVideoURI(Uri.parse(src))
        _video.start()
        _play.setImageResource(R.drawable.ic_pause)
    }

    fun setup(  serie: SelectedSerie?){
            _serie = serie!!
        init()

    }
    fun next(){
        if (SelectedEpisodes+1 < _serie.SeasonsList[SelectedSeason].episodes.size )
        {
            SelectedEpisodes++;

        }
        else
        {
            if (SelectedSeason + 1 < _serie.SeasonsList.size )
            {
                if (_serie.SeasonsList[SelectedSeason + 1].name != "Special")
                {
                    SelectedSeason++;
                    SelectedEpisodes = 0;

                }
                else
                back()
//else do something else like Series finished or so


            } else
               back()
        }
    }
    fun timeConversion(value: Long): String? {
        val songTime: String
        val dur = value.toInt()
        val hrs = dur / 3600000
        val mns = dur / 60000 % 60000
        val scs = dur % 60000 / 1000
        songTime = if (hrs > 0) {
            String.format("%02d:%02d:%02d", hrs, mns, scs)
        } else {
            String.format("%02d:%02d", mns, scs)
        }
        return songTime
    }
    var current_pos : Int = 0
    var total_duration : Int =0

    constructor(context: Context):super(context){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.sample_video_player_pro, this)
        _webview = WebView(context)
        _video = findViewById(R.id.videoplayer2)
        _play = findViewById(R.id.playbtn)
        _skipbackward = findViewById(R.id.skipbackward)
        _skipforward = findViewById(R.id.skipforward)
        _backbtn = findViewById(R.id.backbtn)
        _title = findViewById(R.id.title_videoplayer)
        _next = findViewById(R.id.nextbtn)
        _maxtime = findViewById(R.id.secondtext)
        _curtime = findViewById(R.id.firsttext)
        _languagecombo = findViewById(R.id.languagecombo)
        _hostercombo = findViewById(R.id.Hostercombo)
        _timeline = findViewById(R.id.seekbar)
        _top = findViewById(R.id.topbar)
        _bar = findViewById(R.id.bottombar)

        _video.setOnPreparedListener(OnPreparedListener {

            init()
        })
            _timeline?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                    Toast.makeText(
                        context,
                        "seekbar progress: $progress",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                    Toast.makeText(context, "seekbar touch started!", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    Toast.makeText(context, "seekbar touch stopped!", Toast.LENGTH_SHORT)
                        .show()
                }
            })

        _backbtn.setOnClickListener {

            back()

        }


        _play.setOnClickListener {

            if(_video.isPlaying) {
                _video.pause()
                _play.setImageResource(R.drawable.ic_play)
            }
            else {
                _video.start()
                _play.setImageResource(R.drawable.ic_pause)
            }
        }

        _video.setOnCompletionListener { next() }

    }





























companion object{
    var loaded = false ;
    var html = ""
    lateinit var _video : VideoView
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



}
