package com.example.emoji.model

/**
 * @author y.gladkikh
 */
data class Reaction(
    val userId: Int,
    val emoji: String,
    val emojiName: String,
    var clicked : Boolean,
)
val reactionsMap: Map<String, String> = mapOf(
    "1f389" to "tada",
    "1f600" to "grinning",
    "1f3a7" to "headphones",
    "1f440" to "eyes",
    "1f610" to "neutral",
    "1f419" to "octopus",
    "1f603" to "smiley",
    "1f5e1" to "dagger",
    "1f612" to "unamused",
    "1f436" to "puppy",
    "1f917" to "hug",
    "1f62a" to "sleepy",
    "1f408" to "cat",
    "1f920" to "cowboy",
    "1f606" to "laughing",
    "1f602" to "joy",
    "1f379" to "tropical_drink",
    "1f47a" to "goblin",
    "1f197" to "squared_ok",

    )
