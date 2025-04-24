package dev.tavarus.boardgamelogger.domain


typealias Play = List<PlayerScore>

data class PlayerScore(
    val player: Player,
    val score: Score
)

sealed class Score(open val notes: String?, open val winner: Boolean) {
    data class IntScore(
        val points: Int?, override val winner: Boolean, override val notes: String? = null,
    ) : Score(notes, winner) {
        override fun formatScore() = points?.toString() ?: ""
        override fun updateScore(newScore: String) =
            this.copy(points = newScore.toIntOrNull())
        override fun updateWinner(winner: Boolean) = this.copy(winner = winner)
    }

    open fun formatScore(): String? = null
    abstract fun updateWinner(winner: Boolean) : Score
    abstract fun updateScore(newScore: String) : Score
}

