package com.example.assignment.ui.feeds

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.assignment.BR
import com.example.assignment.R
import com.example.assignment.base.BaseFragment
import com.example.assignment.databinding.DashboardFramgentBinding
import com.example.assignment.errors.ErrorViewModel
import com.example.assignment.repository.model.FeedData

/**
 *   FeedsFragment class is responsible to receive feed data to viewmodel
 *
 */
class FeedsFragment : BaseFragment<FeedViewModel, DashboardFramgentBinding>(){
    private lateinit var mRecyclerView: RecyclerView
    lateinit var mErrorViewModel: ErrorViewModel
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mErrorViewModel = ViewModelProviders.of(requireActivity()).get(ErrorViewModel::class.java)

    }

    override fun initListeners() {

    }
    /**
     * intitialised all observer
     */
    override fun initObservers() {

        mErrorViewModel.getCloseButtonClick()?.observe(this, Observer { isClicked:Boolean->errorCloseClicked(isClicked) })
        mViewDataModel.getServerStatus().observe(this, Observer { message:String->getServerStatus(message) })
        mViewDataModel.getFeedData().observe(this, Observer { feedsData: FeedData ->getFeedsData(feedsData) })
    }

    /**
     * Error close button click
     *
     * @param isClicked
     */
    fun errorCloseClicked(isClicked: Boolean) {
        if (isClicked) {
            mDataBinding.includeErrorMessage.rlErrorMessage.setVisibility(View.INVISIBLE)
        }
    }


    /**
     * method to update list with feed records
     * */
    private fun getFeedsData(feedsData:FeedData){
        activity?.setTitle(feedsData.title)
        mRecyclerView.layoutManager = LinearLayoutManager(
            requireActivity(),
            LinearLayoutManager.VERTICAL, false
        )
        mViewDataModel.setDashBoardAdapter(feedsData.rows)
    }

    /**
     * initialise view
     */
    override fun initViews() {
        mRecyclerView=mDataBinding.listNews
        mDataBinding.includeErrorMessage.errorVM=mErrorViewModel
        mSwipeRefreshLayout=mDataBinding.swiperefresh
        mViewDataModel.fetchFeedsFromLocal()
    }

    /**
     * update error scenario while loading data
     */
    fun getServerStatus(message: String?) {
       if (message != null) {
           mDataBinding.includeErrorMessage.rlErrorMessage.visibility = View.VISIBLE
            mSwipeRefreshLayout.isRefreshing=false
            mDataBinding.includeErrorMessage.tvError.setText(message)
       }
     }
    override val bindingVariable: Int
        get() = BR.dashboardViewModelMain
    override val viewModelClass: Class<FeedViewModel>?
        get() = FeedViewModel::class.java
    override val layoutRef: Int
        get() = R.layout.dashboard_framgent

    /**
     * clear all references
     */
    override fun clearAllReferences() {
    }

}