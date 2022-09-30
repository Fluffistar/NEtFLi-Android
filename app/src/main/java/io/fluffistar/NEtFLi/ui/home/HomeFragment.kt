package io.fluffistar.NEtFLi.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.LinearLayout.HORIZONTAL
import androidx.fragment.app.*
import io.fluffistar.NEtFLi.Backendv2.Serie

import io.fluffistar.NEtFLi.Backendv2.Start
import io.fluffistar.NEtFLi.ListSerie
import io.fluffistar.NEtFLi.R

import io.fluffistar.NEtFLi.SerieVIEW
import io.fluffistar.NEtFLi.pmap
import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {



    private  lateinit var  maingrid : LinearLayout;
    private  lateinit var  root : View
    private  val  views : MutableList<ListSerie> = mutableListOf()
    override fun onResume() {
        super.onResume()

                //does actions on Ui-Thread u neeed it because Ui-elements can only be edited in Main/Ui-Thread

        for(vi in views)
            vi.Update()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

          root = inflater.inflate(R.layout.fragment_home, container, false)

            maingrid   = root.findViewById(R.id.maingrid)




                    //does actions on Ui-Thread u neeed it because Ui-elements can only be edited in Main/Ui-Thread





        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showGenre(Start.Watchlist , root.context, "WATCHLIST:")

        showGenre(Start.Beliebt, root.context, "BELIEBT:")
        showGenre(Start.Neu, root.context, "NEU:")
        (Start.Genres.filter { it.Series.size > 0 }).shuffled().take(5).pmap {
                   showGenre(
                       it.Series.shuffled().take(25), root.context,it.Name)
               }


        progress_loader.visibility = View.GONE
    }


    private fun  showGenre(list: List<Serie>,  context: Context, title: String )  {



        val view = ListSerie(context,list,title)
        views.add(view)
        maingrid.addView(view)

    }
}