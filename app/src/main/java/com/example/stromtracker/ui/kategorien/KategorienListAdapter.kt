package com.example.stromtracker.ui.kategorien

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R

class KategorienListAdapter(private val myDataset: Array<String>) : RecyclerView.Adapter<KategorienListAdapter.MyViewHolder>() {
    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): KategorienListAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val textView = layoutInflater.inflate(R.layout.fragment_kategorien_recycler_text_view, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text= myDataset[position]
        holder.textView.text = myDataset[position]
    }
    override fun getItemCount() = myDataset.size

}