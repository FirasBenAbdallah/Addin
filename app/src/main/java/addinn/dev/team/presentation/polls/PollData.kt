package addinn.dev.team.presentation.polls

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PollData(
    val id: MutableState<String>,
    val question: MutableState<String>,
    val choices: MutableList<MutableState<String>>,
    val choiceVotes: MutableList<MutableState<String>>,
    var selectedOptionIndex: MutableState<Int?> = mutableStateOf(null),
    var selectionCounts: MutableList<MutableState<Int>> = MutableList(choices.size) {
        mutableStateOf(
            0
        )
    },
    var totalSelectionCount: MutableState<Int> = mutableStateOf(0)
)
