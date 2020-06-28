package com.example.stromtracker.ui.kategorien

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.stromtracker.R


class SimpleImageArrayAdapter(context: Context, private var images: Array<Int>) : ArrayAdapter<Int>(context, R.layout.fragment_kategorien_spinner_row, images) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return getImageForPosition(position, parent!!)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getImageForPosition(position, parent)!!
    }

    private fun getImageForPosition(position: Int, parent: ViewGroup): View? {
        val imageView = ImageView(context)
        val height = parent.resources.getDimension(R.dimen.spinner_icon_dropdown_height)
        val width = parent.resources.getDimension(R.dimen.spinner_icon_dropdown_width)
        //Hier wird die Höhe und Breite der DropdownBilder festgelegt
        imageView.layoutParams = ViewGroup.LayoutParams(width.toInt(), height.toInt())
        imageView.setImageResource(images.get(position))


        return imageView
    }
}