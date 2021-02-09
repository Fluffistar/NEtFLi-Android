package io.fluffistar.NEtFLi.ui.Search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.fluffistar.NEtFLi.Backend.CustomAdapter

import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.EpisodeView
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.Serie
import io.fluffistar.NEtFLi.SerieVIEW
import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage

class SearchFragment : Fragment() {

    private lateinit var searchtxt: AutoCompleteTextView
    private lateinit var searchbtn: ImageView
    private lateinit var epilist : GridView


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search, container, false)

        searchtxt = root.findViewById(R.id.searchbox)
        searchbtn = root.findViewById(R.id.searchbtn)
        epilist = root.findViewById(R.id.searchepilist)

        val searchlist :MutableList<String> = mutableListOf()

        for(i in Verwaltung._AllSeries.series)
            searchlist.add(i.name)

        searchtxt.setAdapter(ArrayAdapter<String>(root.context,R.layout.spinner_item,searchlist))


        searchbtn.setOnClickListener {



            Log.d("clicl", "clicked")
            var list = Verwaltung._AllSeries.series.filter { it.name.contains(searchtxt.text.toString(), ignoreCase = true) }.take(100)
            Log.d("COUNT",list.size.toString())
            var android : MutableList<SerieVIEW> = mutableListOf()
          /*  for (i in list){

                var s = SerieVIEW(root.context)

                s.text = if(i.name.length <= 16) i.name else i.name.substring(0,13) + "..."
                s.id =  i.id
                s.setImage(Verwaltung.main + i.cover)

                s.setPadding(0, 0, 10, 0)
                s.setOnClickListener {
                    // Toast.makeText(context, "${s.id}", Toast.LENGTH_SHORT).show()

                    val intent = Intent(
                            root.context,
                            SeriesPage::class.java
                    )
                    intent.putExtra("ID", s.id)
                    root.context.startActivity(intent)

                }

                android.add(s)
            }*/
            var ad = CustomAdapter(root.context,R.layout.sample_serie_v_i_e_w,list)
            epilist.adapter = ad;


        }

        return root
    }
}