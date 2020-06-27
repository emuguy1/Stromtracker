package com.example.stromtracker.ui.kategorien

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.ui.kategorien.edit_kategorie.KategorienEditFragment
import com.example.stromtracker.ui.kategorien.new_kategorie.KategorienNewFragment
import java.util.*

class KategorienListAdapter(private val myKategorien: List<Kategorie>) : RecyclerView.Adapter<KategorienListAdapter.KategorienViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): KategorienListAdapter.KategorienViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val mItemView = layoutInflater.inflate(R.layout.fragment_kategorien_recycler_text_view, parent, false)
        //mItemView ist nun die jeweilige View auf das ConstraintLayout der Items in der Recyclerview

        //KategorienViewHolder erstellen und returnen, diesem wird die mItemView übergeben.
        return KategorienViewHolder(mItemView)
    }

    override fun onBindViewHolder(holder: KategorienViewHolder, position: Int) {
        holder.mTextView.text= myKategorien[position].getName()
    }
    override fun getItemCount() = myKategorien.size

    inner class KategorienViewHolder(val mItemView:View) : RecyclerView.ViewHolder(mItemView), View.OnClickListener {

        //Über die mItemView lassen sich nun die Inhalte auslesen, diese werden in der init-Funktion initialisiert und Click Listener registriert
        val mTextView:TextView
        val mCardView:CardView
        init {
            mCardView = mItemView.findViewById(R.id.kategorien_recycler_card)
            mCardView.setOnClickListener(this)
            mTextView = mItemView.findViewById<TextView>(R.id.kategorien_recycler_text)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                //layoutposition gibt an, welche Position geklickt wurde
                //neues Fragment erstellen, Beim Klick auf eine Kategorie soll ja auf die Seite mit Kategorie Bearbeiten weitergeleitet werden
                val frag = KategorienEditFragment(myKategorien[layoutPosition])
                //Fragment Manager aus Main Activity holen
                val fragMan = mItemView.findFragment<KategorienFragment>().parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }

}