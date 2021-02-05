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
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.Serie
import io.fluffistar.NEtFLi.SerieVIEW
import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false) as (ViewGroup)

        val maingrid :LinearLayout = root.findViewById(R.id.maingrid)




    Thread{
        while(Verwaltung.laoded == false ){}
        this.activity?.runOnUiThread(){
            //does actions on Ui-Thread u neeed it because Ui-elements can only be edited in Main/Ui-Thread
           for( i in 1..10)
            showGenre(Verwaltung._AllSeries.series, root.context, maingrid)
        }

    }.start()



      /*  homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    fun  showGenre(list: List<Serie>, context: Context, layout: LinearLayout)  {

        Log.d("allSerie", Verwaltung._AllSeries.series.size.toString())

        val linearParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        var panel : LinearLayout = LinearLayout(context)
        panel.isNestedScrollingEnabled = true

        panel.orientation = HORIZONTAL
        panel.layoutParams = linearParams
        panel.setBackgroundResource(R.drawable.panelbackground)

        panel.setPadding(0, 0, 0, 20)
        var view = HorizontalScrollView(context)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = layoutParams

        view.addView(panel)
        for( i in 1..8 ){

            var s = SerieVIEW(context)
            s.layoutParams = layoutParams
            s.text = list[i].name
            s.id =  list[i].id
            s.setImage(Verwaltung.main + list[i].cover)
            Log.d("allSerie", s.text.toString())
            s.setPadding(0, 0, 10, 0)
            s.setOnClickListener {
               // Toast.makeText(context, "${s.id}", Toast.LENGTH_SHORT).show()

                val intent = Intent(
                    this.activity,
                    SeriesPage::class.java
                )
                intent.putExtra("ID", s.id)
                startActivity(intent)

            }
            panel.addView(s)


        }

        layout.addView(view)

    }
}