package com.example.wallnut.model

/**
 * Data class representing an item for onboarding screens.
 *
 * @property imageResId The resource ID of the image associated with the item.
 * @property title The title or heading for the item.
 * @property description The description or content of the item.
 */
data class OnboardingItem(
    val imageResId: Int,
    val title: String,
    val description: String
)
