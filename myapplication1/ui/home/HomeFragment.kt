package pl.hoorder.myapplication1.ui.home

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import pl.hoorder.myapplication1.DatabaseHelper
import pl.hoorder.myapplication1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        loadFeatures()

        return binding.root
    }

    private fun loadFeatures() {
        displayUserName()
        displayAmount()
        displayFuneralsThisYear()
        displayFuneralsThisMonth()
        displayFuneralsToday()
        displayFuneralsLastYear()
    }

    private fun displayAmount() {
        val selectQuery2 = "SELECT SUM(amount) FROM funeral"
        val cursor2 = databaseHelper.readableDatabase.rawQuery(selectQuery2, null)

        if (cursor2 != null && cursor2.moveToFirst()) {
            val sumAmount = cursor2.getInt(0)

            val userNameTextView: TextView = binding.displayUserAmount
            userNameTextView.text = "Środki na koncie: \n $sumAmount,00 PLN"

            Log.d("xD", "Środki na koncie: $sumAmount, 00 PLN")
        }
        cursor2.close()
    }

    private fun displayUserName() {
        val selectQuery3 = "SELECT userName FROM users WHERE id_user = 1"
        val cursor3 = databaseHelper.readableDatabase.rawQuery(selectQuery3, null)

        cursor3?.use {
            while (cursor3.moveToNext()) {
                val userName = cursor3.getString(cursor3.getColumnIndexOrThrow("userName"))

                val userNameTextView: TextView = binding.displayUserName
                userNameTextView.text = "Witaj $userName."

                Log.d("xD", "Witaj $userName.")
            }
        }
    }

    private fun displayFuneralsThisYear() {
        val selectQuery = "SELECT COUNT(*) AS thisYear FROM funeral WHERE strftime('%Y', date) = strftime('%Y', 'now')"
        val cursor = databaseHelper.readableDatabase.rawQuery(selectQuery, null)

        if (cursor != null && cursor.moveToFirst()) {
            val count = cursor.getInt(cursor.getColumnIndexOrThrow("thisYear"))

            val userNameTextView: TextView = binding.displayFuneralThisYear
            userNameTextView.text = "Pogrzeby \n w tym roku:\n \n $count szt."
        }
    }

    private fun displayFuneralsThisMonth() {
        val selectQuery = "SELECT COUNT(*) AS month FROM funeral WHERE strftime('%m', date) = strftime('%m', 'now') AND strftime('%Y', date) = strftime('%Y', 'now')"
        val cursor = databaseHelper.readableDatabase.rawQuery(selectQuery, null)

        if(cursor != null && cursor.moveToFirst()) {
            val count = cursor.getInt(cursor.getColumnIndexOrThrow("month"))

            val userNameTextView: TextView = binding.displayFuneralThisMonth
            userNameTextView.text = "Pogrzeby w tym \n miesiącu:\n \n $count szt."
        }
    }

    private fun displayFuneralsToday() {
        val selectQuery = "SELECT COUNT(*) AS today FROM funeral WHERE strftime('%d', date) = strftime('%d', 'now') AND strftime('%Y', date) = strftime('%Y', 'now') AND strftime('%m', date) = strftime('%m', 'now')"
        val cursor = databaseHelper.readableDatabase.rawQuery(selectQuery, null)

        if(cursor != null && cursor.moveToFirst()) {
            val count = cursor.getInt(cursor.getColumnIndexOrThrow("today"))

            val userNameTextView: TextView = binding.displayFuneralToday
            userNameTextView.text = "Pogrzeby \n Dzisiaj:\n \n $count szt."
        }
    }

    private fun displayFuneralsLastYear() {
        val selectQuery = "SELECT COUNT(*) AS lastYear FROM funeral WHERE strftime('%Y', date) = strftime('%Y', 'now', '-1 year')"
        val cursor = databaseHelper.readableDatabase.rawQuery(selectQuery, null)

        if(cursor != null && cursor.moveToFirst()) {
            val count = cursor.getInt(cursor.getColumnIndexOrThrow("lastYear"))

            val userNameTextView: TextView = binding.displayFuneralLastYear
            userNameTextView.text = "Pogrzeby w \n ubiegłym roku:\n \n $count szt."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}