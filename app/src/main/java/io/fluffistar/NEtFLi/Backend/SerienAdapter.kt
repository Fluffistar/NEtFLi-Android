package io.fluffistar.NEtFLi.Backend

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import io.fluffistar.NEtFLi.Backendv2.Serie
import io.fluffistar.NEtFLi.Backendv2.Start
import io.fluffistar.NEtFLi.R

import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage
import kotlinx.coroutines.*

class SerienAdapter (private val context: Context, val  ArrayList: List<Serie>) :
    RecyclerView.Adapter<SerienAdapter.Viewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        // to inflate the layout for each item of recycler view.
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.serie_cardview, parent, false)
        return Viewholder(view)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        // to set data to textview and imageview of each card layout
        val model: Serie =  ArrayList[position]
        GlobalScope.launch(Dispatchers.IO) {
         //   model.load()
            Log.d("Cover ${model.Title}",model.poster)
            withContext(Dispatchers.Main) {
                if(model.poster != "")
                Picasso.get().load(model.poster).noFade().noPlaceholder().into(holder.img);
                val title = model.Title.replace("&amp;","")
                holder.Title.text =
                    (if (title.length <= 18) title else title.substring(
                        0,
                        14
                    ) + " ...")

            }
        }
        holder.itemView.setOnClickListener{

            val intent = Intent(
                this.context,
                SeriesPage::class.java
            )

          loadtv(model)
            model.loadSeasons()

           Start.SelectedSerie = model

            this.context.startActivity(intent)

        }

    }
    fun loadtv(model :Serie ) = runBlocking(Dispatchers.IO) {
        model.loadTv()
    }

    override fun getItemCount(): Int {

        return  ArrayList.size
    }




    inner class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Title: TextView
        val img : ImageView


        init {
            Title = itemView.findViewById(R.id.serie_cardview_title)
            img = itemView.findViewById(R.id.serie_cardview_img)

        }
    }


}