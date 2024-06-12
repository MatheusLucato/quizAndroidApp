package com.app.quizandroidapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard ORDER BY score DESC LIMIT 10")
    suspend fun getTopLeaderboard(): List<Leaderboard>

    @Insert
    suspend fun insert(leaderboard: Leaderboard)
}