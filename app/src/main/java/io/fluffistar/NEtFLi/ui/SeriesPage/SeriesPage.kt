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
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.EpisodeView
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.Seaso
import io.fluffistar.NEtFLi.Serializer.Seasons
import io.fluffistar.NEtFLi.Serializer.SelectedSerie
import io.fluffistar.NEtFLi.Serializer.TvShow2
import io.fluffistar.NEtFLi.Videoplayer
import kotlinx.coroutines.*


class SeriesPage : AppCompatActivity() {
         var  id : Long? = -1
        lateinit var img : ImageView
          lateinit var backbtn : ImageView
        lateinit var text  : TextView
        lateinit var  title : TextView
        lateinit var serie :SelectedSerie
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

    fun createlist(){
        var actual : Seaso? = null
        episodelist.removeAllViewsInLayout()
        val index = serie!!.SeasonsList[comboseason.selectedItemId.toInt()].id
        if(tv != null)
         actual = tv!!.seasons.find { it.seasonNumber.toInt() == index }
        var i = 0
        for (episode in serie!!.SeasonsList[comboseason.selectedItemId.toInt()].episodes){

            val epi = EpisodeView(this)
            epi.title = "${i+1}. "+  if(episode.german != "") episode.german else episode.english
            epi.text = if(episode.description.length < 140) episode.description else episode.description.substring(0,140) + "..."
            epi.id = episode.episode.toLong()
            epi.season = episode.season.toLong()
            try {
                if (actual != null)
                    epi.setImage(TMDB.ImgPath + actual!!.episodes[i].stillPath)
            }catch (ex: Exception){Log.d("ERROR", ex.message.toString())}

            epi.setOnClickListener {
                // Toast.makeText(context, "${s.id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(
                        this.applicationContext,
                        Videoplayer::class.java
                )

                Verwaltung.SelectedSerie = serie
                Verwaltung.SelectedSerie!!.SelectedEpisode  = epi.id!!.toInt()-1

                Verwaltung.SelectedSerie!!.SelectedSeason = comboseason.selectedItemPosition

                startActivity(intent)

            }

            episodelist.addView(epi)
            i++
        }
    }


     fun SetupSerie(){
        GlobalScope.launch(Dispatchers.Unconfined) {


            serie =   Verwaltung.GetSerie(id.toString())

    try {


            tv = TMDB.getTV(serie!!);
            TMDB.GetTvEpisodes(tv!!);}catch (ex : java.lang.Exception){ }

            serie!!.CreateSeasons();

            withContext(Dispatchers.Main) {
                // blocking I/O operations

            Picasso.get().load(Verwaltung.main + serie!!.series.cover).into(img);
            title.text = serie!!.series.name
            text.text = if (serie!!.series.description.length < 170) serie!!.series.description else serie!!.series.description.substring(0, 167) + "..."

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
        comboseason.adapter = ArrayAdapter<Seasons>(
                this ,
                R.layout.spinner_item,
                serie!!.SeasonsList
        )
    }
}