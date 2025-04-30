package dev.tavarus.boardgamelogger

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.tavarus.boardgamelogger.ui.AppNavHost
import dev.tavarus.boardgamelogger.ui.theme.BoardGameLoggerTheme

import org.junit.Test

import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class ExampleInstrumentedTest {
    @get:Rule(order = 1)
    val hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity
    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltTestRule.inject()
        composeTestRule.setContent {
            BoardGameLoggerTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                AppNavHost(navController)
            }

        }
    }

    @Test
    fun sampleTest() {
        composeTestRule.onNodeWithText("tav's BGG Collection").assertIsDisplayed()
    }

}