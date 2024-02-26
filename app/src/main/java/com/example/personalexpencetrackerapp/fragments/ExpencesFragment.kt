package com.example.personalexpencetrackerapp.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.personalexpencetrackerapp.R
import com.example.personalexpencetrackerapp.adapters.CategoriesListAdapter
import com.example.personalexpencetrackerapp.adapters.ExpencesListAdapter
import com.example.personalexpencetrackerapp.databinding.FragmentExpencesBinding
import com.example.personalexpencetrackerapp.fragments.UpdateExpensesFragment.Companion._expencesEntitiy
import com.example.personalexpencetrackerapp.interfaces.OnClickListnerInterface
import com.example.personalexpencetrackerapp.roomDatabase.ExpencesEntitiy
import com.example.personalexpencetrackerapp.viewModel.ExpencesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@AndroidEntryPoint
class ExpencesFragment : Fragment(), OnClickListnerInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var _binding: FragmentExpencesBinding
    val roomViewModel: ExpencesViewModel by viewModels()
    private lateinit var expencesListAdapter: ExpencesListAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var categoriesList: MutableList<String>
    private lateinit var selectedCategory: String
    private lateinit var categoriesListAdapter: CategoriesListAdapter

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
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_expences, container, false)

        categoriesList = mutableListOf(
            "Food",
            "Entertainment",
            "Housing",
            "Utilities",
            "Fuel",
            "Automotive",
            "Misc"
        )


        lifecycleScope.launchWhenCreated {
            roomViewModel.getAllExpences().collect {
                expencesList(it)
            }
        }

        _binding.addNewExpence.setOnClickListener {
            findNavController().navigate(R.id.action_expencesFragment_to_addNewExpenseFragment)
        }

        _binding.filterExpenses.setOnClickListener {
            filterExpensesData()
        }

//        filterDate("date","2024-2-28")

        return _binding.root

    }

    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpencesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun expencesList(expencesEntitiys: List<ExpencesEntitiy>) {
        _binding.expencesList.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        expencesListAdapter = ExpencesListAdapter(
            expencesEntitiys as MutableList<ExpencesEntitiy>, this
        )
        _binding.expencesList!!.adapter = expencesListAdapter

    }

    override fun onClickListner(expencesEntitiy: ExpencesEntitiy, position: Int) {
        _expencesEntitiy = expencesEntitiy
        findNavController().navigate(R.id.action_expencesFragment_to_updateExpensesFragment)
    }


    fun filterDate(type: String, value: String) {
        roomViewModel.getExpencesByFilter(type, value).observe(viewLifecycleOwner)
        { expences ->
            expencesListAdapter.updateExpencesList(expences!! as MutableList<ExpencesEntitiy>)
        }
    }


    @SuppressLint("MissingInflatedId")
    private fun filterExpensesData() {

        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.FullWidthBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.item_bottomsheet, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        bottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        bottomSheetDialog.show()

        val expensesDate: TextView = view.findViewById(R.id.expensesDate)
        val clearFilter: TextView = view.findViewById(R.id.clearFilter)
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)

        clearFilter.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                roomViewModel.getAllExpences().collect {
                    expencesListAdapter.updateExpencesList(it as MutableList<ExpencesEntitiy>)
                }
            }
        }

        categoriesListAdapter = CategoriesListAdapter(
            requireContext(), categoriesList

        )
        categorySpinner.adapter = categoriesListAdapter
        categoryOnClickListner(categorySpinner)
        expensesDate.setOnClickListener {
            showDatePickerDialog()
        }


    }

    fun categoryOnClickListner(categorySpinner: Spinner) {
        categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categoriesList[position]
                    filterDate("category", selectedCategory)
                 }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle the case when nothing is selected (if needed)
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

                filterDate("date", selectedDate)
                bottomSheetDialog.dismiss()
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

}