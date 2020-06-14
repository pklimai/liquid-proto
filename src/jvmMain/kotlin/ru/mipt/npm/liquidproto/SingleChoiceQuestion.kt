package ru.mipt.npm.liquidproto

class SingleChoiceQuestion(
    override val description: String,
    override val options: List<String>,
    override val checker: (List<Int>) -> Boolean = { it: List<Int> -> it.size == 1 }
) : VotingQuestion
