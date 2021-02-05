package io.fluffistar.NEtFLi.Backend

import android.R.string
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.await
import com.github.kittinunf.fuel.core.awaitResult
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.fluffistar.NEtFLi.Serializer.SelectedSerie
import io.fluffistar.NEtFLi.Serializer.Serie
import io.fluffistar.NEtFLi.Serializer.Series
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class Verwaltung {
    companion object{
        val main ="https://s.to/"
        val allSerie: String = "api/v1/series/list?extended=1&category=0"
        var Neu: String = "api/v1/series/list?extended=1&category=2"
        var Top: String = "api/v1/series/list?extended=1&category=3"
        var Beliebt: String = "api/v1/series/list?extended=1&category=1"
        var SerieGet: String = "api/v1/series/get?series="
        var New_Episodes: String = "api/v1/episodes/updates"
        var laoded = false
        var _AllSeries : Series = Series(mutableListOf())
        var _NeuSeries : Series = Series(mutableListOf())
        var _BeliebtSeries : Series = Series(mutableListOf())
        var _TopSeries : Series = Series(mutableListOf())
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
            _NeuSeries = Json.decodeFromString<Series>(neuseriesdata);
            _BeliebtSeries = Json.decodeFromString<Series>(beliebtseriesdata);
            _TopSeries = Json.decodeFromString<Series>(topseriesdata);






            Verwaltung.laoded = true

        }

        suspend fun GetSerie(id: String): SelectedSerie {
            return Json{ignoreUnknownKeys = true}.decodeFromString<SelectedSerie>(getJson(getKey(main + SerieGet + id)));
        }

       suspend  fun getJson(url : String) : String = suspendCoroutine {

                  cont-> geturl(url)   { cont.resume(it) }
             }

         fun geturl (url : String , callback  : (String) -> Unit){

            Thread {
                Log.d("allSerie", "geturl")
               val (_,_,result) = Fuel.get(url)
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



        fun  getKey(s : String) : String {
            if (s.contains("?")) {

                  return s+ "&key=${APIKEY.key}"
            }
                else
                    return s + "?key=${APIKEY.key}"
        }






    }
}