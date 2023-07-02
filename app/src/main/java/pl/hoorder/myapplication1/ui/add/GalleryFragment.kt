package pl.hoorder.myapplication1.ui.add

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import pl.hoorder.myapplication1.DatabaseHelper
import pl.hoorder.myapplication1.databinding.FragmentGalleryBinding
import java.text.SimpleDateFormat
import java.util.*

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        loadFeatures()
        return binding.root
    }

    private fun loadFeatures() {
        displayFuneralList()
        displayDate()
    }

    private fun displayDate() {
        fun getCurrentDate(): String {
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return dateFormat.format(currentDate)
        }

        val editText: EditText = binding.editText
        val dateSpinner: Spinner = binding.dateSpinner
        val addButton: Button = binding.addButton

        val dates = arrayOf(getCurrentDate())
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dateSpinner.adapter = adapter

        addButton.setOnClickListener {
            val locality = editText.text.toString()
            val selectedDate = dateSpinner.selectedItem as String

            addFuneral(locality, selectedDate)

            editText.text.clear()
            dateSpinner.setSelection(0)
            Toast.makeText(requireContext(), "Dodano wpis do bazy danych", Toast.LENGTH_SHORT).show()
            databaseHelper.show30DaysFuneral()
        }
    }

    private fun displayFuneralList() {
        val tableLayout: TableLayout = binding.tableLayout
        val records = databaseHelper.show30DaysFuneral()

        for (record in records) {
            val row = TableRow(requireContext())
            val layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
            )
            row.layoutParams = layoutParams

            val columns = record.split(" - ")
            for (i in columns.indices) {
                val textView = TextView(requireContext())
                textView.text = columns[i]
                textView.setTextColor(Color.BLACK)
                textView.setPadding(20, 8, if (i == 2) 8 else 120, 8)
                textView.textSize = 15f
                row.addView(textView)

                fun deleteFuneral(id: String) {
                    val db = databaseHelper.writableDatabase
                    val deleteQuery = "DELETE FROM funeral WHERE id_funeral = ?"
                    val whereArgs = arrayOf(id)
                    db.execSQL(deleteQuery, whereArgs)
                    db.close()
                    Toast.makeText(requireContext(), "Rekord został usunięty", Toast.LENGTH_SHORT).show()
                    loadFeatures()
                }

                if (i == 2) {
                    val deleteButton = Button(requireContext())
                    deleteButton.text = "Usuń"
                    deleteButton.setOnClickListener {
                        val id = columns[0]
                        deleteFuneral(id)
                        displayFuneralList()
                    }
                    row.addView(deleteButton)
                }
            }

            tableLayout.addView(row)
        }
    }

    private fun addFuneral(locality: String, date: String) {
        val funeralValues = ContentValues()
        funeralValues.put("locality", locality)
        funeralValues.put("date", date)
        funeralValues.put("amount", 75)
        databaseHelper.writableDatabase.insert("funeral", null, funeralValues)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

