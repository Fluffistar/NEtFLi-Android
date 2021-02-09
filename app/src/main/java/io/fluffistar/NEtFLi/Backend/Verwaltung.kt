package io.fluffistar.NEtFLi.Backend

import android.R.attr
import android.content.Context
import android.os.Environment
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import io.fluffistar.NEtFLi.Serializer.*
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.io.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class HosterName(val id: Int, val name: String)
class Language(val id: Int, val lang: String)

class Verwaltung {
    companion object{
        val main ="https://s.to/"
        var SelectedSerie : SelectedSerie? = null
        var Session : String = ""
        var Settings : Settings = Settings(false ,false,false)
        val allSerie: String = "api/v1/series/list?extended=1&category=0"
        var Neu: String = "api/v1/series/list?extended=1&category=2"
        var Top: String = "api/v1/series/list?extended=1&category=3"
        var Beliebt: String = "api/v1/series/list?extended=1&category=1"
        var SerieGet: String = "api/v1/series/get?series="
        var New_Episodes: String = "api/v1/episodes/updates"
        val AllGenres : MutableList<Genres> = mutableListOf()
        val HosterNames : MutableList<HosterName> = mutableListOf()
        val Languages : MutableList<Language> = mutableListOf()
        var _hosternames = arrayOf<String>("Vivo", "VOE", "Vidoza", "Streamtape")
        var linkname = arrayOf<String>(
                "Abenteuer",
                "Action",
                "Animation",
                "Anime",
                "Comedy",
                "Dokumentation",
                "Dokusoap",
                "Drama",
                "Dramedy",
                "Familie",
                "Fantasy",
                "History",
                "Horror",
                "Jugend",
                "Kinderserie",
                "Krankenhaus",
                "Krimi",
                "Mystery",
                "Romantik",
                "Science-Fiction",
                "Sitcom",
                "Telenovela",
                "Thriller",
                "Western",
                "Zeichentrick",
                "K-Drama",
                "Reality-Tv",
                "Netflix-Originals",
                "Amazon-Originals"
        )

        var laoded = false
        var _AllSeries : Series = Series(mutableListOf())
        var _NeuSeries : Series = Series(mutableListOf())
        var _BeliebtSeries : Series = Series(mutableListOf())
        var _TopSeries : Series = Series(mutableListOf())
        var _WatchSeries : Series = Series(mutableListOf())
        var _Sublist :Series = Series(mutableListOf())
        private var gen = false
        fun Setup(context: Context) = runBlocking {

            val sharedPref = context.getSharedPreferences(  "data",Context.MODE_PRIVATE)
            Session = sharedPref.getString( "SessionID", "").orEmpty()

            _WatchSeries.series.clear()
            _Sublist.series.clear()
            val mainurl = main + getKey(allSerie)
            val topurl = main + getKey(Top)
            val neuurl = main + getKey(Neu)
            val beliebturl = main + getKey(Beliebt)

            laodSettings(context)

            val allseriesdata =  getJson(mainurl)
            var watchseriesdata = ""
            var sublist = ""
            val topseriesdata = getJson(topurl)
            val neuseriesdata = getJson(neuurl)
            val beliebtseriesdata = getJson(beliebturl)
            if(Settings.synwatchlist)
                  watchseriesdata = getWatch( main+ getKey("api/v1/account/watchlist/list"))
            if(Settings.showsub)
                  sublist = getWatch(main + getKey("api/v1/account/subscription/list"))
            _AllSeries = Json.decodeFromString<Series>(allseriesdata);
            GlobalScope.launch{
            CreateGenreListe2()}.start()
            Log.d("watchdata" , "fata : " +  watchseriesdata)
            _NeuSeries = Json.decodeFromString<Series>(neuseriesdata);
            _BeliebtSeries = Json.decodeFromString<Series>(beliebtseriesdata);
            _TopSeries = Json.decodeFromString<Series>(topseriesdata);
            getWatchedSeries(context)
            if(Settings.synwatchlist){
                val json : Series =  Json.decodeFromString<Series>(watchseriesdata)
                for(i in json.series){
                    if(_WatchSeries.series.find { it.id == i.id } == null)
                        _WatchSeries.series.add(_AllSeries.series.find { it.id == i.id }!!)
                }
            }





            if(Settings.showsub){
                _Sublist = Series(mutableListOf())
                val json : Series =  Json.decodeFromString<Series>(sublist)
                for(i in json.series){

                        _Sublist.series.add(_AllSeries.series.find { it.id == i.id }!!)
                }
            }
            while (gen == false){

            }


            Verwaltung.laoded =  true



            HosterNames.add(HosterName(1, "Vivo"))
            HosterNames.add(HosterName(2, "VOE"))
            HosterNames.add(HosterName(3, "Vidoza"))
            HosterNames.add(HosterName(4, "Streamtape"))

            //add LAngues
            Languages.add(Language(1, "German"))
            Languages.add(Language(2, "English"))
            Languages.add(Language(3, "German Sub"))

        }

        suspend fun GetSerie(id: String): SelectedSerie {
            return Json{ignoreUnknownKeys = true}.decodeFromString<SelectedSerie>(
                    getJson(
                            getKey(
                                    main + SerieGet + id
                            )
                    )
            );
        }

        fun saveSettings(context: Context){
            val string =   Json.encodeToString(Settings)
            createfile("setting.json",string,context)
        }

        fun laodSettings(context: Context){
            val string  = readFromFile(context,"setting.json")
            if(!string.isNullOrBlank())
              Settings =  Json { ignoreUnknownKeys = true }.decodeFromString(string)

        }

       suspend  fun getJson(url: String) : String = suspendCoroutine {

           cont-> geturl(url)   { cont.resume(it) }
             }
        fun CreateGenreListe2()
        {
            for(i in 0..28)
            {
                val gen =  Genres(
                        linkname[i],
                        _AllSeries.series.filter { it.genre.toInt() == i + 1 });
                AllGenres.add(gen);
            }

            gen =true
        }
         fun geturl(url: String, callback: (String) -> Unit){

            Thread {
                Log.d("allSerie", "geturl")
                val (_, _, result) = Fuel.get(url)
                        .responseString()
                Log.d("allSerie", "geturl")
                when (result) {
                    is Result.Failure -> {
                        Log.d("allSerie", result.getException().toString())
                        callback("")

                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.d("allSerie", data)
                        //
                        callback(data)

                    }
                }
            }.start()
    }

        suspend  fun getWatch(url: String) : String = suspendCoroutine {

                cont-> geturlWatch(url)   { cont.resume(it) }
        }
        fun geturlWatch(url: String, callback: (String) -> Unit){

            Thread {
                Log.d("allSerie", "geturl")
                val (_, _, result) = Fuel.post(url ).header( "Cookie" , "SSTOSESSION=${Verwaltung.Session}"   )
                    .responseString()
                Log.d("allSerie", "geturl")
                when (result) {
                    is Result.Failure -> {
                        Log.d("allSerie", result.getException().toString())
                        callback("")

                    }
                    is Result.Success -> {
                        val data = result.get()
                        Log.d("allSerie", data)
                        //
                        callback(data)

                    }
                }
            }.start()
        }


        private fun readFromFile(context: Context , file: String): String? {

            var ret = ""
            try {
                val inputStream: InputStream? = context.openFileInput(file)
                if (inputStream != null) {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var receiveString: String? = ""
                    val stringBuilder = StringBuilder()
                    while (bufferedReader.readLine().also { receiveString = it } != null) {
                        stringBuilder.append("\n").append(receiveString)
                    }
                    inputStream.close()
                    ret = stringBuilder.toString()
                }
            } catch (e: FileNotFoundException) {
                Log.e("login activity", "File not found: " + e.toString())
            } catch (e: IOException) {
                Log.e("login activity", "Can not read file: $e")
            }
            return ret
        }


        fun addwatch(s: SelectedSerie , context: Context){

            if(_WatchSeries.series.find { it.id == s.series.id } == null)
                 _WatchSeries.series.add(s.series)
            else {
                _WatchSeries.series.removeAll { it.id == s.series.id }
                _WatchSeries.series.add(s.series)
            }
            val string =   Json.encodeToString(_WatchSeries)
            createfile("watch.json",string,context)
            Thread {
                Fuel.post(getKey("https://s.to/api/v1/account/watchlist/add"), listOf("id" to s.series.id))
                    .header("Cookie", "SSTOSESSION=${Verwaltung.Session}")
                    .responseString { _, _, result ->
                        Log.d("allSerie", "geturl")
                        when (result) {
                            is Result.Failure -> {
                                Log.d("allSerie", result.getException().toString())


                            }
                            is Result.Success -> {
                                val data = result.get()
                                Log.d("allSerie", data)
                                //


                            }

                        }
                    }
            }.start()

        }

        fun getWatchedSeries(context: Context)  {


           val string  = readFromFile(context,"watch.json")
              if(!string.isNullOrBlank()){
               val json: Series = Json { ignoreUnknownKeys = true }.decodeFromString(string)
                  for(s in json.series)
                      if(_WatchSeries.series.find { it.id == s.id } == null)
                          _WatchSeries.series.add(s)

            }else
                _WatchSeries = Series(mutableListOf())
        }

        fun createfile(file: String, data: String, context: Context){

            try {
                val outputStreamWriter = OutputStreamWriter(context.openFileOutput(  file, Context.MODE_PRIVATE))
                outputStreamWriter.write(data)
                outputStreamWriter.close()
            } catch (e: IOException) {
                Log.e("Exception", "File write failed: " + e.toString())
            }
        }



        fun  getKey(s: String) : String =if (s.contains("?")) s+ "&key=${APIKEY.key}" else  s + "?key=${APIKEY.key}"







    }
}