package io.j3solutions.boilerandroid.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class SimplePagerAdapter<T>(
    private val context: Context?,
    private val layout: Int,
    private val itemSetter: (View, T, SimplePagerAdapter<T>) -> Unit,
    private val pageWidth: Float = 1f
) : PagerAdapter() {
    var items: List<T> = listOf()

    fun populate(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(layout, container, false) as ViewGroup

        itemSetter(layout, items.get(position), this)

        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getPageWidth(position: Int): Float {
        return pageWidth
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }
}
