package pl.hoorder.myapplication1.ui.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import pl.hoorder.myapplication1.DatabaseHelper
import pl.hoorder.myapplication1.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())

        val searchBtn: Button = binding.searchButton
        val searchInput: EditText = binding.searchInput

        searchBtn.setOnClickListener(){
            val searchText = searchInput.text.toString()
            displayFuneralList(searchText)
        }
        return binding.root
    }

    private fun displayFuneralList(locality: String) {
        val tableLayout: TableLayout = binding.tableLayout
        val records = databaseHelper.searchLocality(locality)

        for (record in records) {
            val row = TableRow(requireContext())
            val layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            row.layoutParams = layoutParams

            val columns = record.split(" - ")
            for (column in columns) {
                val textView = TextView(requireContext())
                textView.text = column
                textView.setTextColor(Color.BLACK)
                textView.setPadding(8, 8, 220, 8)
                textView.textSize = 16f
                row.addView(textView)
            }

            tableLayout.addView(row)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

