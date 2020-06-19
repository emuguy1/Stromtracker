package com.example.stromtracker.ui.haushalt

import android.content.Intent
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.stromtracker.R
import com.example.stromtracker.ui.haushalteBearbeiten_Loeschen.HaushaltBearbeitenLoeschenFragment
import java.util.*
import kotlin.random.Random

class ListAdapterHaushalt() : RecyclerView.Adapter<ListAdapterHaushalt.ViewHolder>() {
    private val data =
        List(50) { ('A' + Random.nextInt('Z' - 'A')).toString() + " " }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_haushalt, parent, false)

        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: String = data[position]
        holder.textView.text = item
        holder.textView

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView = itemView.findViewById(R.id.textview_haushalt)
        init {
            textView.setOnClickListener(this)
        }
        override fun onClick(view: View?) {
            if (view != null) {
                Toast.makeText(view.context,
                    String.format(
                        Locale.GERMAN,
                        "Position: %d was clicked",
                        layoutPosition),
                    Toast.LENGTH_SHORT).show()

            }
        }
    }
}