package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "application_records")
data class ApplicationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val tokenNumber: String,
    val applicantName: String,
    val mobileNumber: String,
    val janAadhaarNumber: String = "",
    val serviceId: String,
    val serviceTitle: String,
    val status: String, // String representation of ApplicationStatus
    val submissionTimestamp: Long = System.currentTimeMillis(),
    val estimatedDate: String = "",
    val remarks: String = "",
    val village: String = "Sigdola Bara, Sikar 332312",
    val documentNames: String = "",
    val documentUris: String = ""
)
