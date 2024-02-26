package com.example.personalexpencetrackerapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.personalexpencetrackerapp.databinding.ViewExpenseLayoutBinding
import com.example.personalexpencetrackerapp.interfaces.OnClickListnerInterface
import com.example.personalexpencetrackerapp.roomDatabase.ExpencesEntitiy

class ExpencesListAdapter(
    var data: MutableList<ExpencesEntitiy>?,
    var clickListner: OnClickListnerInterface
) : RecyclerView.Adapter<CartItemsListViewHolder>() {

    private lateinit var binding: ViewExpenseLayoutBinding
    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CartItemsListViewHolder {
        binding = ViewExpenseLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return CartItemsListViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: CartItemsListViewHolder, position: Int) {
        holder.initialize(data!![position], clickListner)
    }

    fun updateExpencesList(filterData: MutableList<ExpencesEntitiy>?){
        data = mutableListOf()
        data = filterData
        notifyDataSetChanged()
    }


}

class CartItemsListViewHolder(itemView: ViewExpenseLayoutBinding) :
    RecyclerView.ViewHolder(itemView.root) {

    val binding = itemView

    @SuppressLint("SetTextI18n")
    fun initialize(data: ExpencesEntitiy, action: OnClickListnerInterface) {

        binding.categoryName.text = data.category
        binding.expenseDate.text = data.date
        binding.amount.text = data.amount+" PKR"

        binding.editExpences.setOnClickListener {
            action.onClickListner(data, adapterPosition)
        }

    }

}