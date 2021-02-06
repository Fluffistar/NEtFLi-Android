package io.fluffistar.NEtFLi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.Serializer.Serie
import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage

/**
 * TODO: document your custom view class.
 */
class ListSerie : LinearLayout {

     private var _panel : LinearLayout
     private  var _text : TextView

    constructor(context: Context ,  attrs : AttributeSet):super(context , attrs){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



        inflater.inflate(R.layout.sample_list_serie,  this )
        _panel = findViewById(R.id.list_Serie)
        _text = findViewById(R.id.list_text)

    }
    constructor(context: Context , list : List<Serie> , string: String   ):super(context  ){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



        inflater.inflate(R.layout.sample_list_serie,  this )
        _panel = findViewById(R.id.list_Serie)
        _text = findViewById(R.id.list_text)


        _text.text = string

        for( i in list ){

            var s = SerieVIEW(context)

            s.text = if(i.name.length <= 16) i.name else i.name.substring(0,13) + "..."
            s.id =  i.id
            s.setImage(Verwaltung.main + i.cover)

            s.setPadding(0, 0, 10, 0)
            s.setOnClickListener {
                // Toast.makeText(context, "${s.id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(
                    context,
                    SeriesPage::class.java
                )
                intent.putExtra("ID", s.id)
                context.startActivity(intent)

            }
            _panel.addView(s)


        }

    }

}