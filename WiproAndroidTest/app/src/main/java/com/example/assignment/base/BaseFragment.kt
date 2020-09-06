/*
 * BaseFragment.kt
 * Demo 1.0
 *
 */
package com.example.assignment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get

/**
 * This Fragment is abstract . This will be extended by all Fragments of app
 *
 * @param <V> is the type of View model
 * @param <D> is the type of ViewDataBinding
</D></V> */
abstract class BaseFragment<V : ViewModel, D : ViewDataBinding> :
    Fragment() {

    protected lateinit var mViewDataModel: V
    protected lateinit var mDataBinding: D

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutRef, container, false)
        return mDataBinding?.getRoot()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataModel=ViewModelProvider(this).get(viewModelClass!!)
       //mViewDataModel = ViewModelProviders.of(this).get(viewModelClass!!)
        mDataBinding.setVariable(bindingVariable, mViewDataModel)
        mDataBinding.lifecycleOwner = viewLifecycleOwner
        mDataBinding.executePendingBindings()
        initViews()
        initObservers()
        initListeners()
    }

    /**
     * Method will be overridden by child classes to initialize the listeners
     */
    protected abstract fun initListeners()

    /**
     * Method will be overridden by child classes to initialize the observers
     */
    protected abstract fun initObservers()

    /**
     * Method will be overridden by child classes to initialize the views
     */
    protected abstract fun initViews()

    /**
     * Method will be overridden by child classes to get the bind variable
     */
    protected abstract val bindingVariable: Int

    /**
     * Method will be overridden by child classes to get the View Model type
     */
    protected abstract val viewModelClass: Class<V>?

    /**
     * Method will be overridden by child classes to get the layout reference
     */
    @get:LayoutRes
    protected abstract val layoutRef: Int

    override fun onDestroyView() {
        mDataBinding?.run { unbind() }
        clearAllReferences()
        super.onDestroyView()
    }

    @VisibleForTesting
    open fun getDataBinding(): D {
        return mDataBinding
    }

    /**
     * Method will be overridden by child classes to clear all references
     */
    protected abstract fun clearAllReferences()
}