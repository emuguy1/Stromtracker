package com.example.stromtracker.ui.raeume

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Raum
import com.example.stromtracker.ui.raeume.raeumeBearbeiten_Loeschen.RaeumeBearbeitenLoeschenFragment

class ListAdapterraeume(private val datain: ArrayList<Raum>) :
    RecyclerView.Adapter<ListAdapterraeume.ViewHolder>() {
    private val data = datain
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_raeume, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = data[position].toString()
        holder.textView.text = item
        holder.textView
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.text_view_raeume)

        init {
            textView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            if (view != null) {
                val frag = RaeumeBearbeitenLoeschenFragment(data[layoutPosition], datain)
                // Fragment Manager aus Main Activity holen
                val fragMan = view.findFragment<RaeumeFragment>().parentFragmentManager
                // Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen!
                // mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
        }
    }
}
