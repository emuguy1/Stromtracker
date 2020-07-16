package com.example.stromtracker.ui.kategorien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.database.Kategorie
import com.example.stromtracker.ui.kategorien.edit_kategorie.KategorienEditFragment

class KategorienListAdapter(private val myKategorien: List<Kategorie>, private val iconArray:Array<Int>) : RecyclerView.Adapter<KategorienListAdapter.KategorienViewHolder>() {

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
        val myIconInt = iconArray[myKategorien[position].getIcon()]
        holder.mImageView.setImageResource(myIconInt)
    }
    override fun getItemCount() = myKategorien.size

    inner class KategorienViewHolder(val mItemView:View) : RecyclerView.ViewHolder(mItemView), View.OnClickListener {

        //Über die mItemView lassen sich nun die Inhalte auslesen, diese werden in der init-Funktion initialisiert und Click Listener registriert
        val mTextView:TextView
        val mCardView:CardView
        val mImageView:ImageView
        init {
            mCardView = mItemView.findViewById(R.id.kategorien_recycler_card)
            mCardView.setOnClickListener(this)
            mTextView = mItemView.findViewById<TextView>(R.id.kategorien_recycler_text)
            mImageView = mItemView.findViewById<ImageView>(R.id.kategorien_image)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                //layoutposition gibt an, welche Position geklickt wurde
                //neues Fragment erstellen, Beim Klick auf eine Kategorie soll ja auf die Seite mit Kategorie Bearbeiten weitergeleitet werden
                val frag = KategorienEditFragment(myKategorien[layoutPosition], iconArray)
                //Fragment Manager aus Main Activity holen
                val fragMan = mItemView.findFragment<KategorienFragment>().parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }

}
