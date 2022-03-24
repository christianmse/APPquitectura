package com.etsisi.appquitectura.domain.model

import com.etsisi.appquitectura.data.model.entities.QuestionEntity

data class QuestionBO(
    val id: String?,
    val title: String?,
    val level: QuestionLevel,
    val age: QuestionAge,
    val topic: QuestionTopic
) {
    fun toEntity() = QuestionEntity(
        id = id.orEmpty(),
        title = title.orEmpty(),
        level = level.value,
        age = age.value,
        topic = topic.value
    )
}

enum class QuestionLevel(val value: Int) {
    UNKNOWN(0),
    EASY(1),
    NORMAL(2),
    DIFFICULT(3);

    companion object {
        fun parseLevel(x: Int?): QuestionLevel {
            return when(x) {
                EASY.value -> EASY
                NORMAL.value-> NORMAL
                DIFFICULT.value -> DIFFICULT
                else -> UNKNOWN
            }
        }
    }
}

enum class QuestionAge(val value: Int) {
    UNKNOWN(0),
    OLD(1),
    MIDDLE_AGE(2),
    MODERN_AGE(3),
    CONTEMPORARY_AGE(4);

    companion object {
        fun parseAge(x: Int?): QuestionAge {
            return when(x) {
                OLD.value -> OLD
                MIDDLE_AGE.value -> MIDDLE_AGE
                MODERN_AGE.value -> MODERN_AGE
                CONTEMPORARY_AGE.value -> CONTEMPORARY_AGE
                else -> UNKNOWN
            }
        }
    }
}

enum class QuestionTopic(val value: Int) {
    UNKNOWN(0),
    EGYPT(1),
    GREECE(2),
    ROME(3),
    PALEOCHRISTIAN(4),
    BYZANTINE(5),
    PREROMANESQUE(6),
    ROMANESQUE(7),
    GOTHIC(8),
    RENAISSANCE(9),
    MANNERISM(10),
    BAROQUE(11),
    ILLUSTRATION(12),
    ENGINEERING(13),
    HISTORICISM(14),
    ART_NOUVEAU(15),
    VANGUARD(16),
    MODERN(17),
    POSTMODERNITY(18),
    ACTUAL(19);

    companion object {
        fun parseTopic(x: Int?): QuestionTopic {
            return values().find { it.value == x } ?: UNKNOWN
        }
    }
}