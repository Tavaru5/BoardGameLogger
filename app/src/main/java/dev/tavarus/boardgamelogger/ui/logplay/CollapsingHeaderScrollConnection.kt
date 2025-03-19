package dev.tavarus.boardgamelogger.ui.logplay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

typealias AnimStateMap = Map<String, ScrollAnimState>

data class ScrollAnimState(
    val startDp: Dp,
    val endDp: Dp,
    val currentValue: Dp,
) {
    fun calculateOpacity(donePercentage: Float) =
        (endDp - (currentValue / donePercentage) - startDp) / (endDp - startDp)
}


class CollapsingHeaderScrollConnection(
    constraints: Map<String, Pair<Dp, Dp>>,
    val currentDensity: Density
): NestedScrollConnection {

    var animStates: AnimStateMap by
        mutableStateOf(
            constraints.mapValues { (_, constraint) ->
                ScrollAnimState(constraint.first, constraint.second, constraint.first)
            }
        )

    fun getCurrentValue(key: String) = animStates[key]?.currentValue
    fun getOpacity(key: String, donePercentage: Float) = animStates[key]?.calculateOpacity(donePercentage)
    fun updateConstraint(replaceKey: String, startDp: Dp? = null, endDp: Dp? = null)  {
        animStates = animStates.mapValues { (key, scrollAnimState) ->
            if (key == replaceKey) {
                scrollAnimState.copy(
                    startDp = startDp ?: scrollAnimState.startDp,
                    endDp = endDp ?: scrollAnimState.endDp
                )
            } else scrollAnimState
        }
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = with(currentDensity) { available.y.toDp() }
        var currentConsumed = 0.dp
        var i = 0
        val animList = animStates.toList()
        val newValues = mutableMapOf<String,Dp>()


        while ((currentConsumed != delta) && i < animList.size) {
            val availableDelta = delta - currentConsumed
            // For some reason it is rounding the previousval + availabledelta, but then it doesn't round delta/available delta
            val anim = animList[i].second
            val previousVal = anim.currentValue
            val newVal =
                (previousVal + availableDelta).coerceIn(anim.endDp, anim.startDp)

            newValues[animList[i].first] = newVal

            currentConsumed += (newVal - previousVal)
            i++
        }
        animStates = animStates.mapValues { (key, animState) ->
            animState.copy(currentValue = newValues[key] ?: animState.currentValue)
        }

        // Return the consumed scroll amount
        return Offset(0f, with(currentDensity) { currentConsumed.toPx() })
    }
}
