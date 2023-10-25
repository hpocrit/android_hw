package ru.kpfu.itis.android_hw

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


class PhoneMaskTextWatcher(private val editText: EditText) : TextWatcher {

    private var previousText = ""
    private var currentText = ""

    private val mask = "+7 (9__)-___-__-__"

    private var isUpdating = false
    private val placeholder = '_'




    init {
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        if (isUpdating) {
            return
        }

        currentText = editable.toString()

        // Удаляем все символы, не являющиеся цифрами
        val digits : String
        if(currentText.length >= 5) {
            digits = currentText.substring(5).replace("[^0-9]".toRegex(), "")
        } else {
            digits = currentText.replace("[^0-9]".toRegex(), "")
        }

        if (digits.length > mask.length) {
            // Обрезаем введенные цифры, если их количество больше, чем в маске
            val trimmedDigits = digits.substring(mask.length)
            updateText(trimmedDigits)
        } else {
            updateText(digits)
        }
    }

    private fun updateText(digits: String) {
        isUpdating = true

        var format = ""

        var maskIndex = 0
        var digitsIndex = 0

        while (maskIndex < mask.length && digitsIndex < digits.length) {
            val maskChar = mask[maskIndex]

            if (maskChar == placeholder) {
                if(digitsIndex < digits.length) {
                    val digit = digits[digitsIndex]
                    format += digit.toString()
                    digitsIndex++
                } else {
                    break
                }
            } else {
                format += maskChar.toString()
            }

            maskIndex++
        }

        previousText = format
        editText.removeTextChangedListener(this)
        editText.setText(previousText)
        editText.setSelection(previousText.length)
        editText.addTextChangedListener(this)

        isUpdating = false
    }
}