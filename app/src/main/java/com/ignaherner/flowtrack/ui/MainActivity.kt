package com.ignaherner.flowtrack.ui

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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
import com.ignaherner.flowtrack.domain.model.TransactionCategory
import com.ignaherner.flowtrack.domain.model.TransactionType
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // Usamos el delegate viewModels con nuestra factory
    private val viewModel : MainViewModel by viewModels {
        MainViewModelFactory(applicationContext)
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

        // 4) Accion del FAB: ahora abrimos el dialogo para cargar el movimiento
        fabAddTransaction.setOnClickListener {
            showAddTransactionDialog()
        }

    }

    /**
     * Observa los StateFlows del ViewModel y actualiza la UI
     */
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

                        // Cambiar color del saldo segun sea positivo, negativo o cero
                        val balanceColorRes = when {
                            summary.balance > 0.0 -> R.color.balancePositive
                            summary.balance < 0.0 -> R.color.balanceNegative
                            else -> R.color.balanceNeutral
                        }

                        tvBalanceValue.setTextColor(
                            androidx.core.content.ContextCompat.getColor(
                                this@MainActivity,
                                balanceColorRes
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Muestra un dialogo para ingresar un nuevo movimiento
     * El usuario completa: titulo, monto y tipo (ingreso o gasto)
     */

    private fun showAddTransactionDialog() {
        // Inflamos el layout del dialogo
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_transaction, null)

        // Referencias a los campos del dialogo
        val etTitle = dialogView.findViewById<EditText>(R.id.etTitle)
        val etAmount = dialogView.findViewById<EditText>(R.id.etAmount)
        val etNote = dialogView.findViewById<EditText>(R.id.etNote)
        val rgType = dialogView.findViewById<RadioGroup>(R.id.rgType)
        val rbIncome = dialogView.findViewById<RadioButton>(R.id.rbIncome)
        val rbExpense = dialogView.findViewById<RadioButton>(R.id.rbExpense)
        val spCategory = dialogView.findViewById<Spinner>(R.id.spCategory)

        //Preparamos las categorias para el Spinner
        val categories = TransactionCategory.values()
        val categoryLabels = listOf(
            "Salario",
            "Alquiler",
            "Comida",
            "Transporte",
            "Entretenimiento",
            "Otros"
        )

        val spinnerAdapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                categoryLabels
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        spCategory.adapter = spinnerAdapter



        //Construimos el AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Nuevo movimiento")
            .setView(dialogView)
            .setPositiveButton("Guardar") {_, _ ->
                // Importante: esta lamba se ejecuta cuando se toca "Guardar"

                val title = etTitle.text.toString().trim()
                val amountText = etAmount.text.toString().trim()
                val noteText = etNote.text.toString().trim()


                // Validacion basica de campos
                if (title.isEmpty()) {
                    Toast.makeText(this, "El titulo no puede estar vacio", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val amount = amountText.toDoubleOrNull()
                if (amount == null || amount <= 0.0) {
                    Toast.makeText(this, "Ingresa un monto valido mayor a 0", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Determinamos el tipo segun el RadioButton seleccionado
                val type = when {
                    rbIncome.isChecked -> TransactionType.INCOME
                    rbExpense.isChecked -> TransactionType.EXPENSE
                    else -> TransactionType.INCOME // Default de seguridad
                }

                val selectedIndex = spCategory.selectedItemPosition
                val category = categories.getOrElse(selectedIndex) { TransactionCategory.OTHER}

                // Llamamos al ViewModel para agregar el movimiento
                viewModel.addTransaction(
                    title = title,
                    amount = amount,
                    type = type,
                    category = category,
                    note = noteText
                )
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}