package com.android1500.gpssetter.room

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import androidx.room.Room
import com.android1500.gpssetter.update.GitHubService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun createGitHubService(): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.github.com/repos/Android1500/GpsSetter/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideDownloadManger(application: Application)= application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager



    @Singleton
    @Provides
    fun provideGithubService(retrofit: Retrofit):GitHubService = retrofit.create(GitHubService::class.java)



    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: AppDatabase.Callback)
            = Room.databaseBuilder(application, AppDatabase::class.java, "user_database")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun providesUserDao(userDatabase: AppDatabase) =
        userDatabase.userDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope