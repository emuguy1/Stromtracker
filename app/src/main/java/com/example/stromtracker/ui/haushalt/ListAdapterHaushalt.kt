package com.example.stromtracker.ui.haushalt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.ui.haushalt.haushalteBearbeiten_Loeschen.HaushaltBearbeitenLoeschenFragment

class ListAdapterHaushalt(datain: List<Haushalt>) :
    RecyclerView.Adapter<ListAdapterHaushalt.ViewHolder>() {
    private val data = datain
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_haushalt, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = data[position].getName()
        holder.textView.text = item
        holder.textView
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var listcard: CardView = itemView.findViewById(R.id.recycler_card_haushalt)
        var textView: TextView = itemView.findViewById(R.id.text_view_haushalt)

        init {
            listcard.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (view != null) {
                val frag = HaushaltBearbeitenLoeschenFragment(data[layoutPosition])
                // Fragment Manager aus Main Activity holen
                val fragMan = view.findFragment<HaushaltFragment>().parentFragmentManager
                // Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
        }
    }
}
