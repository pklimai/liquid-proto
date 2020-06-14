package ru.mipt.npm.liquidproto

import java.time.LocalDateTime

class Voting(
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    val question: VotingQuestion,
    val category: Category
) {
    // set to true if finished - either forcefully or due to endTime approached
    var finalized: Boolean = false

    // Final results must be saved here - because delegation is subject to change,
    // the final result is calculated at endTime and becomes fixed.

}