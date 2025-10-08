package com.hyeonuproject.studylogger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * 데이터베이스 본체.
 * 앱의 모든 Entity(테이블)와 DAO를 포함하고 관리합니다.
 */
@Database(entities = [StudyRecord::class, Category::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 이 데이터베이스가 사용하는 DAO를 명시합니다.
    abstract fun studyDao(): StudyDao

    companion object {
        // @Volatile: 이 변수에 대한 변경 사항이 모든 스레드에 즉시 보이도록 보장합니다.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // 여러 스레드가 동시에 접근하는 것을 방지합니다.
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "study_logger_database" // 데이터베이스 파일 이름
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}