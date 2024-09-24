package template.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import template.core.database.AppDatabase
import template.core.database.dao.TeamDao

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesPostDao(
        database: AppDatabase,
    ): TeamDao = database.teamDao()
}
