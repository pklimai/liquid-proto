package ru.mipt.npm.liquidproto

typealias QuestionDescription = String

typealias Options = String

//enum class QuestionType {
//    SingleChoice, MultipleChoice
//}

interface VoteQuestion {
    val description: QuestionDescription
    val results: MutableList<VoteResult>
    val options: List<Options>
}

class SingleChoiceVoteQuestion(override val description: String, override val options: List<Options>) : VoteQuestion {
    override val results = mutableListOf<VoteResult>()
}

class MultipleChoiceVoteQuestion(
    override val description: String,
    override val options: List<Options>,
    val numberToSelect: Int
) :
    VoteQuestion {
    init {
        if (numberToSelect <= 0 || numberToSelect >= options.size) {
            error("Wrong number of options for multiple-choice vote")
        }
    }

    override val results = mutableListOf<VoteResult>()
}

interface VoteResult {}

class SingleChoiceVoteResult(val selected: Int) : VoteResult

class MultipleChoiceVoteResult(val selected: List<Int>) : VoteResult

fun returnError(e: String) {
    println(e)
}

typealias UserID = String

fun countDelegates(voter: Voter): Int {
    var num = 1
    for (v in voter.votingFor) {
        num += countDelegates(v)
    }
    return num
}


class Voter(val id: UserID) {
    var delegate: Voter? = null
    var votingFor: MutableList<Voter> = mutableListOf()

    val alreadyVotedQuestions = mutableListOf<VoteQuestion>()

    fun vote(q: VoteQuestion) {
        if (q in alreadyVotedQuestions) {
            returnError("This question was already voted by $id")
            return
        } else {
            alreadyVotedQuestions.add(q)
        }

        if (delegate == null) {
            // voting itself
            println("VOTING USER: $id")
            println("QUESTION TO VOTE: ${q.description}")
            q.options.forEach {
                println(it.toString())
            }
            print("Enter number starting 0 > ")
            val res = readLine()!!.toInt()

            repeat(countDelegates(this)) {
                q.results.add(SingleChoiceVoteResult(selected = res))
            }
        }
    }

    fun delegateTo(delegate: Voter) {
        // TODO check for loops
        this.delegate = delegate
        // TODO check if already there
        delegate.votingFor.add(this)
    }

}

fun main() {

    val q1 = SingleChoiceVoteQuestion("Do you want a ternary operator in Kotlin?", listOf("Yes", "No"))

    val voter1 = Voter("user1")
    val voter2 = Voter("user2")
    val voter3 = Voter("user3")
    val voter4 = Voter("user4")
    val voter5 = Voter("user5")

    voter1.delegateTo(voter2)
    voter3.delegateTo(voter2)
    voter2.delegateTo(voter4)

    voter1.vote(q1)
    voter2.vote(q1)
    voter3.vote(q1)
    voter4.vote(q1)
    voter5.vote(q1)


    println(q1.results.map{
        (it as SingleChoiceVoteResult).selected }.joinToString()
    )

}



