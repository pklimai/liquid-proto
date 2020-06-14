package ru.mipt.npm.liquidproto

import java.time.LocalDateTime

fun returnError(e: String) {
    println(e)
}

fun countDelegates(voter: Voter, category: Category): Int {
    var num = 1
    for (v in voter.votingFor[category] ?: listOf<Voter>()) {
        num += countDelegates(v, category)
    }
    return num
}

fun main() {

    // You have some users in your system (Slack etc.)
    val voters = (0..6).map { Voter("user$it") }

    // They do some voting rights delegation - generally can be done at any time
    voters[1].delegateTo(voters[2], Category.General)
    voters[3].delegateTo(voters[2], Category.General)
    voters[2].delegateTo(voters[4], Category.General)

    // 'Admin' creates question and a voting
    val q1 = SingleChoiceQuestion(
        "Do you want a ternary operator in Kotlin?",
        listOf("Yes", "No")
    )

    val v1 = Voting(
        LocalDateTime.of(2020, 6, 14, 14, 0, 0),
        LocalDateTime.of(2020, 6, 15, 14, 0, 0),
        q1, Category.General
    )

    // Voting is sent to users.
    // Could be based on grouping, invites, etc. - here we just send to all
    voters.forEach {
        it.addVoting(v1)
    }

    val answerYes = Answer<SingleChoiceQuestion>(listOf(0))
    val answerNo = Answer<SingleChoiceQuestion>(listOf(1))

    // voters[0] does nothing so expect no vote from him
    voters[1].vote(v1, answerYes) // This user has a delegate but votes herself
    voters[4].vote(v1, answerNo)
    voters[5].vote(v1, answerNo)
    voters[6].vote(v1, answerYes)

    println("Results: " +
        voters.map { it.getAnswer(v1)?.answeredOptions?.get(0) }.joinToString()
    )

    val yesCount = voters.count { it.getAnswer(v1) == answerYes }
    val noCount = voters.count { it.getAnswer(v1) == answerNo }
    println("Yes: $yesCount, No: $noCount")

}
