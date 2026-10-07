package com.industri.fintrack

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.industri.fintrack.adapter.TransactionAdapter
import com.industri.fintrack.viewmodel.TransactionViewModel
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val txViewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvBalance = findViewById<TextView>(R.id.tvTotalBalance)
        val rvTransactions = findViewById<RecyclerView>(R.id.rvTransactions)
        val fabAdd = findViewById<ExtendedFloatingActionButton>(R.id.fabAdd)

        val adapter = TransactionAdapter(
            onLongClick = { txToDelete -> 
                confirmDeleteDialog(txToDelete)
            }
        )
        rvTransactions.adapter = adapter
        rvTransactions.layoutManager = LinearLayoutManager(this)

        txViewModel.allTransactions.observe(this) { txList ->
            adapter.setData(txList)
        }

        txViewModel.currentBalance.observe(this) { balance ->
            val actualBalance = balance ?: 0.0
            val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            tvBalance.text = formatter.format(actualBalance)
        }

        fabAdd.setOnClickListener {
            showAddTransactionDialog()
        }
    }

    private fun showAddTransactionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Catat Transaksi Baru")

        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(50, 20, 50, 20) }
        
        val inputTitle = EditText(this).apply { hint = "Keterangan (Cth: Beli Kopi)" }
        val inputAmount = EditText(this).apply { hint = "Nominal (Cth: 20000)"; inputType = android.text.InputType.TYPE_CLASS_NUMBER }
        
        layout.addView(inputTitle)
        layout.addView(inputAmount)
        builder.setView(layout)

        builder.setPositiveButton("Pemasukan (INCOME)") { _, _ ->
            val title = inputTitle.text.toString()
            val amountStr = inputAmount.text.toString()
            if (title.isNotEmpty() && amountStr.isNotEmpty()) {
                txViewModel.addTransaction(title, amountStr.toDouble(), "INCOME")
            }
        }
        
        builder.setNeutralButton("Pengeluaran (EXPENSE)") { _, _ ->
            val title = inputTitle.text.toString()
            val amountStr = inputAmount.text.toString()
            if (title.isNotEmpty() && amountStr.isNotEmpty()) {
                txViewModel.addTransaction(title, amountStr.toDouble(), "EXPENSE")
            }
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun confirmDeleteDialog(tx: com.industri.fintrack.model.TransactionEntity) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Transaksi")
            .setMessage("Yakin ingin menghapus catatan '${tx.title}'?")
            .setPositiveButton("Hapus") { _, _ ->
                txViewModel.deleteTransaction(tx)
                Toast.makeText(this, "Transaksi Dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
