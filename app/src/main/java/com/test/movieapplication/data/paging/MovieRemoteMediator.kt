package com.test.movieapplication.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.movieapplication.data.database.MovieDatabase
import com.test.movieapplication.data.database.RemoteKeys
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.repository.MovieRepositoryInterface
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val repository: MovieRepositoryInterface,
    private val db: MovieDatabase,
    private val apiKey: String
) : RemoteMediator<Int, Movie>() {

    private val movieDao = db.movieDao()
    private val keysDao = db.remoteKeysDao()

    // Ako kesh-ot e svez (< 1 cas), ne osvezhuvaj pri otvoranje
    override suspend fun initialize(): InitializeAction {
        val lastUpdated = keysDao.get(LABEL)?.lastUpdated ?: 0L
        val isFresh = System.currentTimeMillis() - lastUpdated < CACHE_TIMEOUT
        return if (isFresh) InitializeAction.SKIP_INITIAL_REFRESH
        else InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val keys = keysDao.get(LABEL)
                when {
                    keys == null -> 1 // nema keshirano, pochni od pocetok
                    keys.nextPage == null ->
                        return MediatorResult.Success(endOfPaginationReached = true)
                    else -> keys.nextPage
                }
            }
        }

        return try {
            val response = repository.getPopularMovies(apiKey, page)

            val movies = response.results.orEmpty()
                .filter { it.poster_path != null && it.overview != null }
                .mapIndexed { index, movie ->
                    movie.copy(position = (page - 1) * 1000 + index)
                }

            val endReached = page >= response.total_pages

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearAll()
                }
                movieDao.insertMovieList(movies)
                keysDao.insert(
                    RemoteKeys(
                        label = LABEL,
                        nextPage = if (endReached) null else page + 1,
                        lastUpdated = System.currentTimeMillis()
                    )
                )
            }
            MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        const val LABEL = "popular"
        val CACHE_TIMEOUT = TimeUnit.HOURS.toMillis(1)
    }
}