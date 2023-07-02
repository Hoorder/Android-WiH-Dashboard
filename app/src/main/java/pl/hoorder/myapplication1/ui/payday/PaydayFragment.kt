package pl.hoorder.myapplication1.ui.payday

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import pl.hoorder.myapplication1.DatabaseHelper
import pl.hoorder.myapplication1.databinding.FragmentPaydayBinding

class PaydayFragment : Fragment() {

    private var _binding: FragmentPaydayBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaydayBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        loadFeatures()

        val paydayBtn: Button = binding.addButton
        paydayBtn.setOnClickListener {
            val db = databaseHelper.writableDatabase
            val contentValues = ContentValues()
            contentValues.put("amount", 0)
            val whereClause = "amount >= 75"
            db.update("funeral", contentValues, whereClause, null)
            db.close()
            Toast.makeText(requireContext(), "Wypłacono", Toast.LENGTH_SHORT).show()
            loadFeatures()
        }


        return binding.root
    }

    private fun loadFeatures() {
        displayFuneralList()
        displayAmount()
        displayCountFuneral()
    }

    private fun displayCountFuneral(){
        val selectQuery2 = "SELECT COUNT(*) AS count FROM funeral WHERE amount >= 75"
        val cursor2 = databaseHelper.readableDatabase.rawQuery(selectQuery2, null)

        if (cursor2 != null && cursor2.moveToFirst()) {
            val count = cursor2.getInt(0)

            val userNameTextView: TextView = binding.sectionName
            userNameTextView.text = "Pogrzebów razem: $count szt."

        }
        cursor2.close()
    }

    private fun displayAmount() {
        val selectQuery2 = "SELECT SUM(amount) FROM funeral WHERE amount >= 75"
        val cursor2 = databaseHelper.readableDatabase.rawQuery(selectQuery2, null)

        if (cursor2 != null && cursor2.moveToFirst()) {
            val sumAmount = cursor2.getInt(0)

            val userNameTextView: TextView = binding.toPayment
            userNameTextView.text = "Do wypłacenia: $sumAmount,00 PLN"

        }
        cursor2.close()
    }

    private fun displayFuneralList() {
        val tableLayout: TableLayout = binding.tableLayout
        val records = databaseHelper.funeralsToPay()

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
                textView.setPadding(8, 8, 100, 8)
                textView.textSize = 15f
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

