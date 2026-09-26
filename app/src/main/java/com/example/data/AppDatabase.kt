package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ApplicationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun applicationDao(): ApplicationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deepak_center_emitra.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed realistic starter applications
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.applicationDao()?.insertAll(getStarterApplications())
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }

        fun getStarterApplications(): List<ApplicationEntity> {
            val now = System.currentTimeMillis()
            val dayMs = 24 * 60 * 60 * 1000L
            return listOf(
                ApplicationEntity(
                    tokenNumber = "DC-2026-4819",
                    applicantName = "Rajesh Kumar",
                    mobileNumber = "9829123456",
                    janAadhaarNumber = "2148765432",
                    serviceId = "caste_obc",
                    serviceTitle = "जाति प्रमाण पत्र (OBC / अन्य पिछड़ा वर्ग)",
                    status = "READY_FOR_PRINT",
                    submissionTimestamp = now - (5 * dayMs),
                    estimatedDate = "आज उपलब्ध है (Ready)",
                    remarks = "तहसीलदार महोदय द्वारा डिजिटल हस्ताक्षर पूर्ण। दीपक सेंटर से मूल प्रति प्राप्त करें।",
                    village = "Sigdola Bara, Sikar 332312",
                    documentNames = "Aadhaar Card, Jan Aadhaar, Land Record (Jamabandi), Income Affidavit"
                ),
                ApplicationEntity(
                    tokenNumber = "DC-2026-5921",
                    applicantName = "Suman Devi",
                    mobileNumber = "9414567890",
                    janAadhaarNumber = "3198765412",
                    serviceId = "pension_annual_kyc",
                    serviceTitle = "सामाजिक सुरक्षा पेंशन (वार्षिक भौतिक सत्यापन)",
                    status = "APPROVED",
                    submissionTimestamp = now - (3 * dayMs),
                    estimatedDate = "सत्यापन सफल (Verified)",
                    remarks = "बायोमेट्रिक ई-केवाईसी द्वारा वार्षिक सत्यापन सफल रहा। आगामी वर्ष तक पेंशन जारी रहेगी।",
                    village = "Sigdola Bara, Sikar 332312",
                    documentNames = "Jan Aadhaar Card, PPO Number, Aadhaar Fingerprint"
                ),
                ApplicationEntity(
                    tokenNumber = "DC-2026-6104",
                    applicantName = "Amit Sharma",
                    mobileNumber = "9785234567",
                    janAadhaarNumber = "7891234560",
                    serviceId = "jan_aadhaar_update",
                    serviceTitle = "नया जन आधार / सदस्य नाम जोड़ना (Jan Aadhaar Addition)",
                    status = "AT_TEHSILDAR_OFFICE",
                    submissionTimestamp = now - (2 * dayMs),
                    estimatedDate = "28 सितंबर 2026",
                    remarks = "प्रथम स्तर सांख्यिकी अधिकारी / विकास अधिकारी कार्यालय में दस्तावेज जांच अधीन है।",
                    village = "Sigdola Bara, Sikar 332312",
                    documentNames = "Jan Aadhaar, Birth Certificate, Aadhaar Card"
                ),
                ApplicationEntity(
                    tokenNumber = "DC-2026-7230",
                    applicantName = "Pooja Meena",
                    mobileNumber = "9928345678",
                    janAadhaarNumber = "4561237890",
                    serviceId = "scholarship_postmatric",
                    serviceTitle = "उत्तर मैट्रिक छात्रवृत्ति (Uttar Matric Scholarship)",
                    status = "SUBMITTED_TO_PORTAL",
                    submissionTimestamp = now - (1 * dayMs),
                    estimatedDate = "05 अक्टूबर 2026",
                    remarks = "संस्थान व कॉलेज स्तर पर फॉर्म प्रेषित। कॉलेज सत्यापन के बाद स्वीकृति जारी होगी।",
                    village = "Sigdola Bara, Sikar 332312",
                    documentNames = "10th Marksheet, 12th Marksheet, Fee Receipt, Jan Aadhaar, Income Certificate"
                )
            )
        }
    }
}
