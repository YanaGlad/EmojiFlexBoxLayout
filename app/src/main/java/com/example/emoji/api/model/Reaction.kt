package com.example.emoji.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GetReactionsResponse(
    @SerialName("name_to_codepoint")
    val reactionsObject: JsonObject,
)

@Serializable
data class
Reaction(
    @SerialName("emoji_code")
    val code: String,

    @SerialName("emoji_name")
    val name: String,

    @SerialName("user_id")
    val userId: Int,

    @SerialName("reaction_type")
    val type: String,

    @SerialName("emoji")
    val emoji: String = "",
)

fun GetReactionsResponse.mapToReactions(): List<Reaction> {
    val reactions = mutableListOf<Reaction>()
    val emojis = mutableSetOf<String>()
    reactionsObject.keys.forEach { key ->
        val code = reactionsObject[key].toString()
        val emoji = jsonToEmoji(code)
        if (emoji.isNotEmpty() && !emojis.contains(emoji)) {
            reactions.add(
                Reaction(
                    userId = -1,
                    name = key,
                    code = code,
                    type = "",
                    emoji = emoji
                )
            )
            emojis.add(emoji)
        }
    }
    return reactions
}

fun Reaction.mapToReaction(): Reaction {
    return Reaction(
        userId = userId,
        name = name,
        code = code,
        type = type,
        emoji = codeToEmoji(code)
    )
}

@Throws(NumberFormatException::class)
fun simpleToEmoji(code: String): String {
    return String(Character.toChars(Integer.parseInt(code, 16)))
}

fun combinedToEmoji(code: String): String {
    val codes = code.split("-")
    return simpleToEmoji(codes[0]) + simpleToEmoji(codes[1])
}

fun utfToEmoji(code: String): String {
    return if (code.contains("-")) {
        combinedToEmoji(code)
    } else {
        simpleToEmoji(code)
    }
}

fun codeToEmoji(code: String): String {
    return try {
        utfToEmoji(code)
    } catch (e: NumberFormatException) {
        UNKNOWN_EMOJI
    }
}

fun jsonToEmoji(code: String): String {
    return try {
        codeToEmoji(code.replace("\"", ""))
    } catch (e: NumberFormatException) {
        ""
    }
}

private const val UNKNOWN_EMOJI = "⬜"
