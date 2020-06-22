package com.example.stromtracker.ui.haushalt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.haushalt.haushalteBearbeiten_Loeschen.HaushaltBearbeitenLoeschenFragment

class ListAdapterHaushalt() : RecyclerView.Adapter<ListAdapterHaushalt.ViewHolder>() {
    private val data =
        List(10) { ("Haushalt " + (it+1).toString()) }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_haushalt, parent, false)

        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = data[position]
        holder.textView.text = item
        holder.textView

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.textview_haushalt)
        init {
            textView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            if (view != null) {
                val frag = HaushaltBearbeitenLoeschenFragment()
                //Fragment Manager aus Main Activity holen
                val fragMan = view.findFragment<HaushaltFragment>().parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).commit()
            }
        }
    }
}