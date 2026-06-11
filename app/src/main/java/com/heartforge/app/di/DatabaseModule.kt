import android.content.Context
import androidx.room.Room
import com.heartforge.app.core.database.CharacterDao
import com.heartforge.app.core.database.HeartForgeDatabase
import com.heartforge.app.core.database.MemoryDao
import com.heartforge.app.core.database.RelationshipDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HeartForgeDatabase {
        return Room.databaseBuilder(
            context,
            HeartForgeDatabase::class.java,
            "heartforge.db"
        ).build()
    }

    @Provides
    fun provideCharacterDao(database: HeartForgeDatabase): CharacterDao = database.characterDao()

    @Provides
    fun provideRelationshipDao(database: HeartForgeDatabase): RelationshipDao = database.relationshipDao()

    @Provides
    fun provideMemoryDao(database: HeartForgeDatabase): MemoryDao = database.memoryDao()
}
