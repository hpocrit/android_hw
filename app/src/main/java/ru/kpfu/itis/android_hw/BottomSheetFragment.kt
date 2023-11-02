package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(var adapter: NewsAdapter) : BottomSheetDialogFragment() {
    private var btnAddNews: Button? = null
    private var etAddNewsCount: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_buttom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNews = view.findViewById(R.id.button)
        etAddNewsCount = view.findViewById(R.id.editText)

        btnAddNews?.setOnClickListener {
            val number = etAddNewsCount?.text.toString().toIntOrNull() ?: 0
            if (number in 1..5) {
                    NewsRepository.addRandomItems(number)
                adapter.setCnt(number)
                adapter.notifyDataSetChanged()

            }
            dismiss()
        }
    }
    companion object {
        private val BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "BOTTOM_SHEET_DIALOG_FRAGMENT_TAG"
    }
}
