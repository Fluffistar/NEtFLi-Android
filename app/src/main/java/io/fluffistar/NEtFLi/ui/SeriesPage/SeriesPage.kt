package io.fluffistar.NEtFLi.ui.SeriesPage

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
import io.fluffistar.NEtFLi.Serializer.Seasons
import io.fluffistar.NEtFLi.Serializer.SelectedSerie
import io.fluffistar.NEtFLi.Serializer.TvShow2
import io.fluffistar.NEtFLi.Videoplayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SeriesPage : AppCompatActivity() {
         var  id : Long? = -1
        lateinit var img : ImageView
        lateinit var text  : TextView
        lateinit var  title : TextView
        lateinit var serie :SelectedSerie
        lateinit var comboseason : Spinner
        lateinit var episodelist : LinearLayout
        lateinit var tv : TvShow2

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

    fun createlist(){
        episodelist.removeAllViewsInLayout()
        val index = serie!!.SeasonsList[comboseason.selectedItemId.toInt()].id
        val actual = tv.seasons.find { it.seasonNumber.toInt() == index }
        var i = 0
        for (episode in serie!!.SeasonsList[comboseason.selectedItemId.toInt()].episodes){

            val epi = EpisodeView(this)
            epi.title = "${i+1}. "+  if(episode.german != "") episode.german else episode.english
            epi.text = if(episode.description.length < 140) episode.description else episode.description.substring(0,140) + "..."
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


    suspend fun Setup(){

            serie =  Verwaltung.GetSerie(id.toString())


           tv = TMDB.getTV(serie!!)  ;
            TMDB.GetTvEpisodes(tv);

            serie!!.CreateSeasons();
        Picasso.get().load(Verwaltung.main + serie!!.series.cover).into(img);
        title.text =  serie!!.series.name
        text.text = if(serie!!.series.description.length < 170) serie!!.series.description else serie!!.series.description.substring(0,167)+"..."
        comboseason.adapter = ArrayAdapter<Seasons>(
                this,
                R.layout.spinner_item,
            serie!!.SeasonsList
        )
        comboseason.setSelection(0)

       createlist()
        comboseason.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                 createlist()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        })
    }

}