package com.example.assignment

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.assignment.app.MyApplication
import com.example.assignment.di.AppComponent
import com.example.assignment.repository.model.FeedData
import com.example.assignment.repository.model.Row
import com.example.assignment.ui.feeds.FeedViewModel
import com.example.assignment.utils.NetworkUtils
import junit.framework.Assert.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito.*
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.robolectric.annotation.LooperMode


@RunWith(PowerMockRunner::class)
@LooperMode(LooperMode.Mode.PAUSED)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*","androidx.test,*","androidx.test.*","androidx.work.*","androidx.lifecycle.*")
@PrepareForTest(MyApplication::class, NetworkUtils::class, Log::class, AndroidViewModel::class)

class FeedViewModelTest {

    lateinit var context: Application
    private var ctx: Context? = null
    lateinit var dashboardViewModel: FeedViewModel
    lateinit var mAppComponent: AppComponent

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
        dashboardViewModel =
            FeedViewModel((context))

    }

    @After
    fun tearDown() {
        ctx = null
    }


    @Test
    fun testFetchFeedsFromLocal_when_feedData_value_null() {

        //GIVEN
        val mockConnectivityManager: ConnectivityManager = mock(ConnectivityManager::class.java)
        //WHEN
        `when`(context?.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            mockConnectivityManager
        )
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(null)
        `when`(NetworkUtils.isNetworkConnected(context)).thenReturn(null)
        dashboardViewModel.getServerStatus().postValue("temp")
        dashboardViewModel.fetchFeedsFromLocal()
        verify(context)
    }

    @Test
    fun testGetServerStatus() {
        assertNotNull(dashboardViewModel.getServerStatus())
    }

    @Test
    fun testGetFeedData() {
        assertNotNull(dashboardViewModel.getFeedData())
    }

    @Test
    fun testGetArticle() {
        assertNull(dashboardViewModel.getFeedRows(0))
    }

    @Test
    fun testGetDashboardAdapter() {
        assertNotNull(dashboardViewModel.getDashboardAdapter())
    }

}
