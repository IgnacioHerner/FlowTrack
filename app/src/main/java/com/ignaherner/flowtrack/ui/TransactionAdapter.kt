package com.ignaherner.flowtrack.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.flowtrack.R
import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.model.TransactionType

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    private var items: List<Transaction> = emptyList()

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TransactionViewHolder,
        position: Int
    ) {
        val item = items[position]

        // Titulo del movimiento
        holder.tvTitle.text = item.title

        // Monto formateado bascico (luego hacemos algo mas lindo)
        val amountText = String.format("$ %.2f", item.amount)
        holder.tvAmount.text = amountText

        // Tipo: Ingreso o Gasto
        holder.tvType.text = when(item.type) {
            TransactionType.INCOME -> "Ingreso"
            TransactionType.EXPENSE -> "Gasto"
        }

        holder.tvDate.text = "Ahora"
    }

    override fun getItemCount(): Int = items.size

    // Funcion publica para actualziar la lista que muestra el adapter
    fun submitList(newItems: List<Transaction>) {
        // Version simple: reemplaza todo y refresca
        // Mas adelante: optimizar con DiffUtil
        items = newItems
        notifyDataSetChanged()
    }

}