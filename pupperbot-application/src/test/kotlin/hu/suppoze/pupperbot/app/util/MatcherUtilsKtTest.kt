package hu.suppoze.pupperbot.app.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class MatcherUtilsKtTest {

    data class PathParamReplacerData(
        val rawUrl: String,
        val params: List<String>,
        val expectedUrl: String
    )

    private fun pathParamReplacerDataProvider(): Stream<PathParamReplacerData> {
        return Stream.of(
            PathParamReplacerData(
                "https://www.test.com/{{0}}/index.html",
                listOf("kutyus"),
                "https://www.test.com/kutyus/index.html"
            ),
            PathParamReplacerData(
                "https://www.test.com/{{0}}/index.html",
                listOf("kutyus", "cicus"),
                "https://www.test.com/kutyus/index.html"
            ),
            PathParamReplacerData(
                "https://www.test.com/{{0}}/{{1}}/index.html",
                listOf("kutyus", "cicus"),
                "https://www.test.com/kutyus/cicus/index.html"
            ),
            PathParamReplacerData(
                "https://www.test.com/index.html",
                listOf("kutyus"),
                "https://www.test.com/index.html"
            )
        )
    }

    @ParameterizedTest
    @MethodSource("pathParamReplacerDataProvider")
    fun shouldReplacePathParams(data: PathParamReplacerData) {
        val replaced = data.rawUrl.withPathParams(*data.params.toTypedArray())
        assertEquals(data.expectedUrl, replaced)
    }

}