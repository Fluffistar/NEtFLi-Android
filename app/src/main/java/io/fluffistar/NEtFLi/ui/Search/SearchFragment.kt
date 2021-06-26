package io.fluffistar.NEtFLi.ui.Search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import io.fluffistar.NEtFLi.Backend.SerienAdapter
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.Backendv2.Start
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.SerieVIEW
import io.fluffistar.NEtFLi.calculateNoOfColumns
import io.fluffistar.NEtFLi.pmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchFragment : Fragment() {

    private lateinit var searchtxt: AutoCompleteTextView
    private lateinit var searchbtn: ImageView
    private lateinit var epilist : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search, container, false)

        searchtxt = root.findViewById(R.id.searchbox)
        searchbtn = root.findViewById(R.id.searchbtn)
        epilist = root.findViewById(R.id.searchepilist)
        val mNoOfColumns: Int = calculateNoOfColumns(requireActivity().applicationContext,120f)
        epilist.setLayoutManager(GridLayoutManager(requireContext(), mNoOfColumns))
        val searchlist :MutableList<String> = mutableListOf()

        for(i in Start.Allseries)
            searchlist.add(i.Title)

        searchtxt.setAdapter(ArrayAdapter<String>(root.context, R.layout.spinner_item, searchlist))


        searchbtn.setOnClickListener {

GlobalScope.launch(Dispatchers.IO) {

            Log.d("clicl", "clicked")
            var list = Start.Allseries.filter { it.Title.contains(
                searchtxt.text.toString(),
                ignoreCase = true
            ) }.take(100)
            Log.d("COUNT", list.size.toString())

            list.pmap {
                it.load()

            }
    withContext(Dispatchers.Main) {
        var ad = SerienAdapter(requireContext(), list)
        epilist.adapter = ad;
    }}

        }

        return root
    }
}