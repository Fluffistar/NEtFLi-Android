package io.fluffistar.NEtFLi.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.HORIZONTAL
import androidx.fragment.app.*
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.ListSerie
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.Serie
import io.fluffistar.NEtFLi.SerieVIEW
import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private  lateinit var  maingrid : LinearLayout;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false) as (ViewGroup)

        maingrid   = root.findViewById(R.id.maingrid)




    Thread{
        while(Verwaltung.laoded == false ){}
        this.activity?.runOnUiThread(){
            //does actions on Ui-Thread u neeed it because Ui-elements can only be edited in Main/Ui-Thread

            showGenre(Verwaltung._TopSeries.series, root.context, "TOP:")
            showGenre(Verwaltung._BeliebtSeries.series, root.context, "BELIEBT:")
            showGenre(Verwaltung._NeuSeries.series, root.context, "NEU:")
            for(i in (0..28).shuffled().take(5))
                showGenre(Verwaltung.AllGenres[i].series.shuffled().take(25),root.context,Verwaltung.AllGenres[i].name)
        }

    }.start()



      /*  homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    fun  showGenre(list: List<Serie>,  context: Context,title: String )  {

        var view = ListSerie(context,list,title)

        maingrid.addView(view)

    }
}