package com.example.assignment.ui.feeds

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinle.CloudManager
import com.example.assignment.R
import com.example.assignment.app.MyApplication
import com.example.assignment.repository.model.FeedData
import com.example.assignment.repository.model.Row
import com.example.assignment.utils.AppURL
import com.example.assignment.utils.NetworkUtils
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *   FeedViewModel class is responsible to provide feed data to view
 *
 */
class FeedViewModel (application: Application) : AndroidViewModel(application){

    @Inject
    lateinit var mCloudManager: CloudManager

    init {
        MyApplication.mAppComponent?.inject(this)
    }

    private val mFeedCloudData=MutableLiveData<FeedData>()
    private val mUpdateServerstatus=MutableLiveData<String>()
    lateinit var mCustomRecyclerAdapter: FeedRecyclerAdapter
    val isLoading = ObservableBoolean()

    /**
     * method to fetch feed from server or local
     */
    fun fetchFeedsFromLocal() {
        if(mFeedCloudData.value==null){
            fetchFeedsFromCloud()
        }else{
            mFeedCloudData.postValue(mFeedCloudData.value)
            isLoading.set(false)
        }
    }

    /**
     * method to fetch feed from server
     */
    fun fetchFeedsFromCloud(){
        if(NetworkUtils.isNetworkConnected(getApplication())){
            isLoading.set(true)
            viewModelScope.launch {
                try {
                    var cloudResponse=mCloudManager.fatchFeeds()
                    if (cloudResponse==null){
                        mUpdateServerstatus.postValue(getApplication<Application>().resources.getString(
                            R.string.api_issue_text))

                    }else if (cloudResponse.toString().isEmpty()){
                        mUpdateServerstatus.postValue(getApplication<Application>().resources.getString(R.string.api_empty_text
                        ))
                    }else{
                        mFeedCloudData.postValue(cloudResponse)
                    }
                    isLoading.set(false)
                }catch (socketexception: SocketTimeoutException){
                    mUpdateServerstatus.postValue(socketexception.message)
                }catch (exception:Exception){
                    mUpdateServerstatus.postValue(getApplication<Application>().resources.getString(
                        R.string.api_issue_text))
                }
            }
        }else{
            Log.i(">>>>>>>>",AppURL.NETWORK_STATUS_MSG)
            isLoading.set(false)
            mUpdateServerstatus.postValue(getApplication<Application>().resources.getString(R.string.no_network_text))
        }
    }

    fun getServerStatus():MutableLiveData<String>{
        return mUpdateServerstatus
    }

    fun getFeedData():MutableLiveData<FeedData>{
        return mFeedCloudData
    }

    /**
     * return feed rows record
     */
    fun getFeedRows(position:Int): Row? {
        return mFeedCloudData.value?.rows?.get(position)
    }

    /**
     * set adapter value
     * */
    fun getDashboardAdapter(): FeedRecyclerAdapter {
        mCustomRecyclerAdapter=
            FeedRecyclerAdapter(
                R.layout.recycler_view_item,
                this
            )
        return mCustomRecyclerAdapter
    }

    /**
     * method to set updated feeds record in list
     */
    fun setDashBoardAdapter(rowsItem: List<Row>?) {
        mCustomRecyclerAdapter.setFeedList(rowsItem!!)
        mCustomRecyclerAdapter.notifyDataSetChanged()
    }
    //* onRefresh() - Needs to be public for Databinding! */
    fun onRefresh() {
        fetchFeedsFromCloud()
    }

}