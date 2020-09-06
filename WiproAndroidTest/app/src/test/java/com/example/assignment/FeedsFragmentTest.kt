package com.example.assignment

import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.assignment.ui.feeds.FeedsFragment
import com.example.assignment.utils.AppURL
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk= intArrayOf(28))
class FeedsFragmentTest {

    private var mFeedsFragment: FeedsFragment? = null
    private var mFragmentScenario: FragmentScenario<FeedsFragment>? = null

    @Before
    fun setUp() {

        mFragmentScenario = FragmentScenario.launch<FeedsFragment>(
            FeedsFragment::class.java)
        mFragmentScenario!!.moveToState(Lifecycle.State.RESUMED)
        mFragmentScenario!!.onFragment { settingsFragment: FeedsFragment -> mFeedsFragment = settingsFragment
        }
    }

    @Test
    fun testFragment(){
        Assert.assertNotNull(mFeedsFragment)
    }

    @Test
    fun clearFragment() {
        mFragmentScenario!!.moveToState(Lifecycle.State.DESTROYED)
        assertEquals(Lifecycle.State.INITIALIZED,mFeedsFragment!!.lifecycle.currentState)
    }

    @Test
    fun getServerStatusApiFail(){

        mFeedsFragment?.getServerStatus(AppURL.FAIL_MSG)
        assertEquals(mFeedsFragment!!.getDataBinding().includeErrorMessage.rlErrorMessage.visibility,View.VISIBLE)
        assertEquals(mFeedsFragment!!.getDataBinding().includeErrorMessage.tvError.text,"fail")
    }

    @Test
    fun testGetServerStatusNetWorkCheck(){

        mFeedsFragment?.getServerStatus(AppURL.NETWORK_STATUS_MSG)
        assertEquals(mFeedsFragment!!.getDataBinding().includeErrorMessage.rlErrorMessage.visibility,View.VISIBLE)
        assertEquals(mFeedsFragment!!.getDataBinding().includeErrorMessage.tvError.text,"networtstatus")
    }

    @Test
    fun testErrorCloseClicked(){
        mFeedsFragment?.errorCloseClicked(true)
        assertEquals(mFeedsFragment!!.getDataBinding().includeErrorMessage.rlErrorMessage.visibility,View.GONE)
    }

    @After
    fun tearDown() {
        mFeedsFragment==null
        mFragmentScenario==null
    }
}