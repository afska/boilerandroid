package io.j3solutions.boilerandroid.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class RecyclerAdapter<T, TViewHolder : RecyclerViewHolder<T>>(
	private val items: MutableList<T>
) : RecyclerView.Adapter<TViewHolder>() {
	protected abstract fun createViewHolder(view: View): TViewHolder
	abstract fun getLayout(viewType: Int): Int

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TViewHolder {
		val layout = getLayout(viewType)
		return createViewHolder(
			LayoutInflater.from(parent.context).inflate(layout, parent, false)
		)
	}

	override fun onBindViewHolder(holder: TViewHolder, position: Int) {
		holder.bind(items[position])
	}

	override fun getItemCount() = items.size

	operator fun get(pos: Int): T = items[pos]

	override fun getItemViewType(position: Int) = getViewTypeOf(items[position])

	fun populate(list: List<T>) {
		items.clear()
		items.addAll(list)
		notifyDataSetChanged()
	}

	open fun getViewTypeOf(item: T) = 0
}

abstract class RecyclerViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
	abstract fun bind(item: T)
}
