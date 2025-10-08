package com.hyeonuproject.studylogger.data // 파일이 data 패키지 안에 있음을 명시

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 목표 카테고리를 저장하기 위한 데이터 클래스.
 * 'categories'라는 이름의 데이터베이스 테이블로 사용됩니다.
 */
@Entity(tableName = "categories")
data class Category(
    // 카테고리 이름은 고유해야 하므로 기본 키(PrimaryKey)로 사용합니다.
    @PrimaryKey
    val name: String
)