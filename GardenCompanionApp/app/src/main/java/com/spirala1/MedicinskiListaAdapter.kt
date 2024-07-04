package com.spirala1

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MedicinskiListaAdapter(private var biljke: List<Biljka>) :
    RecyclerView.Adapter<MedicinskiListaAdapter.BiljkeViewHolder>() {

    var onItemClick: ((Biljka) -> Unit)? = null
    private lateinit var appContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkeViewHolder {
        appContext=parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.medicinski_fokus_layout, parent, false)
        return BiljkeViewHolder(view)
    }

    override fun getItemCount(): Int = biljke.size

    override fun onBindViewHolder(holder: BiljkeViewHolder, position: Int) {

        holder.nazivD.text = biljke[position].naziv

        holder.upozorenjeD.text = biljke[position].medicinskoUpozorenje

        // Prikaz medicinskih koristi
        val koristi = biljke[position].medicinskeKoristi
        if (koristi.isNotEmpty()) {
            holder.korist1ItemD.text = koristi[0].opis
        } else {
            holder.korist1ItemD.visibility = View.GONE
        }

        if (koristi.size > 1) {
            holder.korist2ItemD.text = koristi[1].opis
        } else {
            holder.korist2ItemD.visibility = View.GONE
        }

        if (koristi.size > 2) {
            holder.korist3ItemD.text = koristi[2].opis
        } else {
            holder.korist3ItemD.visibility = View.GONE
        }

        // Klik na element liste
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(biljke[position])
        }

        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = BiljkaDatabase.getInstance(appContext).biljkaDao().getImageForBiljka(biljke[position].id!!)
            holder.slika.setImageBitmap(bitmap)
        }
    }

    fun updateMedicinske(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }

    inner class BiljkeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika: ImageView=itemView.findViewById(R.id.slikaItem)
        val nazivD: TextView = itemView.findViewById(R.id.nazivItem)
        val upozorenjeD: TextView = itemView.findViewById(R.id.upozorenjeItem)
        val korist1ItemD: TextView = itemView.findViewById(R.id.korist1Item)
        val korist2ItemD: TextView = itemView.findViewById(R.id.korist2Item)
        val korist3ItemD: TextView = itemView.findViewById(R.id.korist3Item)
    }


}