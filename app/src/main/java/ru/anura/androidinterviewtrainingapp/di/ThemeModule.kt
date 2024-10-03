package ru.anura.androidinterviewtrainingapp.di

import dagger.Module
import dagger.Provides
import ru.anura.androidinterviewtrainingapp.domain.entity.Theme
import javax.inject.Named

@Module
class ThemeModule(private val theme: Theme) {

    @Provides
    fun provideTheme(): Theme {
        // Здесь можно задать логику для получения нужного значения Theme
        return theme// Например, возвращаем тему LIGHT
    }
}