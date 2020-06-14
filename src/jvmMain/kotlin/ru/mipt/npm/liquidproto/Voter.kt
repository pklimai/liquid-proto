package ru.mipt.npm.liquidproto

typealias UserID = String

class Voter(val id: UserID) {
    val delegates: MutableMap<Category, Voter?> = mutableMapOf()
    val votingFor: MutableMap<Category, MutableList<Voter>> = mutableMapOf()

    val votings = mutableMapOf<Voting, Answer<VotingQuestion>?>()

    fun addVoting(v: Voting) {
        votings[v] = null
    }

    /**
     * Overrides delegation, if it is present
     */
    fun vote(v: Voting, a: Answer<VotingQuestion>) {
        if (v !in votings) {
            returnError("This question is not in votings")
            return
        } else {
            votings[v] = a
        }
    }

    /**
     * Get answer, with recursive search by delegates if needed
     */
    fun getAnswer(v: Voting): Answer<VotingQuestion>? {
        // did not participate in this Voting
        if (v !in votings) {
            return null
        }
        // has its own answer
        if (votings[v] != null) {
            return votings[v]
        }
        // there is a delegate for the category
        if (delegates[v.category] != null) {
            return delegates[v.category]!!.getAnswer(v)
        }
        // no delegate, no own answer
        return null
    }

    /**
     * TODO check for loops
     */
    fun delegateTo(delegate: Voter, category: Category) {
        delegates[category] = delegate
        // update back-refs (are they needed?)
        if (delegate.votingFor[category] == null) {
            delegate.votingFor[category] = mutableListOf()
        }
        if (this !in delegate.votingFor[category]!!) {
            delegate.votingFor[category]!!.add(this)
        }
    }

}
