package io.fluffistar.NEtFLi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.Serializer.Links
import io.fluffistar.NEtFLi.Serializer.SelectedSerie


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
    var _more : ImageView

    var SelectedEpisodes :Int
        get () =  Verwaltung.SelectedSerie!!.SelectedEpisode
        set  (value)  { Verwaltung.SelectedSerie!!.SelectedEpisode = value }
  var SelectedSeason : Int
    get() =  Verwaltung.SelectedSerie!!.SelectedSeason
    set(value) { Verwaltung.SelectedSerie!!.SelectedSeason = value;  }


     class MyJavaScriptInterface(val activity: Activity) {
        @SuppressWarnings("unused")
        @JavascriptInterface
        fun processHTML(html: String?) {
           Log.d("MYHTML", html!!)

            when(hoster){

                "Vivo" -> {
                    var src = vivo(html)
                    Log.d("MYSRC", src)
                    if (src != "")
                        if (!VideoPlayerPro.loaded) {
                            VideoPlayerPro.loaded = true
                            Log.d("MYSRC", src)
                            activity.runOnUiThread {
                                _video.stopPlayback()
                                _video.setVideoURI(Uri.parse(src))
                                myurl = src
                                _video.start()
                            }
                        }
                }
                "VOE" -> {
                    var src = veo(html)
                    Log.d("MYSRC", src)
                    if (src != "")
                        if (!VideoPlayerPro.loaded) {
                            VideoPlayerPro.loaded = true
                            Log.d("MYSRC", src)
                            activity.runOnUiThread {
                                _video.stopPlayback()
                                _video.setVideoURI(Uri.parse(src))
                                myurl = src
                                _video.start()
                            }
                        }
                }
                "Streamtape" -> {
                    var src = streamtape(html)
                    Log.d("MYSRC", src)
                    if (src != "")
                        if (!VideoPlayerPro.loaded) {
                            VideoPlayerPro.loaded = true
                            Log.d("MYSRC", src)
                            activity.runOnUiThread {
                                _video.stopPlayback()
                                _video.setVideoURI(Uri.parse(src))
                                myurl = src
                                _video.start()
                            }
                        }
                }
                "Vidoza" -> {
                    var src = vidoza(html)
                    Log.d("MYSRC", src)
                    if (src != "")
                        if (!VideoPlayerPro.loaded) {
                            VideoPlayerPro.loaded = true
                            Log.d("MYSRC", src)
                            activity.runOnUiThread {
                                _video.stopPlayback()
                                _video.setVideoURI(Uri.parse(src))
                                myurl = src

                                _video.start()

                            }
                        }
                }

            }

    }}

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
    _more = findViewById(R.id.openwith)
            val webSettings: WebSettings = _webview.settings
            _webview.addJavascriptInterface(MyJavaScriptInterface((context as Activity)), "HTMLOUT")
            webSettings.javaScriptEnabled = true
            _webview.webViewClient = object : WebViewClient() {


                override fun onPageFinished(view: WebView, url: String) {
                    Log.d("MYHTML", "FINISHED")

                    loaded = false
                    view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")


                }
            }

        _next.setOnClickListener { if (loaded) next() }


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

            _languagecombo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                    changedcombo()
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
            _hostercombo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                    changedcombo()
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

            _video.setOnPreparedListener {  total_duration = _video.duration
                _maxtime.text = timeConversion(total_duration.toLong())
                _curtime.text = timeConversion(current_pos.toLong())
                _timeline.max = total_duration
                _video.start()
                _play.setImageResource(R.drawable.ic_pause)
                _video.setOnCompletionListener { if (loaded) next() }


            }

            _more.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(Uri.parse(myurl ), "video/*")
                (context as Activity).startActivity(intent)
            }

    }


    private var avaibleHoster : List<Links> = mutableListOf()
    private var SelectedLanguage : List<Links> = mutableListOf()
    private var GermanLanguage : List<Links> = mutableListOf()
    private var EnglishLanguage : List<Links> = mutableListOf()
    private var SubLanguage : List<Links> = mutableListOf()
    private val allLanguages : MutableList<List<Links>> = mutableListOf()
    private  val lags : MutableList<String> = mutableListOf()
    private  val host : MutableList<String> = mutableListOf()

    fun init(){

        _hostercombo.setSelection(0)
        avaibleHoster = Verwaltung.SelectedSerie!!.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].links
        current_pos = _video.currentPosition

        _title.text = if(Verwaltung.SelectedSerie!!.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].german != "") Verwaltung.SelectedSerie!!.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].german else Verwaltung.SelectedSerie!!.SeasonsList[SelectedSeason].episodes[SelectedEpisodes].english
