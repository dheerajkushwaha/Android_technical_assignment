package com.example.assignment.di

import com.example.assignment.ui.feeds.FeedViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [APIModule::class])
interface AppComponent {

        fun inject(dashboardViewModel: FeedViewModel)
}