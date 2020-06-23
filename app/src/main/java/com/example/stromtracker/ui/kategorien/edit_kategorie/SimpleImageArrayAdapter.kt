package com.example.stromtracker.ui.kategorien.edit_kategorie

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.stromtracker.R


class SimpleImageArrayAdapter(context: Context, private var images: Array<Int>) : ArrayAdapter<Int>(context, R.layout.fragment_kategorien_spinner_row, images) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        return getImageForPosition(position)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this!!.getImageForPosition(position)!!
    }

    private fun getImageForPosition(position: Int): View? {

        val imageView = ImageView(context)
        imageView.setBackgroundResource(images.get(position))
        imageView.setLayoutParams(
            AbsListView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        return imageView
    }
}