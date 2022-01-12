package com.example.calculator

import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val calculation = mutableListOf<String>()

    fun receiveInput(input: String) {
        calculation.add(input)
    }

    fun getCurrentCalculation() = calculation.joinToString("")

    fun allClear() = calculation.clear()

    fun deleteLastNumber(): String {
        if (calculation.isNotEmpty()) {
            calculation.removeLast()
        }
        return getCurrentCalculation()
    }

    fun addDot() {
        if ("." !in calculation) {
            calculation.add(".")
        }
    }
}