Thread {
    GermanLanguage =
        avaibleHoster.filter { it.language == 1 && Verwaltung._hosternames.contains(it.hosterTitle) }
    EnglishLanguage =
        avaibleHoster.filter { it.language == 2 && Verwaltung._hosternames.contains(it.hosterTitle) }
    SubLanguage =
        avaibleHoster.filter { it.language == 3 && Verwaltung._hosternames.contains(it.hosterTitle) }
    if (GermanLanguage.isNotEmpty()) {
        allLanguages.add(GermanLanguage)
        lags.add("German")
    }
    if (EnglishLanguage.isNotEmpty()) {
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

        _languagecombo.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, lags)
        _hostercombo.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, host)


        changedcombo()
    }

}.start()
        //display video duration


        updateinit()
    }

    fun changedcombo(){
        loaded  = false
        SelectedLanguage = allLanguages[_languagecombo.selectedItemPosition]

        hoster = SelectedLanguage[_hostercombo.selectedItemPosition].hoster
        Log.d("Hoster${_hostercombo.selectedItemPosition}", Verwaltung.getKey(Verwaltung.main + SelectedLanguage[_hostercombo.selectedItemPosition].link))

        CookieManager.getInstance().setCookie(
                Verwaltung.main,
                "SSTOSESSION=${Verwaltung.Session}"
        );
        _webview.webViewClient = object : WebViewClient() {


            override fun onPageFinished(view: WebView, url: String) {
                Log.d("MYHTML", "FINISHED")



                view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")


            }
        }
    Thread{
        Verwaltung.addwatch(Verwaltung.SelectedSerie!!,context)}.start()

        _webview.loadUrl(Verwaltung.getKey(Verwaltung.main + SelectedLanguage[_hostercombo.selectedItemPosition].link))
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
                        current_pos = _video.currentPosition
                        _curtime.text = timeConversion(current_pos.toLong())
                        _timeline.progress = current_pos
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
    fun play(src: String){
        _video.setVideoURI(Uri.parse(src))
        _video.start()
        _play.setImageResource(R.drawable.ic_pause)
    }

    fun setup(serie: SelectedSerie?){

        init()

    }
    fun next(){
        loaded = false
        allLanguages.clear()
        lags.clear()
        host.clear()
        if (SelectedEpisodes+1 < Verwaltung.SelectedSerie!!.SeasonsList[SelectedSeason].episodes.size )
        {
            SelectedEpisodes++;
            init()
        }
        else
        {
            if (SelectedSeason + 1 < Verwaltung.SelectedSerie!!.SeasonsList.size )
            {
                if (Verwaltung.SelectedSerie!!.SeasonsList[SelectedSeason + 1].name != "Special" || Verwaltung.Settings.autplay)
                {
                    SelectedSeason++;
                    SelectedEpisodes = 0;
                    init()
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
    private var current_pos : Int = 0
    private var total_duration : Int =0

    constructor(context: Context):super(context){

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
        _more = findViewById(R.id.openwith)
        val webSettings: WebSettings = _webview.settings
        _webview.addJavascriptInterface(MyJavaScriptInterface((context as Activity)), "HTMLOUT")
        webSettings.javaScriptEnabled = true
        _webview.webViewClient = object : WebViewClient() {


            override fun onPageFinished(view: WebView, url: String) {
                Log.d("MYHTML", "FINISHED")



                view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")


            }
        }

        _next.setOnClickListener { if (loaded) next() }


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
        _languagecombo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                _video.stopPlayback()
                loaded = false
                changedcombo()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
        _hostercombo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                _video.stopPlayback()
                loaded = false
                changedcombo()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }
        _video.setOnPreparedListener {  total_duration = _video.duration
            _maxtime.text = timeConversion(total_duration.toLong())
            _curtime.text = timeConversion(current_pos.toLong())
            _timeline.max = total_duration
            _video.start()
            _play.setImageResource(R.drawable.ic_pause)
            _video.setOnCompletionListener { if (loaded) next() }


        }


    }





























companion object{
    var loaded = false ;
    var hoster = ""
    var myurl = ""
    @SuppressLint("StaticFieldLeak")
    lateinit var _video : VideoView


    fun getBetween(html: String, start: String, end: String) : String{

        val regex =  """(?<=$start)(.*)(?=$end)""".toRegex()
        val cryptlink = regex.find(html)?.value.orEmpty()
        return  cryptlink
    }


    fun veo(str: String) : String{
     return   getBetween(str, "\"mp4\": \"", "\",")

    }

    fun vidoza(str: String) : String{

        val videourl: String = getBetween(str, "src: \"", "\",").replace("\"", "")



        return videourl.trim()
    }

    fun streamtape(str: String) : String {
        var videourl: String =  getBetween(str, "id=\"videolink\"", "iv>")

        videourl =  getBetween(videourl, "//", "</d").replace("amp;", "")




        return "https://" + videourl.trim()
    }

    fun vivo(str: String):String{


        var input : String = getBetween(str, "source: '", "',")
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
