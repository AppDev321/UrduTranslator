package com.dictionary.module

import android.content.Context
import android.media.MediaPlayer
import com.dictionary.model.SettingsDataFactory
import com.dictionary.utils.GoogleTTS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UICommonModule {

    @Provides
    @Singleton
    fun provideFragmentDTODataFactory(
        @ApplicationContext appContext: Context,
    ) = SettingsDataFactory(appContext)


    @Provides
    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    @Provides
    @Singleton
    fun provideGoogleTTSSupport(mediaPlayer: MediaPlayer): GoogleTTS {
        return GoogleTTS(mediaPlayer)
    }
}