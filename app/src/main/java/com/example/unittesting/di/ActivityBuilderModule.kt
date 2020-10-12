package com.example.unittesting.di

import com.example.unittesting.NotesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(

    )
    abstract fun contributesNotesListActivity(): NotesListActivity
}
