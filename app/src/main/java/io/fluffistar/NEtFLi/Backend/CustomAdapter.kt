package io.fluffistar.NEtFLi.Backend



import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import com.squareup.picasso.Picasso
import io.fluffistar.NEtFLi.R
import io.fluffistar.NEtFLi.Serializer.Serie
import io.fluffistar.NEtFLi.ui.SeriesPage.SeriesPage
import java.util.*


class CustomAdapter(context: Context, resource: Int, objects: List<Serie>) : ArrayAdapter<Serie?>(context, resource, objects) {
    var items_list: List<Serie> = ArrayList()
    var custom_layout_id: Int
    override fun getCount(): Int {
        return items_list.size
    }

    override fun getView(position: Int, @Nullable convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            // getting reference to the main layout and
            // initializing
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(custom_layout_id, null)
        }

        // initializing the imageview and textview and
        // setting data
        val imageView: ImageView = v!!.findViewById(R.id.img_Serie)
        val textView = v.findViewById<TextView>(R.id.text_Serie)

        // get the item using the  position param
        val item: Serie = items_list[position]
        v.id = item.id.toInt()
        Picasso.get().load(Verwaltung.main + item.cover).into(imageView);
        textView.text =(if(item?.name!!.length <= 16) item.name else item.name?.substring(0, 16)+"...")



        v.setOnClickListener{

            val intent = Intent(
                    context,
                    SeriesPage::class.java
            )
            intent.putExtra("ID", item.id)
            context.startActivity(intent)

        }


        return v
    }

    init {
        items_list = objects
        custom_layout_id = resource
    }
}