package io.fluffistar.NEtFLi.Backend

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import io.fluffistar.NEtFLi.Serializer.Genres
import io.fluffistar.NEtFLi.Serializer.SelectedSerie
import io.fluffistar.NEtFLi.Serializer.Series
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HosterName(val id: Int, val name: String)
class Language(val id: Int, val lang: String)

class Verwaltung {
    companion object{
        val main ="https://s.to/"
        val allSerie: String = "api/v1/series/list?extended=1&category=0"
        var Neu: String = "api/v1/series/list?extended=1&category=2"
        var Top: String = "api/v1/series/list?extended=1&category=3"
        var Beliebt: String = "api/v1/series/list?extended=1&category=1"
        var SerieGet: String = "api/v1/series/get?series="
        var New_Episodes: String = "api/v1/episodes/updates"
        val AllGenres : MutableList<Genres> = mutableListOf()
        val HosterNames : MutableList<HosterName> = mutableListOf()
        val Languages : MutableList<Language> = mutableListOf()
        var _hosternames = arrayOf<String>("Vivo","VOE","Vidoza","Streamtape")
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
        private var gen = false
        fun Setup() = runBlocking {




            val mainurl = main + getKey(allSerie)
            val topurl = main + getKey(Top)
            val neuurl = main + getKey(Neu)
            val beliebturl = main + getKey(Beliebt)



            val allseriesdata =  getJson(mainurl)

            val topseriesdata = getJson(topurl)
            val neuseriesdata = getJson(neuurl)
            val beliebtseriesdata = getJson(beliebturl)
            _AllSeries = Json.decodeFromString<Series>(allseriesdata);
            CreateGenreListe2()
            _NeuSeries = Json.decodeFromString<Series>(neuseriesdata);
            _BeliebtSeries = Json.decodeFromString<Series>(beliebtseriesdata);
            _TopSeries = Json.decodeFromString<Series>(topseriesdata);



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



        fun  getKey(s: String) : String =if (s.contains("?")) s+ "&key=${APIKEY.key}" else  s + "?key=${APIKEY.key}"







    }
}