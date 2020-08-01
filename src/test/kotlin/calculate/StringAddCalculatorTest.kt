package calculate

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource

class StringAddCalculatorTest {
    @DisplayName(value = "split 다수 delimiters 확인 테스트")
    @Test
    fun stringToIntList() {
        val input = "1,2,3;4"
        val numbers = input.split(",", ";")
        assertThat(numbers).isEqualTo(listOf("1", "2", "3", "4"))
    }

    private lateinit var calculator: StringAddCalculator

    @BeforeEach
    fun beforeTest() {
        calculator = StringAddCalculator()
    }

    @DisplayName(value = "빈 문자열 또는 null 값을 입력할 경우 0을 반환해야 한다.")
    @ParameterizedTest
    @NullAndEmptySource
    fun emptyOrNull(text: String?) {
        assertThat(calculator.add(text)).isSameAs(0)
    }

    @DisplayName(value = "숫자 하나를 문자열로 입력할 경우 해당 숫자를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = ["1"])
    fun oneNumber(text: String) {
        assertThat(calculator.add(text)).isSameAs(Integer.parseInt(text))
    }

    @DisplayName(value = "숫자 두개를 쉼표(,) 구분자로 입력할 경우 두 숫자의 합을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = ["1,2"])
    fun twoNumbers(text: String) {
        assertThat(calculator.add(text)).isSameAs(3)
    }

    @DisplayName(value = "구분자를 쉼표(,) 이외에 콜론(:)을 사용할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = ["1,2:3"])
    fun colons(text: String) {
        assertThat(calculator.add(text)).isSameAs(6)
    }

    @DisplayName(value = "//와 \n 문자 사이에 커스텀 구분자를 지정할 수 있다.")
    @ParameterizedTest
    @ValueSource(strings = ["//;\n1;2;3"])
    fun customDelimiter(text: String) {
        assertThat(calculator.add(text)).isSameAs(6)
    }

    @DisplayName(value = "문자열 계산기에 숫자 이외의 값 또는 음수를 전달하는 경우 RuntimeException 예외 처리를 한다.")
    @Test
    fun negativeOrNotNumberString() {
        assertThatThrownBy { calculator.add("3,5:-1:9") }.isInstanceOf(RuntimeException::class.java)
        assertThatThrownBy { calculator.add("3,5:k:9") }.isInstanceOf(RuntimeException::class.java)
    }
}
