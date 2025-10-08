package com.hyeonuproject.studylogger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * 데이터베이스에 접근하여 데이터를 처리하는 함수들을 정의하는 인터페이스.
 * Room이 이 인터페이스를 보고 실제 실행 코드를 생성합니다.
 */
@Dao
interface StudyDao {

    // 새로운 공부 기록을 데이터베이스에 추가합니다.
    @Insert
    suspend fun insertStudyRecord(record: StudyRecord)

    // 새로운 카테고리를 데이터베이스에 추가합니다.
    @Insert
    suspend fun insertCategory(category: Category)

    // 모든 카테고리 목록을 가져옵니다. Flow를 사용하면 데이터 변경 시 자동으로 UI가 업데이트됩니다.
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    // 특정 날짜(startTime과 endTime 사이)에 해당하는 모든 공부 기록을 가져옵니다.
    @Query("SELECT * FROM study_records WHERE startTime >= :dayStart AND startTime < :dayEnd ORDER BY startTime DESC")
    fun getRecordsByDay(dayStart: Long, dayEnd: Long): Flow<List<StudyRecord>>
}

@Query("SELECT * FROM study_records ORDER BY startTime DESC")
fun getAllRecords(): Flow<List<StudyRecord>>