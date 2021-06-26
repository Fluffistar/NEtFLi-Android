package io.fluffistar.NEtFLi.ui.SeriesPage

import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import io.fluffistar.NEtFLi.Backend.EpisodeAdapter
import io.fluffistar.NEtFLi.Backend.TMDB
import io.fluffistar.NEtFLi.Backendv2.Season
import io.fluffistar.NEtFLi.Backendv2.Serie
import io.fluffistar.NEtFLi.Backendv2.Start
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.TvShow2

import kotlinx.coroutines.*


class SeriesPage : AppCompatActivity() {
         var  id : Long? = -1
        lateinit var img : ImageView
          lateinit var backbtn : ImageView
        lateinit var text  : TextView
        lateinit var  title : TextView
        lateinit var serie : Serie
        lateinit var comboseason : Spinner
        lateinit var episodelist : RecyclerView
          var tv : TvShow2? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = Firebase.analytics
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
        episodelist.setLayoutManager( LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        episodelist.setHasFixedSize(true);
        episodelist.setItemViewCacheSize(20);
        episodelist.setDrawingCacheEnabled(true);
                SetupSerie()


        backbtn.setOnClickListener { this.finish() }


    }

    fun createlist()   = runBlocking(context = Dispatchers.IO)  {

        serie!!.Seasons[comboseason.selectedItemId.toInt()].load()



    }



     fun SetupSerie(){



            serie = Start.SelectedSerie!!




       createlist()
         comboseason.setSelection(0)
                // blocking I/O operations

            Picasso.get().load(  serie.poster).into(img);
            title.text = serie.Title

            text.text =  serie.despriction



            comboseason.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                    createlist()
                    episodelist.adapter = EpisodeAdapter(
                        this@SeriesPage,
                        serie!!.Seasons[comboseason.selectedItemId.toInt()].Episodes, comboseason.selectedItemPosition)
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }
                load()





    }

    fun load(){
        comboseason.adapter = ArrayAdapter<Season>(
                this ,
               R.layout.spinner_item,
                serie!!.Seasons
        )
    }
}