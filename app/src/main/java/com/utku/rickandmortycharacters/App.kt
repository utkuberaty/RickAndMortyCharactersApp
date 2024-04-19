package com.utku.rickandmortycharacters

import android.app.Application
import com.utku.rickandmortycharacters.di.networkModule
import com.utku.rickandmortycharacters.di.repositoryModule
import com.utku.rickandmortycharacters.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(networkModule, repositoryModule, viewModelModule)
        }
    }
}