package ru.mipt.npm.liquidproto

interface VotingQuestion {
    val description: String
    val options: List<String>
    // checks if the list can be treated as a correct answer
    val checker: (List<Int>) -> Boolean
}