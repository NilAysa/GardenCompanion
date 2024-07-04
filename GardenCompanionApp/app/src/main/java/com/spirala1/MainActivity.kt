package com.spirala1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var biljkeR: RecyclerView
    private lateinit var adapterMed: MedicinskiListaAdapter
    private lateinit var adapterBot: BotanickiListaAdapter
    private lateinit var adapterKuh: KuharskiListaAdapter
    private lateinit var biljkaDAO: BiljkaDAO

    private val addPlantResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                CoroutineScope(Dispatchers.Main).launch {
                    val listaBiljaka = withContext(Dispatchers.IO) {
                        biljkaDAO.getAllBiljkas()
                    }
                    adapterMed.updateMedicinske(listaBiljaka)
                    adapterBot.updateBotanicki(listaBiljaka)
                    adapterKuh.updateKuharske(listaBiljaka)
                    spinner.setSelection(0)

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        biljkaDAO = BiljkaDatabase.getInstance(applicationContext).biljkaDao()


        spinner = findViewById<Spinner>(R.id.modSpinner)

        val spinnerOptions = resources.getStringArray(R.array.SpinnerModovi)
        spinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, spinnerOptions)

        val btn=findViewById<Button>(R.id.resetBtn)
        biljkeR = findViewById(R.id.biljkeRV)
        biljkeR.layoutManager = LinearLayoutManager(this)

        adapterMed = MedicinskiListaAdapter(listOf())
        adapterBot = BotanickiListaAdapter(listOf())
        adapterKuh = KuharskiListaAdapter(listOf())

        biljkeR.adapter = adapterMed
        CoroutineScope(Dispatchers.Main).launch {
            val listaBiljaka = withContext(Dispatchers.IO) {
                biljkaDAO.getAllBiljkas()
            }
            adapterMed.updateMedicinske(listaBiljaka)
        }


        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                Log.d("MainActivity", "BACA ERROR onItemSelected: Position: $position")
                if (spinnerOptions[position].toString() == "Botanički") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, BotanickiPretragaFragment()).commit()


                } else {
                    if (spinnerOptions[position].toString() == "Medicinski") {
                        replaceFragmentWithEmpty()
                        biljkeR.adapter = adapterMed
                        CoroutineScope(Dispatchers.Main).launch {
                            val listaBiljaka = withContext(Dispatchers.IO) {
                                biljkaDAO.getAllBiljkas()
                            }
                            adapterMed.updateMedicinske(listaBiljaka)


                        adapterMed.onItemClick = { selectedBiljka ->
                            val filteredBiljke = when (spinnerOptions[position].toString()) {
                                "Medicinski" -> listaBiljaka.filter { biljka ->
                                    biljka.medicinskeKoristi.any { korist ->
                                        selectedBiljka.medicinskeKoristi.contains(korist)
                                    }
                                }

                                else -> listOf()
                            }
                            adapterMed.updateMedicinske(filteredBiljke)
                            btn.setOnClickListener {
                                biljkeR.adapter = adapterMed
                                adapterMed.updateMedicinske(listaBiljaka)
                            }
                        }

                            }
                    } else if (spinnerOptions[position].toString() == "Kuharski") {
                        replaceFragmentWithEmpty()
                        biljkeR.adapter = adapterKuh
                        CoroutineScope(Dispatchers.Main).launch {
                            val listaBiljaka = withContext(Dispatchers.IO) {
                                biljkaDAO.getAllBiljkas()
                            }
                            adapterKuh.updateKuharske(listaBiljaka)

                            adapterKuh.onItemClick = { selectedBiljka ->
                                val filteredBiljke = when (spinnerOptions[position].toString()) {
                                    "Kuharski" -> listaBiljaka.filter { biljka ->
                                        biljka.profilOkusa == selectedBiljka.profilOkusa || biljka.jela.intersect(
                                            selectedBiljka.jela
                                        ).isNotEmpty()
                                    }

                                    else -> listOf()
                                }
                                adapterKuh.updateKuharske(filteredBiljke)
                                btn.setOnClickListener {
                                    biljkeR.adapter = adapterKuh
                                    adapterKuh.updateKuharske(listaBiljaka)
                                }
                            }
                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                biljkeR.adapter = adapterMed
                CoroutineScope(Dispatchers.Main).launch {
                    val listaBiljaka = withContext(Dispatchers.IO) {
                        biljkaDAO.getAllBiljkas()
                    }
                    adapterMed.updateMedicinske(listaBiljaka)
                }
            }
        }

        //SPIRALA2

        //Button za dodavanje nove biljke
        val dodaj_biljku=findViewById<Button>(R.id.novaBiljkaBtn)
        dodaj_biljku.setOnClickListener {
            addPlantResultLauncher.launch(Intent(this, NovaBiljkaActivity::class.java))
        }

    }
    private fun replaceFragmentWithEmpty() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, Fragment())
            .commit()
    }

}