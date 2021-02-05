package io.fluffistar.NEtFLi.ui.SeriesPage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.fluffistar.NEtFLi.Backend.TMDB
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.EpisodeView
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.Seasons
import io.fluffistar.NEtFLi.Videoplayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SeriesPage : AppCompatActivity() {
         var  id : Long? = -1
        lateinit var img : ImageView
        lateinit var text  : TextView
        lateinit var  title : TextView
        lateinit var comboseason : Spinner
        lateinit var episodelist : LinearLayout
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

       // Toast.makeText(this,"${id}",Toast.LENGTH_SHORT)
        GlobalScope.launch(Dispatchers.Main){
                Setup()
        }

    }

    suspend fun Setup(){
        val  serie  =  Verwaltung.GetSerie(id.toString())


           var   tv = TMDB.getTV(serie)  ;
            TMDB.GetTvEpisodes(tv);

        serie.CreateSeasons();
        Picasso.get().load(Verwaltung.main + serie.series.cover).into(img);
        title.text =  serie.series.name
        text.text = serie.series.description
        comboseason.adapter = ArrayAdapter<Seasons>(
            this,
            R.layout.support_simple_spinner_dropdown_item,
            serie.SeasonsList
        )
        comboseason.setSelection(0)

        val index = serie.SeasonsList[comboseason.selectedItemId.toInt()].id
        val actual = tv.seasons.find { it.seasonNumber.toInt() == index }
        var i = 0
        for (episode in serie.SeasonsList[comboseason.selectedItemId.toInt()].episodes){

            val epi = EpisodeView(this)
            epi.title = episode.german
            epi.text = episode.description
            epi.id = episode.episode.toLong()
            try {
                if (actual != null)
                    epi.setImage(TMDB.ImgPath + actual!!.episodes[i].stillPath)
            }catch (ex: Exception){Log.d("ERROR", ex.message.toString())}

            epi.setOnClickListener {
                // Toast.makeText(context, "${s.id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(
                    this,
                    Videoplayer::class.java
                )
                intent.putExtra("ID", epi.id)
                val bundle = Bundle()
                bundle.putSerializable("Serie", serie)

                intent.putExtras(bundle)

                startActivity(intent)

            }

            episodelist.addView(epi)
             i++
        }

    }

}