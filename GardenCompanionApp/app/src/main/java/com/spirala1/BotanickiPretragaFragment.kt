package com.spirala1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class BotanickiPretragaFragment : Fragment() {

    private lateinit var pretragaET: EditText
    private lateinit var bojaSPIN: Spinner
    private lateinit var brzaPretragaBtn: Button
    private lateinit var biljkeRV: RecyclerView
    private lateinit var adapterBot: BotanickiListaAdapter

    private lateinit var biljkaDao: BiljkaDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_botanicki, container, false)

        biljkaDao = BiljkaDatabase.getInstance(requireContext()).biljkaDao()

        pretragaET = view.findViewById(R.id.pretragaET)
        bojaSPIN = view.findViewById(R.id.bojaSPIN)
        brzaPretragaBtn = view.findViewById(R.id.brzaPretraga)
        biljkeRV = activity?.findViewById(R.id.biljkeRV)!!
        val btn = activity?.findViewById<Button>(R.id.resetBtn)

        lateinit var listaBiljaka: List<Biljka> // Declare listaBiljaka here

        // Launching a coroutine on the IO dispatcher
        lifecycleScope.launch {
            // Move the database operation to a background thread
            listaBiljaka = withContext(Dispatchers.IO) {
                biljkaDao.getAllBiljkas()
            }

            adapterBot = BotanickiListaAdapter(listOf())
            biljkeRV.adapter = adapterBot
            adapterBot.updateBotanicki(listaBiljaka)

            adapterBot.onItemClick = {
                selectedBiljka ->
                val filteredBiljke = listaBiljaka.filter { biljka ->
                    biljka.porodica == selectedBiljka.porodica &&
                            biljka.klimatskiTipovi.any { klima ->
                                selectedBiljka.klimatskiTipovi.contains(
                                    klima
                                )
                            } &&
                            biljka.zemljisniTipovi.any { zemljiste ->
                                selectedBiljka.zemljisniTipovi.contains(
                                    zemljiste
                                )
                            }

                }
                adapterBot.updateBotanicki(filteredBiljke)
                if (btn != null) {
                    btn.setOnClickListener {
                        biljkeRV.adapter = adapterBot
                        adapterBot.updateBotanicki(listaBiljaka)
                    }
                }
            }
        }

        val colorChoice = resources.getStringArray(R.array.Boje)
        bojaSPIN.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, colorChoice)

        brzaPretragaBtn.setOnClickListener {
            val query = pretragaET.text.toString()
            val selectedColor = bojaSPIN.selectedItem.toString()

            if (query.isNotEmpty() && selectedColor.isNotEmpty()) {
                performSearch(query, selectedColor)
            } else {
                Toast.makeText(requireContext(), "Please enter a query and select a color", Toast.LENGTH_SHORT).show()
            }
            if (btn != null) {
                btn.setOnClickListener {
                    biljkeRV.adapter = adapterBot
                    adapterBot.updateBotanicki(listaBiljaka)
                }
            }
        }
        return view
    }


    private fun performSearch(query: String, selectedColor: String) {
        lifecycleScope.launch {
            try {
                val filteredBiljke = withContext(Dispatchers.IO) {
                    TrefleDAO().getPlantswithFlowerColor(selectedColor.toLowerCase(Locale.ROOT), query)
                }

                if (filteredBiljke.isNotEmpty()) {
                    adapterBot.updateBotanicki(filteredBiljke,true)
                } else {
                    Log.d("BotanickiPretraga", "Nema rezultata pretrage")
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error fetching plants: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}