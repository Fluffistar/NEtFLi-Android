package io.fluffistar.NEtFLi.Backend

import android.R.attr.name
import android.R.string
import android.util.Log
import io.fluffistar.NEtFLi.Serializer.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.util.concurrent.Executors
import javax.security.auth.callback.Callback
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class TMDB {
    companion object{
        val ImgPath = "https://image.tmdb.org/t/p/original/"


        suspend  fun getTV(name : SelectedSerie ) : TvShow2 = suspendCoroutine {

                cont-> setup(name)   { cont.resume(it) }
        }


        fun setup(name: SelectedSerie , callback: (TvShow2) -> Unit)  = runBlocking{

            var src: String = if (name.series.name.length <= 34) name.series.name else name.series.name.substring(
                0,
                34
            )
            val pagesrc =  Verwaltung.getJson("https://api.themoviedb.org/3/search/tv?api_key=${APIKEY.tmdb}&query=${src}&language=de")
            var output :TvShow2? = null
            val page : Page  = Json{ignoreUnknownKeys = true;coerceInputValues = true;isLenient = true}.decodeFromString(pagesrc)
            if (page.total_results > 1){
                for ( tv in page.results)
                {
                    var  x : Int;
                    try
                    {
                        // x = Int32.Parse(tv.first_air_date.Split('-')[0]);
                        x = LocalDate.parse(tv.first_air_date).year;
                    }
                    catch( ex : Exception)
                    {
                        x = 0;
                    }

                    if (x == name.series.productionStart  )
                    {
                        val url = "https://api.themoviedb.org/3/tv/${tv.id}?api_key=${APIKEY.tmdb}&language=de"
                        Log.d("URL",url)
                        val ss = Verwaltung.getJson(url)
                        output =  Json{ignoreUnknownKeys = true;coerceInputValues = true;isLenient = true}.decodeFromString<TvShow2>(ss);
                    }

                }
                if(output == null ) {
                    val url = "https://api.themoviedb.org/3/tv/${page.results[0].id}?api_key=${APIKEY.tmdb}&language=de"
                    Log.d("URL",url)

                    val ss =
                        Verwaltung.getJson(url)
                    output = Json{ignoreUnknownKeys = true;coerceInputValues = true;isLenient = true}.decodeFromString<TvShow2>(ss);
                }
            }
            else{
                val url = "https://api.themoviedb.org/3/tv/${page.results[0].id}?api_key=${APIKEY.tmdb}&language=de"
                Log.d("URL",url)
                var sss = Verwaltung.getJson(url)
                output =  Json{ignoreUnknownKeys = true;coerceInputValues = true;isLenient = true}.decodeFromString<TvShow2>(sss);


        }

            callback(output)
        }


        suspend fun work(input:  Seaso , id : Long) {
    val url = "https://api.themoviedb.org/3/tv/${id}/season/${input.seasonNumber}?api_key=${APIKEY.tmdb}&language=de"
            Log.d("URL",url)
            val json = Verwaltung.getJson(url);

            input.episodes = Json{ignoreUnknownKeys = true;coerceInputValues = true;isLenient = true}.decodeFromString<Season2>(json).episodes;
        }

       fun GetTvEpisodes(tv :TvShow2) {
            tv.seasons.parallelStream().forEach {
                GlobalScope.launch {
                    work(it,tv.id)
                }

            }
        }

    }
}