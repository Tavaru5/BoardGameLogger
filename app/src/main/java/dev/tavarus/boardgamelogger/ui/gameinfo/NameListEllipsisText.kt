package dev.tavarus.boardgamelogger.ui.gameinfo

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Constraints

private const val ellipsisCharacter = "\u2026"
private const val minNameLength = 4

@Composable
fun NameListEllipsisText(
    names: List<String>,
    modifier: Modifier = Modifier,
) {
    // This will need to change to accommodate longer winner names
    val offsetList = mutableListOf<Pair<String, Int>>()
    var endEllipses = false
    val text = names.foldIndexed("") { index, folded, name ->
        offsetList.add(Pair(name, folded.length))
        folded + name.plus(if ((index == names.size - 1) && !endEllipses) "" else ", ")
    }
    val layoutText = remember(text) { "$text$ellipsisCharacter" }
    val textLayoutResultState = remember(layoutText) {
        mutableStateOf<TextLayoutResult?>(null)
    }
    SubcomposeLayout(modifier) { constraints ->
        // result is ignored - we only need to fill our textLayoutResult
        subcompose("measure") {
            Text(
                text = layoutText,
                maxLines = 1,
                onTextLayout = { textLayoutResultState.value = it },
            )
        }.first().measure(Constraints())
        val textLayoutResult = textLayoutResultState.value
            ?: // shouldn't happen - onTextLayout is called before subcompose finishes
            return@SubcomposeLayout layout(0, 0) {}
        val placeable = subcompose("visible") {
            val finalText = remember(text, textLayoutResult, constraints.maxWidth) {
                if (text.isEmpty() || textLayoutResult.getBoundingBox(text.indices.last).right <= constraints.maxWidth) {
                    return@remember text
                }

                val ellipsisWidth = layoutText.indices.toList()
                    .last()
                    .let widthLet@{ index ->
                        val width = textLayoutResult.getBoundingBox(index).width
                        if (width > 0) {
                            return@widthLet width
                        }
                        // this should not happen, because
                        // this error occurs only for the last character in the string
                        throw IllegalStateException("all ellipsis chars have invalid width")
                    }

                val counters = offsetList.mapIndexed { index, (score, offset) ->
                    ScoreWidthCounter(
                        textLayoutResult,
                        offset,
                        ellipsisWidth,
                        score,
                        (index != offsetList.size - 1) || endEllipses
                    )
                }.toMutableList()
                val availableWidth = constraints.maxWidth
                var totalWidth = counters.fold(0f) { acc, i -> acc + i.totalWidth() }
                while (totalWidth > availableWidth) {
                    val widthToRemove = counters.maxBy { it.totalWidth() }.cutNextChar()
                    // We can't remove an individual character, so instead we remove the last name
                    if (widthToRemove == null) {
                        if (!endEllipses) {
                            endEllipses = true
                            totalWidth += ellipsisWidth
                        }
                        val toRemove = counters.removeAt(counters.lastIndex)
                        totalWidth -= toRemove.totalWidth()
                    } else {
                        totalWidth -= widthToRemove
                    }
                }

                counters.fold("") { acc, counter -> acc + counter.getResultString() }
                    .plus(if (endEllipses) ellipsisCharacter else "")
            }
            Text(text = finalText)
        }[0].measure(constraints)
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}

private class ScoreWidthCounter(
    private val textLayoutResult: TextLayoutResult,
    private val initialOffset: Int,
    private val ellipsisWidth: Float,
    playerScore: String,
    withComma: Boolean,
) {
    var name: String = playerScore
        private set
    var stringTrail = if (withComma) ", " else ""
    var widths = (name + stringTrail).indices.map { i ->
        textLayoutResult.getBoundingBox(initialOffset + i).width
    }.toMutableList()
    var hasEllipsis = false


    fun totalWidth() = widths.sum() + if (hasEllipsis) ellipsisWidth else 0f

    fun getResultString(): String {
        return name.plus(if (hasEllipsis) ellipsisCharacter else "").plus(stringTrail)
    }

    // Returns the width of the next char that is cut, or null if it can't be cut
    fun cutNextChar(): Float? {
        if (name.length <= minNameLength) {
            return null
        }
        name = name.dropLast(1)
        // widths contains the comma as well, so we have to specify the index
        var lostWidth = widths[name.length]
        widths.removeAt(name.length)
        if (!hasEllipsis) {
            hasEllipsis = true
            lostWidth -= ellipsisWidth
        }
        return lostWidth
    }
}