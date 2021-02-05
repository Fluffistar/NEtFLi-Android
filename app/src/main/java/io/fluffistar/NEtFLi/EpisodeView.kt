package io.fluffistar.NEtFLi

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * TODO: document your custom view class.
 */
class EpisodeView : LinearLayout {

    private  var  _img : ImageView
    private  var _text : TextView
    private  var _title : TextView
    private var _id : Long = -1


    fun setImage(value : String){

        Picasso.get().load(value).into(_img);

    }
    var title : String?
        get() = _title.text.toString()
        set(value) {
            _title.text = value
        }


    var id : Long?
        get() = _id
        set(value) {
            _id = value!!

        }

    var text : String?
        get() = _text.text.toString()
        set(value) {
            _text.text = value
        }

    constructor(context: Context ,  attrs : AttributeSet):super(context , attrs){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



        inflater.inflate(R.layout.sample_episode_view,  this )
        _text = findViewById(R.id.text_Episode)
        _title = findViewById(R.id.title_Episode)
        _img = findViewById(R.id.img_episode)
    }
    constructor(context: Context  ):super(context  ){

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater



        inflater.inflate(R.layout.sample_episode_view,  this )
        _text = findViewById(R.id.text_Episode)
        _title = findViewById(R.id.title_Episode)
        _img = findViewById(R.id.img_episode)
    }

}