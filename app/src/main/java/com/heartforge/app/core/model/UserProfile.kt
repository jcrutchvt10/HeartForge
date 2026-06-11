package com.heartforge.app.core.model

import java.time.LocalDate

data class UserProfile(
    val nickname: String,
    val birthday: LocalDate?,
    val interests: List<String>,
    val favoriteFoods: List<String>,
    val hobbies: List<String>,
    val likes: List<String>,
    val dislikes: List<String>,
    val personalityTraits: List<String>,
    val preferredConversationStyle: String,
    val relationshipGoals: List<String>
)
