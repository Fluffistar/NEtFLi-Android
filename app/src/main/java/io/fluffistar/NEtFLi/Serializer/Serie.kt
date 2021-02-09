package io.fluffistar.NEtFLi.Serializer

import io.fluffistar.NEtFLi.Backend.Verwaltung.Companion.SerieGet
import io.fluffistar.NEtFLi.Backend.Verwaltung.Companion.getJson
import io.fluffistar.NEtFLi.Backend.Verwaltung.Companion.getKey
import io.fluffistar.NEtFLi.Backend.Verwaltung.Companion.main
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Serializable
class Serie(
    val id: Long,
    val name: String,
    val link: String,
    val cover: String = "",
    val background: String = "",
    val description: String = "",
    val trailer: String = "",
     val genre: Long = 0,
    val fsk: Long = 0,
       val productionStart :Int  = 0,
        val productionEnd :Int = 0,
//public Genre mainGenre :Genre ,
//public Genre[] otherGenres { get; set; }
//public Rating rating { get; set; }
//public Person[] directors { get; set; }
//public Person[] actors { get; set; }
//public Country[] countries { get; set; }
//public bool watchlist { get; set; }
//public bool favourite { get; set; }
//public DateTime DateTime { get; set; }
//public int lastepisode { get; set; }
//public int lastseason { get; set; }




): java.io.Serializable
{
}

@Serializable
class Series(var series : MutableList<Serie>){
}

@Serializable
public class SelectedSerie
    (

    var  finished :Boolean = false,
    val series  :Serie,
    val seasons : List<Int>,
    var SeasonsList :MutableList<Seasons>  = mutableListOf(),
    var last :Seasons? = null,
    var hasSpecial  :Boolean = false,
    var SelectedSeason :Int = 0,
    var SelectedEpisode :Int = 0
    ) : java.io.Serializable {
    suspend fun CreateSeasons()
    {
        SeasonsList = mutableListOf()
        if (finished == false)
        {

            for (i in seasons)
            {
                val request : String = getJson(getKey(main + SerieGet + series.id + "&season=" + i));

                val seaso : Seasons = Json{ignoreUnknownKeys = true;coerceInputValues = true}.decodeFromString<Seasons>(request);
                seaso.name = if(i == 0)  "Specials" else "Season " + i;
                seaso.id = i;

                if (i != 0)
                    SeasonsList.add(seaso);
                else
                {
                    last = seaso;
                    hasSpecial = true;
                }
            }
            if (last != null)
                SeasonsList.add(last!!);
            finished = true;
        }
    }



}
@Serializable
public class LastTimeEpisodeWatched
(
    val id :Int,
    val duration:Double,
    val maxduration :Double
)
@Serializable
public class Seasons
(
    ///if 0 Specials
    //  public bool finished = false;
    var name :String = "" ,
    var id :Int  = -1,
    val  episodes : List<Episodes>


): java.io.Serializable {
    override fun toString(): String {
        return name
    }
    }
@Serializable
public class Episodes
    (
    val id :Int,
    val series : Int,
     val season  : Int  ,
    val episode  : Int,
    val german  : String = "",
    val english  : String = "",
    // public string thumbnail { get; set; }
    val description  : String = "",
    val  links :List<Links>
): java.io.Serializable
@Serializable
public class Links
    (
        val id :Int,
        val link  : String ,
        val hoster  : String ,
        val hosterTitle  : String ,
        val language : Int

        ): java.io.Serializable{
    override fun toString(): String {
        return  hosterTitle
    }
        }
