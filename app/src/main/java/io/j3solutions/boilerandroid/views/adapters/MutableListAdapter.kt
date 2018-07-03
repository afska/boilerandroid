package io.j3solutions.boilerandroid.views.adapters


import android.view.View
import io.j3solutions.boilerandroid.utils.hide
import io.j3solutions.boilerandroid.utils.show

interface MutableListAdapter<T> {
	val noItemsView: View?
	val items: MutableList<T>
	fun notifyItemInserted(position: Int)
	fun notifyItemRemoved(position: Int)
	fun notifyItemRangeInserted(start: Int, end: Int)
	fun notifyItemChanged(position: Int)
	fun notifyDataSetChanged()

	operator fun get(pos: Int): T? = items[pos]

	fun hasItems(): Boolean {
		return items.isNotEmpty()
	}

	fun populate(list: List<T>) {
		this.items.clear()
		this.items.addAll(list)
		this.notifyDataSetChanged()
		if (!items.isEmpty()) noItemsView?.hide() else noItemsView?.show()
	}

	fun addFirst(item: T) {
		this.items.add(0, item)
		notifyItemInserted(0)
	}

	fun addItem(item: T) {
		this.items.add(item)
		notifyItemInserted(items.lastIndex)
		if (!items.isEmpty()) noItemsView?.hide()
	}

	fun addItems(newItems: List<T>) {
		val oldSize = this.items.size
		this.items.addAll(newItems)
		this.notifyItemRangeInserted(oldSize, items.lastIndex)
		if (!items.isEmpty()) noItemsView?.hide()
	}

	fun removeItem(item: T) {
		val index = items.indexOf(item)
		if (index < 0) return
		items.removeAt(index)
		notifyItemRemoved(index)
		if (items.isEmpty()) noItemsView?.show()
	}

	fun updateItem(item: T) {
		val index = items.indexOf(item)
		if (index < 0) return
		items.removeAt(index)
		items.add(index, item)
		notifyItemChanged(index)
	}
}