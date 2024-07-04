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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KuharskiListaAdapter (private var biljke: List<Biljka>) : RecyclerView.Adapter<KuharskiListaAdapter.BiljkeViewHolder>() {
    private lateinit var appContext:Context
    var onItemClick:((Biljka)->Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkeViewHolder {
        appContext=parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.kuharski_fokus_layout, parent, false)
        return BiljkeViewHolder(view)
    }

    override fun getItemCount(): Int=biljke.size

    override fun onBindViewHolder(holder: BiljkeViewHolder, position: Int) {
        holder.nazivItem.text = biljke[position].naziv

        val jelaList = biljke[position].jela
        if (jelaList.isNotEmpty()) {
            holder.jelo1Item.text = jelaList[0]
        } else {
            holder.jelo1Item.visibility = View.GONE
        }

        if (jelaList.size > 1) {
            holder.jelo2Item.text = jelaList[1]
        } else {
            holder.jelo2Item.visibility = View.GONE
        }

        if (jelaList.size > 2) {
            holder.jelo3Item.text = jelaList[2]
        } else {
            holder.jelo3Item.visibility = View.GONE
        }

        holder.profilOkusaItem.text=biljke[position].profilOkusa?.opis

        // Klik na element liste
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(biljke[position])
        }

        CoroutineScope(Dispatchers.Main).launch {
            val bitmap = BiljkaDatabase.getInstance(appContext).biljkaDao().getImageForBiljka(biljke[position].id!!)
            holder.slika.setImageBitmap(bitmap)
        }

    }
    inner class BiljkeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val slika:ImageView=itemView.findViewById(R.id.slikaItem)
        val nazivItem: TextView = itemView.findViewById(R.id.nazivItem)
        val profilOkusaItem: TextView = itemView.findViewById(R.id.profilOkusaItem)
        val jelo1Item: TextView = itemView.findViewById(R.id.jelo1Item)
        val jelo2Item: TextView = itemView.findViewById(R.id.jelo2Item)
        val jelo3Item: TextView = itemView.findViewById(R.id.jelo3Item)
    }
    fun updateKuharske(biljke: List<Biljka>) {
        this.biljke = biljke
        notifyDataSetChanged()
    }
    private suspend fun loadPlantImage(biljka: Biljka): Bitmap {
        Log.d("MedicinskiListaAdapter", "Pozivam TrefleDAO.getImage za biljku: ${biljka.naziv}")
        val bitmap = TrefleDAO().getImage(biljka)
        Log.d("MedicinskiListaAdapter", "Dobio sam bitmapu veličine ${bitmap.width}x${bitmap.height}")
        return bitmap
    }
}