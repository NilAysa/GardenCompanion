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
import kotlinx.coroutines.withContext

class BotanickiListaAdapter (private var biljke:List<Biljka>): RecyclerView.Adapter<BotanickiListaAdapter.BiljkeViewHolder>(){
    private lateinit var appContext:Context
    private var searching=false
    var onItemClick:((Biljka)->Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiljkeViewHolder {
        appContext=parent.context
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.botanicki_fokus_layout,parent,false)
        return BiljkeViewHolder(view)
    }
    override fun getItemCount(): Int=biljke.size

    override fun onBindViewHolder(holder: BiljkeViewHolder, position: Int) {
        holder.nazivD.text = biljke[position].naziv
        holder.porodica.text = biljke[position].porodica

        // Check if klimatskiTipovi list is not empty before accessing its first element
        if (biljke[position].klimatskiTipovi.isNotEmpty()) {
            holder.klimatskiTip.text = biljke[position].klimatskiTipovi[0].opis
        } else {
            holder.klimatskiTip.text = "" // Set empty string if the list is empty
        }

        // Check if zemljisniTipovi list is not empty before accessing its first element
        if (biljke[position].zemljisniTipovi.isNotEmpty()) {
            holder.zemljiste.text = biljke[position].zemljisniTipovi[0].naziv
        } else {
            holder.zemljiste.text = "" // Set empty string if the list is empty
        }

        holder.itemView.setOnClickListener {
            if(searching==false) {
                onItemClick?.invoke(biljke[position])
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            if (searching) {
                val bitmap = withContext(Dispatchers.IO) {
                    TrefleDAO().getImage(biljke[position])
                }
                holder.slika.setImageBitmap(bitmap)

            } else {
                val bitmap = withContext(Dispatchers.IO) {
                    BiljkaDatabase.getInstance(appContext).biljkaDao().getImageForBiljka(biljke[position].id!!)
                }
                holder.slika.setImageBitmap(bitmap)
            }
        }
    }

    fun updateBotanicki(biljke: List<Biljka>,pretraga:Boolean=false){
        searching=pretraga
        this.biljke=biljke
        notifyDataSetChanged()
    }
    inner class BiljkeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val slika:ImageView=itemView.findViewById(R.id.slikaItem)
        val nazivD: TextView =itemView.findViewById(R.id.nazivItem)
        val porodica: TextView =itemView.findViewById(R.id.porodicaItem)
        val zemljiste: TextView =itemView.findViewById(R.id.zemljisniTipItem)
        val klimatskiTip: TextView =itemView.findViewById(R.id.klimatskiTipItem)
    }
    private suspend fun loadPlantImage(biljka: Biljka): Bitmap {
        Log.d("MedicinskiListaAdapter", "Pozivam TrefleDAO.getImage za biljku: ${biljka.naziv}")
        val bitmap = TrefleDAO().getImage(biljka)
        Log.d("MedicinskiListaAdapter", "Dobio sam bitmapu veličine ${bitmap.width}x${bitmap.height}")
        return bitmap
    }
}