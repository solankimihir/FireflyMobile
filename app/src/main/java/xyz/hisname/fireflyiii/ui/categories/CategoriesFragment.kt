package xyz.hisname.fireflyiii.ui.categories

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.base_swipe_layout.*
import kotlinx.android.synthetic.main.fragment_base_list.*
import xyz.hisname.fireflyiii.R
import xyz.hisname.fireflyiii.repository.models.category.CategoryData
import xyz.hisname.fireflyiii.ui.base.BaseFragment
import xyz.hisname.fireflyiii.util.extension.create
import xyz.hisname.fireflyiii.util.extension.toastError
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import kotlinx.android.synthetic.main.activity_base.*


class CategoriesFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.create(R.layout.fragment_base_list, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayView()
        pullToRefresh()
    }

    private fun displayView(){
        runLayoutAnimation(recycler_view)
        val dividerItemDecoration = DividerItemDecoration(recycler_view.context, LinearLayoutManager(requireContext()).orientation)
        recycler_view.addItemDecoration(dividerItemDecoration)
        swipeContainer.isRefreshing = true
        categoryViewModel.getAllCategory().observe(this, Observer { categoryData ->
            categoryViewModel.isLoading.observe(this, Observer { loading ->
                if (loading == false) {
                    swipeContainer.isRefreshing = false
                    if (categoryData.isNotEmpty()) {
                        listText.isVisible = false
                        listImage.isVisible = false
                        recycler_view.isVisible = true
                        recycler_view.adapter = CategoriesRecyclerAdapter(categoryData) { data: CategoryData ->  }
                    } else {
                        listText.text = "No category found"
                        listText.isVisible = true
                        listImage.isVisible = true
                        listImage.setImageDrawable(IconicsDrawable(requireContext())
                                .icon(FontAwesome.Icon.faw_chart_bar)
                                .sizeDp(24))
                        recycler_view.isVisible = false
                    }
                }
            })
        })

        categoryViewModel.apiResponse.observe(this, Observer {
            toastError(it)
        })
    }

    private fun pullToRefresh(){
        swipeContainer.setOnRefreshListener {
            displayView()
        }
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)
    }


    override fun onAttach(context: Context){
        super.onAttach(context)
        activity?.activity_toolbar?.title = resources.getString(R.string.categories)
    }

    override fun onResume() {
        super.onResume()
        activity?.activity_toolbar?.title = resources.getString(R.string.categories)
    }

}