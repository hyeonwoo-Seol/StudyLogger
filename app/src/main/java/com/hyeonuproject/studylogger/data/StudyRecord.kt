package com.hyeonuproject.studylogger.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 개별 공부 기록을 저장하기 위한 데이터 클래스.
 * 'study_records'라는 이름의 데이터베이스 테이블로 사용됩니다.
 */
@Entity(tableName = "study_records")
data class StudyRecord(
    // 각 기록을 고유하게 식별하기 위한 기본 키(PrimaryKey)입니다. 자동으로 증가합니다.
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val category: String,
    val startTime: Long,
    val endTime: Long,
    val duration: Long, // 초 단위
    val memo: String
)