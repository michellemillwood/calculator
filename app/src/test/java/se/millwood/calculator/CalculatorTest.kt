package se.millwood.calculator

import org.junit.Test

import org.junit.Assert.*

class CalculatorTest {
    @Test
    fun additionAndMultiplication() {
        assertEquals(
            15.0,
            Calculator.calculateExpression(mutableListOf("5", "+", "5", "x", "2")),
            10.0
        )
    }

    @Test
    fun subtractionAndDivision() {
        assertEquals(
            15.0,
            Calculator.calculateExpression(mutableListOf("20", "-", "10", "/", "2")),
            5.0
        )
    }

    @Test
    fun subtractionAndDivisionWithParentheses() {
        assertEquals(
            5.0,
            Calculator.calculateExpression(mutableListOf("(", "20", "-", "10", ")", "/", "2")),
            15.0
        )
    }

    @Test
    fun nestledParentheses() {
        assertEquals(
            37.5,
            Calculator.calculateExpression(mutableListOf("5","(", "2", "(", "2", "+", "2", ")", "-", "0.5", ")")),
            32.5
        )
    }

    @Test
    fun inputParsing() {
        val viewModel = CalculatorViewModel()
        viewModel.parseDecimalSeparatorInput()
        viewModel.parseOperatorInput("+")
        viewModel.parseDigitInput("2")
        assertEquals("0.0 + 2", viewModel.getCurrentExpression())
    }
}