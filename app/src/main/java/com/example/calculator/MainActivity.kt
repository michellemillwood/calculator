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
        setupNumberButtons()
        setupEqualsButton()
        setupDotButton()
    }

    private fun setupDotButton() {
        with(binding) {
            dot.setOnClickListener {
                viewModel.addDot()
                inputNumbers.text = viewModel.getCurrentCalculation()
            }
        }
    }

    private fun setupEqualsButton() {
        with(binding) {
            equals.setOnClickListener {
                result.text = viewModel.getCurrentCalculation()
            }
        }
    }


    private fun setupNumberButtons() {
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
                    viewModel.receiveInput(button.text.toString())
                    binding.inputNumbers.text = viewModel.getCurrentCalculation()
                }
            }
        }
    }

    private fun setupBackSpaceButton() {
        with(binding) {
            backspace.setOnClickListener {
                inputNumbers.text = viewModel.deleteLastNumber()
            }
        }
    }


    private fun setupClearButton() {
        with(binding) {
            allClear.setOnClickListener {
                viewModel.allClear()
                inputNumbers.text = viewModel.getCurrentCalculation()
                result.text = viewModel.getCurrentCalculation()
            }
        }
    }
}