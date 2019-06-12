package io.j3solutions.boilerandroid.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.j3solutions.boilerandroid.persistence.Database
import io.j3solutions.boilerandroid.utils.extensions.setUpThreads
import io.reactivex.Flowable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable
import timber.log.Timber

class SimpleRecyclerAdapter<T>(
    private val itemLayoutResource: Int,
    private val itemSetter: (View, T, SimpleRecyclerAdapter<T>) -> Unit,
    override val noItemsView: View? = null
) : RecyclerView.Adapter<SimpleRecipeViewHolder<T>>(), MutableListAdapter<T> {
    override var items: MutableList<T> = mutableListOf()

    @CheckReturnValue
    fun populate(query: (Database) -> (Flowable<List<T>>)): Disposable {
        return populateWithListener(query)
    }

    @CheckReturnValue
    fun populateWithListener(
        query: (Database) -> (Flowable<List<T>>),
        onError: (Throwable) -> (Unit) = {},
        onPopulate: (List<T>) -> (Unit) = {}
    ): Disposable {
        return Database.instance { db ->
            query(db).setUpThreads().subscribe({
                this.populate(it)
                onPopulate(it)
            }, {
                onError(it)
                Timber.e(it)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleRecipeViewHolder<T> {
        return SimpleRecipeViewHolder<T>(
            LayoutInflater
                .from(parent.context)
                .inflate(itemLayoutResource, parent, false), itemSetter, this
        )
    }

    override fun onBindViewHolder(holder: SimpleRecipeViewHolder<T>, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int = items.size
}

open class SimpleRecipeViewHolder<T>(
    val view: View,
    val itemSetter: (View, T, SimpleRecyclerAdapter<T>) -> Unit,
    private val adapter: SimpleRecyclerAdapter<T>
) : RecyclerView.ViewHolder(view) {
    open fun setItem(item: T) {
        itemSetter(view, item, adapter)
    }
}
