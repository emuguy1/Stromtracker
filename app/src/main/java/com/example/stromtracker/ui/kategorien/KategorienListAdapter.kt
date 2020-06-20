package com.example.stromtracker.ui.kategorien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import java.util.*

class KategorienListAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<KategorienListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): KategorienListAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val mItemView = layoutInflater.inflate(R.layout.fragment_kategorien_recycler_text_view, parent, false)

        return MyViewHolder(mItemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTextView.text= myDataset[position]
    }
    override fun getItemCount() = myDataset.size

    inner class MyViewHolder(val mItemView:View) : RecyclerView.ViewHolder(mItemView), View.OnClickListener {
        val mTextView:TextView
        val mCardView:CardView
        init {
            mCardView = mItemView.findViewById(R.id.kategorien_recycler_card)
            mCardView.setOnClickListener(this)
            mTextView = mItemView.findViewById<TextView>(R.id.kategorien_recycler_text)
        }

        override fun onClick(v: View?) {
            if (v != null) {
                Toast.makeText(v.context,
                    String.format(
                        Locale.GERMAN,
                        "Position: %d is clicked.",
                        layoutPosition),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}