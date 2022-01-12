package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: CalculatorViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setupButtons()
    }

    private fun setupButtons() {
        setupClearButton()
        setupBackSpaceButton()
        setupDigitButtons()
        setupEqualsButton()
        setupDecimalButton()
        setupOperatorButtons()
    }

    private fun setupDecimalButton() {
        with(binding) {
            dot.setOnClickListener {
                viewModel.receiveDecimalSeparatorInput()
                input.text = viewModel.getCurrentCalculation()
            }
        }
    }

    private fun setupEqualsButton() {
        with(binding) {
            buttonEquals.setOnClickListener {
                result.text = viewModel.getCurrentCalculation()
            }
        }
    }


    private fun setupDigitButtons() {
        with(binding) {
            val numberButtons = listOf(
                buttonZero,
                buttonOne,
                buttonTwo,
                buttonThree,
                buttonFour,
                buttonFive,
                buttonSix,
                buttonSeven,
                buttonEight,
                buttonNine,
            )
            numberButtons.forEach { button ->
                button.setOnClickListener {
                    viewModel.receiveDigitInput(button.text.first())
                    input.text = viewModel.getCurrentCalculation()
                }
            }
        }
    }

    private fun setupBackSpaceButton() {
        with(binding) {
            backspace.setOnClickListener {
                viewModel.deleteLast()
                input.text = viewModel.getCurrentCalculation()
            }
        }
    }


    private fun setupClearButton() {
        with(binding) {
            allClear.setOnClickListener {
                viewModel.allClear()
                input.text = viewModel.getCurrentCalculation()
                result.text = viewModel.getCurrentCalculation()
            }
        }
    }

    private fun setupOperatorButtons() {
        with(binding) {
            val operatorButtons = listOf(
                buttonDivision,
                buttonMultiply,
                buttonSubtraction,
                buttonAddition,
            )
            operatorButtons.forEach { button ->
                button.setOnClickListener {
                    viewModel.receiveOperatorInput(button.text.first())
                    input.text = viewModel.getCurrentCalculation()
                }
            }
        }
    }
}