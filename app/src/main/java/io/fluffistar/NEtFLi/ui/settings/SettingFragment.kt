package io.fluffistar.NEtFLi.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.R

class SettingFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.settingfragment, container, false) as (ViewGroup)
        val autoplay = root.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.autoplay_settings)
        val watchlist = root.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.watchlist_settings)
        val sublist = root.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.sublist_settings)


        autoplay.isChecked = Verwaltung.Settings.autplay
        watchlist.isChecked = Verwaltung.Settings.synwatchlist
        sublist.isChecked = Verwaltung.Settings.showsub

        autoplay.setOnClickListener {
            Log.d("Autoplay" , autoplay.isChecked.toString())
            Verwaltung.Settings.autplay = autoplay.isChecked
            Verwaltung.saveSettings(root.context)
        }
        watchlist.setOnClickListener {
            Log.d("Watchlist" , watchlist.isChecked.toString())
            Verwaltung.Settings.synwatchlist = watchlist.isChecked
            Verwaltung.saveSettings(root.context)
        }
        sublist.setOnClickListener {
            Log.d("Sublist", sublist.isChecked.toString())
            Verwaltung.Settings.showsub = sublist.isChecked
            Verwaltung.saveSettings(root.context)
        }
        return root
    }

}