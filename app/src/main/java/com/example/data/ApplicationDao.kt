package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {

    @Query("SELECT * FROM application_records ORDER BY submissionTimestamp DESC")
    fun getAllApplications(): Flow<List<ApplicationEntity>>

    @Query("SELECT * FROM application_records WHERE tokenNumber = :token LIMIT 1")
    suspend fun getByToken(token: String): ApplicationEntity?

    @Query("SELECT * FROM application_records WHERE tokenNumber LIKE '%' || :query || '%' OR mobileNumber LIKE '%' || :query || '%' OR applicantName LIKE '%' || :query || '%' ORDER BY submissionTimestamp DESC")
    fun searchApplications(query: String): Flow<List<ApplicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application: ApplicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(applications: List<ApplicationEntity>)

    @Update
    suspend fun updateApplication(application: ApplicationEntity)

    @Delete
    suspend fun deleteApplication(application: ApplicationEntity)

    @Query("SELECT COUNT(*) FROM application_records")
    suspend fun getCount(): Int
}
