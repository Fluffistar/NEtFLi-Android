package io.fluffistar.NEtFLi.ui.SeriesPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.fluffistar.NEtFLi.Backend.TMDB
import io.fluffistar.NEtFLi.Backend.TVResult
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.EpisodeView
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.*
import io.fluffistar.NEtFLi.Videoplayer
import kotlinx.coroutines.*


class SeriesPage : AppCompatActivity() {
         var  id : Long? = -1
        lateinit var img : ImageView
          lateinit var backbtn : ImageView
        lateinit var text  : TextView
        lateinit var  title : TextView
        lateinit var serie : Serie
        lateinit var comboseason : Spinner
        lateinit var episodelist : LinearLayout
          var tv : TvShow2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_page)
        setSupportActionBar(findViewById(R.id.toolbar))
        img = findViewById(R.id.img_SeriePage)
        text = findViewById(R.id.text_SeriePage)
        title = findViewById(R.id.title_SeriePage)
        comboseason = findViewById(R.id.seasoncombo)
        episodelist = findViewById(R.id.episodelist)
        id = intent.extras?.getLong("ID");
        backbtn = findViewById(R.id.backbtn_SeriePage)
       // Toast.makeText(this,"${id}",Toast.LENGTH_SHORT)


                SetupSerie()


        backbtn.setOnClickListener { this.finish() }


    }

    fun createlist()    {

       val job: Job = GlobalScope.launch(context = Dispatchers.Default) {
            serie!!.SeasonsList[comboseason.selectedItemId.toInt()].load()
        }
        job.start()


            while(!job.isCompleted){}
            var actual: Seaso? = null
            episodelist.removeAllViewsInLayout()
            val index = serie!!.SeasonsList[comboseason.selectedItemId.toInt()].Season
            if (tv != null)
                actual = tv!!.seasons.find { it.seasonNumber.toInt() == index }
            var i = 0
            for (episode in serie!!.SeasonsList[comboseason.selectedItemId.toInt()].Episodes) {
                runOnUiThread {
                val epi = EpisodeView(this)
                epi.title =
                    "${i + 1}. " + if (episode.german != "") episode.german else episode.english
                    if(actual!=null && !actual!!.episodes.isNullOrEmpty())
                        epi.text = if(actual!!.episodes[i].overview.length < 140) actual!!.episodes[i].overview else actual!!.episodes[i].overview.substring(0,140) + "..."
                    else
                        epi.text = "No Data Found"
                epi.id = episode.Episode.toLong()
                epi.season = episode.Season.toLong()
                try {
                    if (actual != null)
                        epi.setImage(TMDB.ImgPath + actual!!.episodes[i].stillPath)
                } catch (ex: Exception) {
                    Log.d("ERROR", ex.message.toString())
                }

                epi.setOnClickListener {
                    // Toast.makeText(context, "${s.id}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(
                        this.applicationContext,
                        Videoplayer::class.java
                    )

                    Verwaltung.SelectedSerie = serie
                    Verwaltung.SelectedSerie!!.lastepisode = epi.id!!.toInt() - 1

                    Verwaltung.SelectedSerie!!.lastseason = comboseason.selectedItemPosition

                    startActivity(intent)

                }

                    episodelist.addView(epi)
                }

                i++
            }

    }



     fun SetupSerie(){
        GlobalScope.launch(Dispatchers.Unconfined) {


            serie = Verwaltung.SelectedSerie!!

    try {

            if(serie.TVResult == null)
             tv = TMDB.getTV(serie!!);
            else
                tv = TMDB.getTV2(serie!!);

            TMDB.GetTvEpisodes(tv!!)
    }catch (ex : java.lang.Exception){ }



            withContext(Dispatchers.Main) {
                // blocking I/O operations

            Picasso.get().load(  serie!!.cover).into(img);
            title.text = serie!!.name
                if(serie!!.TVResult != null)
                    text.text = if (serie!!.TVResult?.overview?.length!! < 170) serie!!.TVResult?.overview!! else serie!!.TVResult?.overview!!.substring(0, 167) + "..."

            comboseason.setSelection(0)

            createlist()
            comboseason.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                    createlist()
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
                load()
            }


        }

    }

    fun load(){
        comboseason.adapter = ArrayAdapter<Serie.SeasonSTO>(
                this ,
                R.layout.spinner_item,
                serie!!.SeasonsList
        )
    }
}