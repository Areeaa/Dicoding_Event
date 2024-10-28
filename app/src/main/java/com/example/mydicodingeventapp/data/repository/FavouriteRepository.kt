
import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mydicodingeventapp.data.database.FavouriteDao
import com.example.mydicodingeventapp.data.database.FavouriteDatabase
import com.example.mydicodingeventapp.data.database.FavouriteEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavouriteRepository (application: Application){

    private val mFavouriteDao: FavouriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()


    init {
        val db = FavouriteDatabase.getDatabase(application)
        mFavouriteDao = db.favouriteDao()
    }

    fun insert(favouriteEvent: FavouriteEvent){
        executorService.execute{mFavouriteDao.insert(favouriteEvent)}
    }

    fun delete(favouriteEvent: FavouriteEvent) {
        executorService.execute { mFavouriteDao.delete(favouriteEvent) }
    }

    fun getFavouriteEventById(id: String): LiveData<FavouriteEvent?> {
        return mFavouriteDao.getFavouriteEventById(id)
    }

    fun getAllFavoriteEvents(): LiveData<List<FavouriteEvent>> {
        return mFavouriteDao.getAllFavoriteEvent()
    }
}


