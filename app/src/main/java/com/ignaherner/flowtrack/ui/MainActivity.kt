package com.ignaherner.flowtrack.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ignaherner.flowtrack.R
import com.ignaherner.flowtrack.domain.model.TransactionType
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Usamos el delegate viewModels con nuestra factory
    private val viewModel : MainViewModel by viewModels {
        MainViewModelFactory()
    }

    // Referencias a vistas
    private lateinit var tvIncomeValue: TextView
    private lateinit var tvExpenseValue: TextView
    private lateinit var tvBalanceValue: TextView
    private lateinit var rvTransactions: RecyclerView
    private lateinit var fabAddTransaction: FloatingActionButton

    // Adapter de la lista
    private val transactionAdapter = TransactionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) Referencias a la UI
        tvIncomeValue = findViewById(R.id.tvIncomeValue)
        tvExpenseValue = findViewById(R.id.tvExpenseValue)
        tvBalanceValue = findViewById(R.id.tvBalanceValue)
        rvTransactions = findViewById(R.id.rvTransactions)
        fabAddTransaction = findViewById(R.id.fabAddTransaction)

        // 2) Configurar RecyclerView
        rvTransactions.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = transactionAdapter
        }

        // 3) Observar los flows del ViewModel
        observeViewModel()

        // 4) Accion del FAB: por ahora agregamos un movimiento de prueba
        fabAddTransaction.setOnClickListener {
            // Por ahora para probar, agregamos un "Ingreso demo"
            // En l bloque 3 lo reemplazamos por un dialogo para cargar datos
            viewModel.addTransaction(
                title = "Ingreso demo",
                amount = 1000.0,
                type = TransactionType.INCOME
            )
        }

    }

    private fun observeViewModel() {
        // Usamos repeatOnLifecycle para recoger los flows de forma segura
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Observamos la lista de movimientos
                launch {
                    viewModel.transaction.collect { list ->
                        transactionAdapter.submitList(list)
                    }
                }

                // Observamos el resumen
                launch {
                    viewModel.summary.collect { summary ->
                        tvIncomeValue.text = String.format("$ %.2f", summary.totalIncome)
                        tvExpenseValue.text = String.format("$ %.2f", summary.totalExpense)
                        tvBalanceValue.text = String.format("$ %.2f", summary.balance)
                    }
                }
            }
        }
    }
}