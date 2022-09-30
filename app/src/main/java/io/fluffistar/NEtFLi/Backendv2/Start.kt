package io.fluffistar.NEtFLi.Backendv2

import android.R.string
import android.content.Context
import android.util.Log
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.HtmlParser
import io.fluffistar.NEtFLi.Serializer.Series
import io.fluffistar.NEtFLi.View
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class Start {
    companion object{

       var Domain = ""
        var Session = ""
        var Hoster: List<String> = mutableListOf("Vivo", "MixDrop", "StreamTape", "VOE", "Vidoza")
        var SelectedSerie :  Serie? = null
        val  Allseries : MutableList<Serie>  = mutableListOf()
        val  Beliebt : MutableList<Serie>  = mutableListOf()
        val  Neu : MutableList<Serie>  = mutableListOf()
        val  Watchlist : MutableList<Serie>  = mutableListOf()
        val  Genres : MutableList<Genre>  = mutableListOf()
        fun setup(context: Context) = runBlocking{
            val sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
            Session = sharedPref.getString("SessionID", "").orEmpty()
            Domain = "http://190.115.18.20/" //getDomain()
            getWatchedSeries(context)
            all()
            neu()
            beliebt()
        }

        fun getWatchedSeries(context: Context)  {


            val string  = Verwaltung.readFromFile(context, "watch.json")
            Log.d("watchjson",string.toString())
            try{
                if(!string.isNullOrBlank()){
                    val json: Series = Json { ignoreUnknownKeys = true }.decodeFromString(string)
                    for(s in json.series) {
                        Watchlist.add(Serie( s.name,s.link))
                    }
                }
            }catch (ex: Exception){
                if(!string.isNullOrBlank()){
                    val json: List<WatchedSerie> = Json { ignoreUnknownKeys = true }.decodeFromString(string)
                    for(s in json ) {
                    //    if(Watchlist.find { it.Title ==s.Title } ==  null)
                        Watchlist.add(Serie(s.Title,s.Link))
                    }
                }
            }

        }

        fun addwatch(s: Serie, context: Context){

            if(Watchlist.find { it.Title == s.Title } == null)
                Watchlist.add(0,s)
            else {
                Watchlist.removeAll { it.Title == s.Title }
                Watchlist.add(0,s)
            }
            val watch : MutableList<WatchedSerie> = mutableListOf()
            for (watchs in Watchlist)
                watch.add(WatchedSerie(watchs.Title,watchs.link))
            val string =   Json.encodeToString(watch)
            Log.d("watchjson",string.toString())
            Verwaltung.createfile("watch.json", string, context)


        }

       suspend fun all(){
          val parser =  HtmlParser();

           val list =   parser.setup(Domain + "/serien");

           for (element in list)
           {
               //	std::cout << "Type: " << item.type << std::endl ;
               val item = element;
               if (item.type == View.div)
                   if (item.classname == "genre")
                   {
                       val gen =   Genre(item.children[0].getChild().content);


                       //items
                       var childs = item.children[1].children;

                       for( elem in childs)
                       {
                           /*	Task.Run(() =>
                               {
                                   DataAccessLibrary.DataAccess.InsertorUpdateAllSerie(elem.children[0].content, elem.children[0].getHref(), item.children[0].getChild().content);
                               });*/
                           val s =   Serie(elem.children[0].content, elem.children[0].href);
                           Allseries.add(s);
                           //switch between genre
                           gen.Series.add(s);
                       }
                       Genres.add(gen);
                   }
           }
        }

        suspend fun  neu(){
            val parser =  HtmlParser();
            var list =  parser.setup(Domain + "/neu");
            for (  elem in list)
            {
                if (elem.classname == "col-md-15 col-sm-3 col-xs-6")
                {
                    Neu.add(  Serie(elem.children[0].getCustomHeader("title=\"").split(  " stream"  )[0], elem.children[0].href));

                }
            }
        }

        suspend fun beliebt(){
            val parser = HtmlParser();
            var list =   parser.setup(Domain + "/beliebte-serien");

            for(  elem in list)
            {
                if(elem.classname == "col-md-15 col-sm-3 col-xs-6")
                {
                    Beliebt.add(  Serie(elem.children[0].getCustomHeader("title=\"").split( " stream"  )[0], elem.children[0].href ));

                }
            }
        }

        suspend fun  getDomain() : String{
            val parser =   HtmlParser();

            val list =   parser.setup("https://serien.domains/");
            Log.d("List: ",list.size.toString())
            for (  elem in list)
            {
                Log.d("Classname: ",    elem.id)
                if (elem.classname == "links")
                {
                    return elem.getChild().getChild().href;
                }
            }
            return "";
        }
    }
}