package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ApplicationRepository(private val dao: ApplicationDao) {

    val allApplications: Flow<List<ApplicationEntity>> = dao.getAllApplications()

    fun searchApplications(query: String): Flow<List<ApplicationEntity>> {
        return dao.searchApplications(query.trim())
    }

    suspend fun getByToken(token: String): ApplicationEntity? = withContext(Dispatchers.IO) {
        dao.getByToken(token.trim())
    }

    suspend fun insertApplication(application: ApplicationEntity): Long = withContext(Dispatchers.IO) {
        dao.insertApplication(application)
    }

    suspend fun ensureInitialData() = withContext(Dispatchers.IO) {
        if (dao.getCount() == 0) {
            dao.insertAll(AppDatabase.getStarterApplications())
        }
    }
}
