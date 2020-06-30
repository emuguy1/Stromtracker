package com.example.stromtracker.ui.geraete

import android.text.Layout
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
import java.util.*

class GeraeteListAdapter(private val geraeteList: List<Geraete>): RecyclerView.Adapter<GeraeteListAdapter.GeraeteViewHolder>() {
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

    }

    inner class GeraeteViewHolder(mItemView:View): RecyclerView.ViewHolder(mItemView), View.OnClickListener {
        val mTextView:TextView
        val mCardView:CardView
        init {
            mTextView = mItemView.findViewById(R.id.geraete_recycler_text)
            mCardView = mItemView.findViewById(R.id.geraete_recycler_card)
            mCardView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if(v!=null) {
                /*Toast.makeText(v.context,
                    String.format(
                        Locale.GERMAN,
                        "Position: %d is clicked.",
                        layoutPosition),
                    Toast.LENGTH_SHORT).show()

                 */
                //editfragment adden
            }
        }
    }
}