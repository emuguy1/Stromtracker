package com.example.stromtracker.ui.geraete

import android.text.Layout
import android.util.Log
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.database.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.findFragment
import com.example.stromtracker.R
import com.example.stromtracker.ui.geraete.geraet_edit.GeraeteEditProduzentFragment
import com.example.stromtracker.ui.geraete.geraet_edit.GeraeteEditVerbraucherFragment
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.math.roundToLong

class GeraeteListAdapter(private val geraeteList: List<Geraete>, private val katList: ArrayList<Kategorie>, private val raumListHaushalt: ArrayList<Raum>): RecyclerView.Adapter<GeraeteListAdapter.GeraeteViewHolder>() {
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
        holder.mVerbrauchView.text = String.format("%.2f", geraeteList[position].getJahresverbrauch())
        val raumID = geraeteList[position].getRaumID()
        for(raum in raumListHaushalt) {
            if(raum.getRaumID() == raumID) {
                holder.mRaumView.text = raum.getName()
                break
            }
        }
        //TODO: Nach merge Icon-Array aus Main Activity holen, val mainAct = requireActivity() as MainActivity; val iconArray : Array<Int> = mainAct.getIconArray()
        //Implementierung hier vorerst mit einem Standard-Icon
        val iconArray: Array<Int> = arrayOf(R.drawable.ic_refrigerator)
        val katID = geraeteList[position].getKategorieID()
        for(kategorie in katList) {
            if(kategorie.getKategorieID() == katID) {
                holder.mKatView.setImageResource(iconArray[0]) //TODO iconArray[kategorie.getIcon]
                break
            }
        }


    }

    inner class GeraeteViewHolder(mItemView:View): RecyclerView.ViewHolder(mItemView), View.OnClickListener {
        val mTextView:TextView
        val mCardView:CardView
        val mVerbrauchView:TextView
        val mRaumView:TextView
        val mKatView:ImageView
        init {
            mTextView = mItemView.findViewById(R.id.geraete_recycler_text)
            mVerbrauchView = mItemView.findViewById(R.id.geraete_recycler_verbrauch)
            mCardView = mItemView.findViewById(R.id.geraete_recycler_card)
            mRaumView = mItemView.findViewById(R.id.geraete_recycler_raum)
            mKatView = mItemView.findViewById(R.id.geraete_recycler_image)
            mCardView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(v!=null) {
                val frag = GeraeteEditVerbraucherFragment(geraeteList[layoutPosition], katList, raumListHaushalt)
                val fragMan = v.findFragment<GeraeteFragment>().parentFragmentManager
                //Wichtig: Hier bei R.id. die Fragment View aus dem content_main.xml auswählen! mit dem neuen Fragment ersetzen und dann committen.
                fragMan.beginTransaction().replace(R.id.nav_host_fragment, frag).addToBackStack(null).commit()
            }
        }
    }
}