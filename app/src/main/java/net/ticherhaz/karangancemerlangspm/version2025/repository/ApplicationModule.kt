package net.ticherhaz.karangancemerlangspm.version2025.repository

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import net.ticherhaz.karangancemerlangspm.version2025.tools.QuickSave

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideQuickSave(): QuickSave {
        return QuickSave.getInstance()
    }

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}