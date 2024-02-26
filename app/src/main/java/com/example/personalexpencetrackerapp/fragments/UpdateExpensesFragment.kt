package com.example.personalexpencetrackerapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.personalexpencetrackerapp.R
import com.example.personalexpencetrackerapp.adapters.CategoriesListAdapter
import com.example.personalexpencetrackerapp.databinding.FragmentUpdateExpensesBinding
import com.example.personalexpencetrackerapp.roomDatabase.ExpencesEntitiy
import com.example.personalexpencetrackerapp.viewModel.ExpencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class UpdateExpensesFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var _binding: FragmentUpdateExpensesBinding
    private lateinit var categoriesList: MutableList<String>
    private lateinit var selectedCategory: String
    private lateinit var categoriesListAdapter: CategoriesListAdapter
    val roomViewModel: ExpencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_update_expenses, container, false)

        _binding.amount.setText(_expencesEntitiy.amount)
        _binding.expensesDate.text = _expencesEntitiy.date

        _binding.expensesDate.setOnClickListener {
            showDatePickerDialog()
        }

        categoriesList = mutableListOf(
            "Food",
            "Entertainment",
            "Housing",
            "Utilities",
            "Fuel",
            "Automotive",
            "Misc"
        )

        categoriesListAdapter = CategoriesListAdapter(
            requireContext(), categoriesList

        )
        _binding.categorySpinner.adapter = categoriesListAdapter
        categoryOnClickListner()

        _binding.addNewExpence.setOnClickListener {
            if (_binding.amount.text.toString().isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.enterAmount),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val expencesEntitiy = ExpencesEntitiy()
                expencesEntitiy.Id = _expencesEntitiy.Id
                expencesEntitiy.amount = _binding.amount.text.toString()
                expencesEntitiy.category = selectedCategory
                expencesEntitiy.date = _binding.expensesDate.text.toString()
                lifecycleScope.launch {
                    roomViewModel.updateExpence(
                        expencesEntitiy
                    )
                }
                findNavController().navigateUp()
                Toast.makeText(requireContext(),getString(R.string.expensesUpdated),Toast.LENGTH_SHORT).show()
            }
        }

        return _binding.root

    }

    companion object {
        var _expencesEntitiy: ExpencesEntitiy = ExpencesEntitiy()


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateExpensesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display the selected date in a TextView or do something with it
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"

                _binding.expensesDate.text = selectedDate
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    fun categoryOnClickListner() {
        _binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categoriesList[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case when nothing is selected (if needed)
                }
            }

        for (category in categoriesList.indices) {
            if (_expencesEntitiy.category == categoriesList[category]) {
                _binding.categorySpinner.setSelection(category)
            }
        }
    }
}