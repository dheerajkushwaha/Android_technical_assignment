package com.example.assignment

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.assignment.app.MyApplication
import com.example.assignment.di.AppComponent
import com.example.assignment.repository.model.FeedData
import com.example.assignment.repository.model.Row
import com.example.assignment.rules.TestCoroutineRule
import com.example.assignment.ui.feeds.FeedRecyclerAdapter
import com.example.assignment.ui.feeds.FeedViewModel
import com.example.assignment.utils.NetworkUtils
import com.example.kotlinle.CloudManager
import com.nhaarman.mockitokotlin2.argumentCaptor
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.api.mockito.PowerMockito.*
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.robolectric.annotation.LooperMode


@ExperimentalCoroutinesApi
@RunWith(PowerMockRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@PowerMockIgnore("javax.net.ssl.*")
@PrepareForTest(MyApplication::class, NetworkUtils::class, Log::class, AndroidViewModel::class)
class FeedViewModelTest {

    @Rule
    var testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    lateinit var context: Application
    private val resources: Resources = mock(Resources::class.java)
    private var ctx: Context? = null
    lateinit var mFeedViewModel: FeedViewModel
    lateinit var mAppComponent: AppComponent
     var mCloudManager: CloudManager =
    mock(CloudManager::class.java)//CloudManager()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        context = mock(Application::class.java)
        mAppComponent = mock(AppComponent::class.java)
        mockStatic(MyApplication::class.java)
        mockStatic(NetworkUtils::class.java)
        mockStatic(Log::class.java)
        mockStatic(AndroidViewModel::class.java)
        ctx = mock(Context::class.java)
        mCloudManager= mock(CloudManager::class.java)
        mFeedViewModel = FeedViewModel((context))
        Dispatchers.setMain(Dispatchers.Unconfined)
        `when`(context.resources).thenReturn(resources)

        mFeedViewModel.mCloudManager = mCloudManager
    }


    @Test
    fun testFetchFeedsFromCloud(){

        val connectivityManager = Mockito.mock(
            ConnectivityManager::class.java
        )
        val networkInfo = Mockito.mock(NetworkInfo::class.java)

        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(connectivityManager)
        Mockito.`when`(connectivityManager.activeNetworkInfo)
            .thenReturn(networkInfo)
        Mockito.`when`(networkInfo.isConnected).thenReturn(true)

        testCoroutineRule.runBlockingTest {
            `when`(mCloudManager.fatchFeeds()).thenReturn(FeedData(arrayListOf(),"title"))
            mFeedViewModel.fetchFeedsFromCloud()
            assertEquals("title", mFeedViewModel.getFeedData().value?.title)
        }
    }

    @Test
    fun testFetchFeedsFromCloud_withoutInternet(){

        val connectivityManager = Mockito.mock(
            ConnectivityManager::class.java
        )
        val networkInfo = Mockito.mock(NetworkInfo::class.java)

        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(connectivityManager)
        Mockito.`when`(connectivityManager.activeNetworkInfo)
            .thenReturn(networkInfo)
        Mockito.`when`(networkInfo.isConnected).thenReturn(false)

        val dummyNullResponse = "cloudResponseNull"
        `when`(resources.getString(anyInt())).thenReturn(dummyNullResponse)

        mFeedViewModel.fetchFeedsFromCloud()

        assertEquals(dummyNullResponse,getPrivateFieldValue<MutableLiveData<String>>(mFeedViewModel,"mUpdateServerstatus").value)

    }
    @Test
    fun testFetchFeedsFromCloud_cloudResponseNull() {

        val connectivityManager = Mockito.mock(
            ConnectivityManager::class.java
        )
        val networkInfo = Mockito.mock(NetworkInfo::class.java)

        Mockito.`when`(context.getSystemService(Context.CONNECTIVITY_SERVICE))
            .thenReturn(connectivityManager)
        Mockito.`when`(connectivityManager.activeNetworkInfo)
            .thenReturn(networkInfo)
        Mockito.`when`(networkInfo.isConnected).thenReturn(true)

        val dummyNullResponse = "cloudResponseNull"
        `when`(resources.getString(anyInt())).thenReturn(dummyNullResponse)

        testCoroutineRule.runBlockingTest {
            `when`(mCloudManager.fatchFeeds()).thenReturn(null)
            mFeedViewModel.fetchFeedsFromCloud()
            assertEquals(dummyNullResponse,getPrivateFieldValue<MutableLiveData<String>>(mFeedViewModel,"mUpdateServerstatus").value)
        }
    }
    @Test
    fun testGetServerStatus() {

        val dummyServerStatus = "TEST"
        getPrivateFieldValue<MutableLiveData<String>>(
            mFeedViewModel,
            "mUpdateServerstatus"
        ).value = dummyServerStatus
        assertEquals(dummyServerStatus, mFeedViewModel.getServerStatus().value)
    }

    @Test
    fun testGetFeedData() {
        val dummyFeedData = FeedData(arrayListOf(), "TEST")
        getPrivateFieldValue<MutableLiveData<FeedData>>(
            mFeedViewModel,
            "mFeedCloudData"
        ).value = dummyFeedData
        assertEquals(dummyFeedData, mFeedViewModel.getFeedData().value)
    }

    @Test
    fun testGetFeedRows() {
        val dummyRow = Row("TEST DESCRIPTION", "TEST IMG REF", "TEST TITLE")
        val dummyFeedData = FeedData(arrayListOf(dummyRow), "TEST")
        getPrivateFieldValue<MutableLiveData<FeedData>>(
            mFeedViewModel,
            "mFeedCloudData"
        ).value = dummyFeedData
        assertEquals(dummyRow, mFeedViewModel.getFeedRows(0))
    }

    @Test
    fun testGetDashboardAdapter() {
        assertEquals(mFeedViewModel, mFeedViewModel.getDashboardAdapter().mViewDataModel)
    }


    @Test
    fun testSetDashboardAdapter() {
        val dummyRowsItem = arrayListOf(Row("TEST DESCRIPTION", "TEST IMG REF", "TEST TITLE"))
        val mockedAdapter = mock(FeedRecyclerAdapter::class.java)
        mFeedViewModel.mCustomRecyclerAdapter = mockedAdapter
        mFeedViewModel.setDashBoardAdapter(dummyRowsItem)
        val rowsCaptor = argumentCaptor<List<Row>>()
        verify(mockedAdapter).setFeedList(rowsCaptor.capture())
        assertEquals(dummyRowsItem, rowsCaptor.firstValue)
        verify(mockedAdapter).notifyDataSetChanged()
    }

    @After
    fun tearDown() {
        ctx = null
    }
}
