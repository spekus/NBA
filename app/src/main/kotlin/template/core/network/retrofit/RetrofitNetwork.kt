package template.core.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import template.BuildConfig
import template.core.network.NetworkDataSource
import template.core.network.model.GamesDto
import template.core.network.model.PlayersDto
import template.core.network.model.TeamsDto
import javax.inject.Inject
import javax.inject.Singleton

private const val KEY = "f413cdcd-3f39-4b50-965d-e99e2fecb191"
private const val LAST_SEASON_DATE = "2023"
private const val DEFAULT_PAGE_SIZE = 75
private const val INITIAL_CURSOR = 0
private const val EMPTY = ""

private interface RetrofitAppNetworkApi {
    @GET(value = "teams")
    suspend fun getTeams(
        @Header("Authorization") auth: String = KEY,
    ): TeamsDto

    @GET(value = "games")
    suspend fun getGames(
        @Query("team_ids[]") id: Int,
        @Header("Authorization") auth: String = KEY,
        @Query("seasons[]") season: String = LAST_SEASON_DATE,
        @Query("per_page") perPage: Int = DEFAULT_PAGE_SIZE,
        @Query("cursor") cursor: Int = INITIAL_CURSOR,
    ): GamesDto

    @GET(value = "players")
    suspend fun getPlayers(
        @Header("Authorization") auth: String = KEY,
        @Query("search") name: String,
        @Query("per_page") perPage: Int = DEFAULT_PAGE_SIZE,
        @Query("cursor") cursor: Int = INITIAL_CURSOR,
    ): PlayersDto
}

@Singleton
class RetrofitNetwork @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : NetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType()),
        )
        .build()
        .create(RetrofitAppNetworkApi::class.java)

    override suspend fun getTeams() = networkApi.getTeams()

    private var idAndCursor: Pair<Int, Int> = Pair(0, 0)
    override suspend fun getGames(teamId: Int): GamesDto {
        val games = networkApi.getGames(
            id = teamId,
            cursor = if (idAndCursor.isKeySame(teamId)) idAndCursor.second else INITIAL_CURSOR
        )
        idAndCursor = idAndCursor.copy(first = teamId, second = games.meta.nextCursor)
        return games
    }

    private var searchAndCursor: Pair<String, Int> = Pair(EMPTY, 0)
    override suspend fun getPlayers(search: String): PlayersDto {
        val players = networkApi.getPlayers(
            name = search,
            cursor = if (searchAndCursor.isKeySame(search)) searchAndCursor.second else INITIAL_CURSOR
        )
        searchAndCursor = searchAndCursor.copy(first = search, second = players.meta.nextCursor)
        return players
    }
}

private fun <A, B> Pair<A, B>.isKeySame(other: A): Boolean {
    return this.first == other
}
