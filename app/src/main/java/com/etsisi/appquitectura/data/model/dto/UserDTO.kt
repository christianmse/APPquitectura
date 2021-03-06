package com.etsisi.appquitectura.data.model.dto

import com.etsisi.appquitectura.data.model.enums.UserGender
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.presentation.utils.EMPTY

data class UserDTO (
        val email: String = String.EMPTY,
        val id: String = String.EMPTY,
        val name: String = String.EMPTY,
        val subject: String = QuestionSubject.UNKNOWN.value,
        val gender: String = UserGender.UNKNOWN.value,
        val surname: String = String.EMPTY,
        val academicRecord: String = String.EMPTY,
        val academicGroup: String = String.EMPTY,
        val city: String = String.EMPTY,
        val gameExperience: Long = 0,
        val totalQuestionsAnswered: Int = 0,
        val totalCorrectQuestionsAnswered: Int = 0
): FirestoreDTO() {
        fun toDomain() = UserBO(
                email = email,
                id = id,
                name = name,
                course = QuestionSubject.parseSubject(subject),
                gender = UserGender.parse(gender),
                surname = surname,
                city = city,
                academicRecord = academicRecord,
                academicGroup = academicGroup,
                gameExperience = gameExperience,
                totalQuestionsAnswered = totalQuestionsAnswered,
                totalCorrectQuestionsAnswered = totalCorrectQuestionsAnswered
        )
}