package pl.hoorder.myapplication1.ui.slideshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import pl.hoorder.myapplication1.DatabaseHelper
import pl.hoorder.myapplication1.databinding.FragmentSlideshowBinding
import java.util.*

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        loadFeatures()
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR).toString()
        val curretYear: TextView = binding.curretYear
        curretYear.text = "Rok: $year"
        return binding.root
    }

    private fun loadFeatures() {
        displayFuneralList()
    }

    private fun displayFuneralList() {
        val tableLayout: TableLayout = binding.tableLayout
        val records = databaseHelper.displayStatistic()

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
                textView.setPadding(8, 8, 250, 8)
                textView.textSize = 17f
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