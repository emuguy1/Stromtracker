package com.example.stromtracker.ui.geraete

import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.database.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import com.example.stromtracker.R
import com.example.stromtracker.ui.geraete.geraet_edit.GeraeteEditProduzentFragment
import com.example.stromtracker.ui.geraete.geraet_edit.GeraeteEditVerbraucherFragment
import java.util.*
import kotlin.math.withSign

class GeraeteListAdapter(
    private val geraeteList: List<Geraete>,
    private val katList: ArrayList<Kategorie>,
    private val raumList: ArrayList<Raum>
) : RecyclerView.Adapter<GeraeteListAdapter.GeraeteViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GeraeteListAdapter.GeraeteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mItemView = layoutInflater.inflate(R.layout.fragment_geraete_textview, parent, false)
        return GeraeteViewHolder((mItemView))
    }

    override fun getItemCount(): Int {
        return geraeteList.size
    }



        override fun onBindViewHolder(holder: GeraeteListAdapter.GeraeteViewHolder, position: Int) {
            holder.mTextView.text = geraeteList[position].getName()
            //.withSign(1) lässt den "Verbrauch" bzw. die Produktion von Produzenten positiv anzeigen, da diese als negativer Verbrauch in der DB gespeichert ist
            holder.mVerbrauchView.text =
                geraeteList[position].getJahresverbrauch().withSign(1).toString()
            holder.mRaumView.text = raumList[geraeteList[position].getRaumID() - 1].getName()

        }

        inner class GeraeteViewHolder(mItemView: View) : RecyclerView.ViewHolder(mItemView),
            View.OnClickListener {
            val mTextView: TextView
            val mCardView: CardView
            val mVerbrauchView: TextView
            val mRaumView: TextView

            init {
                mTextView = mItemView.findViewById(R.id.geraete_recycler_text)
                mVerbrauchView = mItemView.findViewById(R.id.geraete_recycler_verbrauch)
                mCardView = mItemView.findViewById(R.id.geraete_recycler_card)
                mRaumView = mItemView.findViewById(R.id.geraete_recycler_raum)
                mCardView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                if (v != null) {
                    val frag =
                        GeraeteEditVerbraucherFragment(
                            geraeteList[layoutPosition],
                            katList,
                            raumList
                        )

                        val fragMan = v.findFragment<GeraeteFragment>().parentFragmentManager
                        //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                        fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag)
                            .addToBackStack(null).commit()
                    }
                }
            }
        }
