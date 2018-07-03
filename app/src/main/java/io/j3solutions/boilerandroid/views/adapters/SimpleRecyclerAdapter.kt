package io.j3solutions.boilerandroid.views.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.rxlifecycle2.RxController
import io.j3solutions.boilerandroid.persistence.Db
import io.j3solutions.boilerandroid.utils.subscribe
import io.reactivex.Flowable

class SimpleRecyclerAdapter<T>(
	val itemLayoutResource: Int,
	val itemSetter: (View, T, SimpleRecyclerAdapter<T>) -> Unit,
	override val noItemsView: View? = null
) : RecyclerView.Adapter<SimpleRecipeViewHolder<T>>(), MutableListAdapter<T> {
	override var items: MutableList<T> = mutableListOf()
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRecipeViewHolder<T> {
		return SimpleRecipeViewHolder<T>(LayoutInflater
			.from(parent.context)
			.inflate(itemLayoutResource, parent, false), itemSetter, this)
	}

	override fun onBindViewHolder(holder: SimpleRecipeViewHolder<T>, position: Int) {
		val item = items[position]
		holder.setItem(item)
	}

	override fun getItemCount(): Int = items.size

	fun populate(context: RxController?, query: (Db) -> (Flowable<List<T>>)) {
		Db.instance { db ->
			query(db).subscribe(context) {
				this.populate(it)
			}
		}
	}
}

open class SimpleRecipeViewHolder<T>(val view: View, val itemSetter: (View, T, SimpleRecyclerAdapter<T>) -> Unit, val adapter: SimpleRecyclerAdapter<T>) : RecyclerView.ViewHolder(view) {
	open fun setItem(item: T) {
		itemSetter(view, item, adapter)
	}
}