package com.example.stromtracker.ui.kategorien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import java.util.*

class KategorienListAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<KategorienListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): KategorienListAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val textView = layoutInflater.inflate(R.layout.fragment_kategorien_recycler_text_view, parent, false) as TextView

        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text= myDataset[position]
    }
    override fun getItemCount() = myDataset.size

    inner class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView), View.OnClickListener {

        init {
            textView.setOnClickListener(this)
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