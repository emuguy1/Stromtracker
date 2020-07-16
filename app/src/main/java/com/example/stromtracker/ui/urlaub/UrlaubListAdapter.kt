package com.example.stromtracker.ui.urlaub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Haushalt
import com.example.stromtracker.database.Urlaub
import com.example.stromtracker.ui.urlaub.urlaub_edit.UrlaubEditFragment
import java.text.SimpleDateFormat
import java.util.*

class UrlaubListAdapter(private val myUrlaub: List<Urlaub>, private val currHaushalt: Haushalt) :
    RecyclerView.Adapter<UrlaubListAdapter.UrlaubViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UrlaubListAdapter.UrlaubViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val mItemView =
            layoutInflater.inflate(R.layout.fragment_urlaub_recycler_text_view, parent, false)
        //mItemView ist nun die jeweilige View auf das ConstraintLayout der Items in der Recyclerview

        //UrlaubViewHolder erstellen und returnen, diesem wird die mItemView übergeben.
        return UrlaubViewHolder(mItemView)
    }

    override fun onBindViewHolder(holder: UrlaubViewHolder, position: Int) {
        holder.mTextView.text = myUrlaub[position].getName()
        var tempStr = "von " + SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.GERMAN
        ).format(myUrlaub[position].getDateVon())
        holder.dateVon.text = tempStr
        tempStr = "bis " + SimpleDateFormat(
            "dd.MM.yyyy",
            Locale.GERMAN
        ).format(myUrlaub[position].getDateBis())
        holder.dateBis.text = tempStr
    }

    override fun getItemCount() = myUrlaub.size

    inner class UrlaubViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView),
        View.OnClickListener {

        //Über die mItemView lassen sich nun die Inhalte auslesen, diese werden in der init-Funktion initialisiert und Click Listener registriert
        val mTextView: TextView
        private val mCardView: CardView = mItemView.findViewById(R.id.urlaub_recycler_card)
        val dateVon: TextView
        val dateBis: TextView

        init {
            mCardView.setOnClickListener(this)
            mTextView = mItemView.findViewById(R.id.urlaub_recycler_text)
            dateVon = mItemView.findViewById(R.id.urlaub_recycler_von)
            dateBis = mItemView.findViewById(R.id.urlaub_recycler_bis)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                //layoutposition gibt an, welche Position geklickt wurde
                //neues Fragment erstellen, Beim Klick auf eine Kategorie soll ja auf die Seite mit Kategorie Bearbeiten weitergeleitet werden
                val frag = UrlaubEditFragment(myUrlaub[layoutPosition], currHaushalt)
                //Fragment Manager aus Main Activity holen
                val fragMan = mItemView.findFragment<UrlaubFragment>().parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                    .addToBackStack(null).commit()
            }
        }
    }

}
