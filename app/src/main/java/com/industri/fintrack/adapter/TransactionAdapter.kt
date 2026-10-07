package com.industri.fintrack.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.industri.fintrack.R
import com.industri.fintrack.model.TransactionEntity
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(
    private val onLongClick: (TransactionEntity) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TxViewHolder>() {

    private var txList = emptyList<TransactionEntity>()
    private val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    inner class TxViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTxTitle)
        val tvType: TextView = view.findViewById(R.id.tvTxType)
        val tvAmount: TextView = view.findViewById(R.id.tvTxAmount)

        init {
            view.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLongClick(txList[position])
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TxViewHolder(view)
    }

    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        val currentTx = txList[position]
        holder.tvTitle.text = currentTx.title
        holder.tvType.text = currentTx.transactionType
        
        val formattedAmount = formatter.format(currentTx.amount)
        if (currentTx.transactionType == "INCOME") {
            holder.tvAmount.text = "+ $formattedAmount"
            holder.tvAmount.setTextColor(Color.parseColor("#10B981"))
            holder.tvType.setTextColor(Color.parseColor("#10B981"))
        } else {
            holder.tvAmount.text = "- $formattedAmount"
            holder.tvAmount.setTextColor(Color.parseColor("#EF4444"))
            holder.tvType.setTextColor(Color.parseColor("#EF4444"))
        }
    }

    override fun getItemCount() = txList.size

    fun setData(transactions: List<TransactionEntity>) {
        this.txList = transactions
        notifyDataSetChanged()
    }
}
