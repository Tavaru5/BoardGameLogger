package dev.tavarus.boardgamelogger.domain

import dev.tavarus.boardgamelogger.data.apimodels.Player


data class Play(val scores: List<PlayerScore> = listOf())

data class PlayerScore(
    val player: Player,
    val score: Score,
)

sealed class Score(open val winner: Boolean, val tag: String) {
    data class IntScore(
        val points: Int?, override val winner: Boolean,
    ) : Score(winner, INT_SCORE_TAG) {
        override fun formatScore() = points?.toString() ?: ""
        override fun updateScore(newScore: String) =
            this.copy(points = newScore.toIntOrNull())

        override fun updateWinner(winner: Boolean) = this.copy(winner = winner)
    }

    open fun formatScore(): String? = null
    abstract fun updateWinner(winner: Boolean): Score
    abstract fun updateScore(newScore: String): Score
    fun toDBString() = listOf(tag, formatScore(), winner.toString()).joinToString(",")

    companion object {
        fun fromDBString(dbString: String?): Score? {
            val split = dbString?.split(",")
            return when (split?.first()) {
                INT_SCORE_TAG -> {
                    IntScore(split[1].toIntOrNull(), split[2].toBoolean())
                }
                else -> null
            }
        }
        const val INT_SCORE_TAG = "INT"
    }
}

