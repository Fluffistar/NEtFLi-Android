package io.fluffistar.NEtFLi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.GridView
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.annotation.RestrictTo
import androidx.leanback.widget.HorizontalGridView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.fluffistar.NEtFLi.Backend.CustomAdapter
import io.fluffistar.NEtFLi.Backend.CustomAdapterRecycler
import io.fluffistar.NEtFLi.Backend.SerienAdapter
import io.fluffistar.NEtFLi.Backendv2.Serie

import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage
import kotlinx.coroutines.*

/**
 * TODO: document your custom view class.
 */
class ListSerie : LinearLayout {

     private var _panel : RecyclerView
     private  var _text : TextView

    constructor(context: Context ,  attrs : AttributeSet):super(context , attrs){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



        inflater.inflate(R.layout.sample_list_serie,  this )
        _panel = findViewById(R.id.list_Serie)
        _text = findViewById(R.id.list_text)

    }

    lateinit var adapter : SerienAdapter

    fun Update(){
        adapter.notifyDataSetChanged()
    }

    fun load (it :  Serie) = runBlocking {



    }

    constructor(context: Context, list : List<Serie>, string: String   ):super(context  )  {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater




    val  root =  inflater.inflate(R.layout.sample_list_serie, this)
        _panel = findViewById(R.id.list_Serie)
        _text = findViewById(R.id.list_text)
        root.isEnabled = false
Thread{
            list.pmap {
                it.load()
                (context as Activity).runOnUiThread {
                    Update()
                    root.isEnabled = true
                }

            }}.start()


        (context as Activity).runOnUiThread {
            adapter = SerienAdapter(context, list)


            _panel.setLayoutManager(
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            );

                _panel.adapter = adapter
                _panel.setHasFixedSize(true);
                _panel.setItemViewCacheSize(20);
                _panel.setDrawingCacheEnabled(true);
                _text.text = string

}





        }



}

