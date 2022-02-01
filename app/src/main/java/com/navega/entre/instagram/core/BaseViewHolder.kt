package com.navega.entre.instagram.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemsView: View) : RecyclerView.ViewHolder(itemsView) {

    abstract fun bind(items: T)
}