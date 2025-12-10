package com.ignaherner.flowtrack.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.flowtrack.R
import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.model.TransactionCategory
import com.ignaherner.flowtrack.domain.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Adapter profesional usando ListAdapter + DiffUtil.
// Esto permite actualizaciones eficientes y animaciones al cambiar la lista.
class TransactionAdapter :
    ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback) {

    init {
        // Le decimos al RecyclerView que los IDs son estables
        setHasStableIds(true)
    }

    private var items: List<Transaction> = emptyList()

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
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
        val item = getItem(position)
        val context = holder.itemView.context

        // Titulo del movimiento
        holder.tvTitle.text = item.title

        // Formato de moneda
        // Usamos locale de Argentina como ejemplo (podes usar Locale.getDefault())
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "AR"))
        val amountText = currencyFormat.format(item.amount)
        holder.tvAmount.text = amountText

        // Tipo y color segun INCOME / EXPENSE
        when(item.type) {
            TransactionType.INCOME -> {
                holder.tvType.text = "Ingreso"
                holder.tvAmount.setTextColor(
                    ContextCompat.getColor(context, R.color.incomeColor)
                )
            }

            TransactionType.EXPENSE -> {
                holder.tvType.text = "Gasto"
                holder.tvAmount.setTextColor(
                    ContextCompat.getColor(context, R.color.expenseColor)
                )
            }
        }

        // Categoria (texto amigable)
        val categoryText = when (item.category) {
            TransactionCategory.SALARY -> "Salario"
            TransactionCategory.RENT -> "Alquiler"
            TransactionCategory.FOOD -> "Comida"
            TransactionCategory.TRANSPORT -> "Transporte"
            TransactionCategory.ENTERTAINMENT -> "Entretenimiento"
            TransactionCategory.OTHER -> "Otros"
        }
        holder.tvCategory.text = categoryText

        // Formato de fecha simple a partir del timestamp
        // dd/MM/yyyy HH:mm -> "07/12/2025 14:30"
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateText = dateFormat.format(Date(item.timestamp))
        holder.tvDate.text = dateText
    }

    override fun getItemId(position: Int) : Long {
        return getItem(position).id
    }

object TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    // Determina si dos items representan la misma entidad (mismo ID)
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.id == newItem.id
    }

    // Determina si el contenido del item es el mismo
    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        // Como Transaction es data class, equals ya compara campo a campo
        return oldItem == newItem
    }
}

}