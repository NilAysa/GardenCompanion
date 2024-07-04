package com.spirala1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NovaBiljkaActivity : AppCompatActivity() {

    private lateinit var nazivET: EditText
    private lateinit var porodicaET: EditText
    private lateinit var medicinskoUpozorenjeET: EditText
    private lateinit var jeloET: EditText
    private lateinit var medicinskaKoristLV: ListView
    private lateinit var klimatskiTipLV: ListView
    private lateinit var zemljisniTipLV: ListView
    private lateinit var profilOkusaLV: ListView
    private lateinit var jelaLV: ListView

    private lateinit var dodajBiljkuBtn:Button
    private lateinit var dodajJeloBtn: Button
    // Lista jela
    private val listaJela = mutableListOf<String>()
    // Adapter za listu jela
    private lateinit var jelaAdapter: ArrayAdapter<String>
    private var dodavanjeNovogJela = true

    //kamera
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private lateinit var uslikajBiljku:Button
    private lateinit var slika:ImageView

    private lateinit var baza:BiljkaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_biljka)

        baza=BiljkaDatabase.getInstance(applicationContext).biljkaDao()

        // Inicijalizacija EditText-ova
        nazivET = findViewById(R.id.nazivET)
        porodicaET = findViewById(R.id.porodicaET)
        medicinskoUpozorenjeET = findViewById(R.id.medicinskoUpozorenjeET)
        jeloET = findViewById(R.id.jeloET)

        // Inicijalizacija ListView-ova
        medicinskaKoristLV = findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV = findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV = findViewById(R.id.zemljisniTipLV)
        profilOkusaLV = findViewById(R.id.profilOkusaLV)
        jelaLV = findViewById(R.id.jelaLV)

        medicinskaKoristLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            MedicinskaKorist.entries.map { it.opis }
        )
        klimatskiTipLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            KlimatskiTip.entries.map { it.opis }
        )
        zemljisniTipLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            Zemljiste.entries.map { it.naziv }
        )
        profilOkusaLV.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            ProfilOkusaBiljke.entries.map { it.opis }
        )

        medicinskaKoristLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        klimatskiTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        zemljisniTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        profilOkusaLV.choiceMode = ListView.CHOICE_MODE_SINGLE

        var selectedValueProfil: String? = null
        profilOkusaLV.setOnItemClickListener { parent, view, position, id ->
            selectedValueProfil = parent.getItemAtPosition(position) as String
        }

        jelaLV.choiceMode = ListView.CHOICE_MODE_SINGLE

        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)

        dodajJeloBtn.setOnClickListener {
            val novoJelo = jeloET.text.toString().trim()
            if (novoJelo.isNotEmpty()) {
                if (dodavanjeNovogJela) {
                    listaJela.add(novoJelo)
                } else {
                    val selectedIndex = jelaLV.checkedItemPosition
                    if (selectedIndex != ListView.INVALID_POSITION) {
                        listaJela[selectedIndex] = novoJelo
                    }
                }

                jeloET.setText("")
                dodajJeloBtn.text = "Dodaj jelo"
                dodavanjeNovogJela = true
            } else if (!dodavanjeNovogJela) {
                val selectedIndex = jelaLV.checkedItemPosition
                if (selectedIndex != ListView.INVALID_POSITION) {
                    listaJela.removeAt(selectedIndex)
                    jeloET.setText("")
                    dodajJeloBtn.text = "Dodaj jelo"
                    dodavanjeNovogJela = true
                }
            }
            jelaLV.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaJela)
        }

// Postavljanje slušača za odabir stavki u listi jela
        jelaLV.setOnItemClickListener { _, _, position, _ ->
            val izabranoJelo = listaJela[position]
            jeloET.setText(izabranoJelo)
            dodajJeloBtn.text = "Izmijeni jelo"
            dodavanjeNovogJela = false
        }
//btn dodaj biljku
        dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)
        dodajBiljkuBtn.setOnClickListener {

            if (validateInput()) {
                podaci()
            }
        }

//USLIKAJ BILJKU BTN
        slika=findViewById(R.id.slikaIV)
        uslikajBiljku=findViewById(R.id.uslikajBiljkuBtn)
        uslikajBiljku.setOnClickListener {
            checkCameraPermission()
        }
    }
    fun validateInput(): Boolean {
        val naziv = nazivET.text.toString()
        val porodica = porodicaET.text.toString()
        val medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString()

        // Provjera dužine teksta u EditText poljima
        if (naziv.length <= 2 || naziv.length >= 20) {
            nazivET.setError("Naziv mora biti duži od 2 i kraći od 20 znakova")
            return false
        }
        if (porodica.length <= 2 || porodica.length >= 20) {
            porodicaET.setError("Porodica mora biti duža od 2 i kraća od 20 znakova")
            return false
        }
        if (medicinskoUpozorenje.length <= 2 || medicinskoUpozorenje.length >= 20) {
            medicinskoUpozorenjeET.setError("Medicinsko upozorenje mora biti duže od 2 i kraće od 20 znakova")
            return false
        }

        // Provjera dodanih jela
        if (listaJela.isEmpty()) {
            setErrorForListView(jelaLV,"Morate dodati barem jedno jelo")
            return false
        }

        // Provjera odabranih vrijednosti u ListView-ovima
        if (medicinskaKoristLV.checkedItemCount == 0 || klimatskiTipLV.checkedItemCount == 0 || zemljisniTipLV.checkedItemCount == 0 || profilOkusaLV.checkedItemPosition == -1) {
            // Ako nije odabrana niti jedna stavka u jednom od ListView-ova
            if (medicinskaKoristLV.checkedItemCount == 0) {
                setErrorForListView(medicinskaKoristLV, "Morate odabrati barem jednu medicinsku korist")
            }
            if (klimatskiTipLV.checkedItemCount == 0) {
                setErrorForListView(klimatskiTipLV, "Morate odabrati barem jedan klimatski tip")
            }
            if (zemljisniTipLV.checkedItemCount == 0) {
                setErrorForListView(zemljisniTipLV, "Morate odabrati barem jedan zemljisni tip")
            }
            if (profilOkusaLV.checkedItemPosition == -1) {
                setErrorForListView(profilOkusaLV, "Morate odabrati jedan profil okusa")
            }
            return false
        }

        // Svi uvjeti su zadovoljeni
        return true
    }

    private fun setErrorForListView(listView: ListView, message: String) {
        // Prikaži Toast s greškom
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun podaci() {
        val selectovanaKorist = mutableListOf<MedicinskaKorist>()
        for (i in 0 until medicinskaKoristLV.count)
            if (medicinskaKoristLV.isItemChecked(i)) {
                val medicalUseEnum =
                    MedicinskaKorist.entries.find { it.opis == medicinskaKoristLV.getItemAtPosition(i)}
                medicalUseEnum?.let(selectovanaKorist::add)
            }

        val selectovaniOkus = profilOkusaLV.checkedItemPosition.takeIf { it != ListView.INVALID_POSITION }?.let {
            val tasteProfileString = profilOkusaLV.getItemAtPosition(it) as String
            ProfilOkusaBiljke.entries.find { it.opis == tasteProfileString }
        } ?: ProfilOkusaBiljke.SLATKI


        val selectovanaKlima = mutableListOf<KlimatskiTip>()
        for (i in 0 until klimatskiTipLV.count)
            if (klimatskiTipLV.isItemChecked(i)) {
                val climateTypeEnum = KlimatskiTip.entries.find { it.opis == klimatskiTipLV.getItemAtPosition(i)}
                climateTypeEnum?.let(selectovanaKlima::add)
            }

        val selectovanoZemljiste = mutableListOf<Zemljiste>()
        for (i in 0 until zemljisniTipLV.count)
            if (zemljisniTipLV.isItemChecked(i)) {
                val soilTypeEnum = Zemljiste.entries.find { it.naziv == zemljisniTipLV.getItemAtPosition(i) }
                soilTypeEnum?.let(selectovanoZemljiste::add)
            }

            var newPlant = Biljka(
                naziv = nazivET.text.toString(),
                porodica = porodicaET.text.toString(),
                medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString(),
                medicinskeKoristi = selectovanaKorist,
                profilOkusa = selectovaniOkus,
                jela = listaJela,
                klimatskiTipovi = selectovanaKlima,
                zemljisniTipovi = selectovanoZemljiste,
            )

            CoroutineScope(Dispatchers.IO).launch {
                newPlant = TrefleDAO().fixData(newPlant)
                baza.saveBiljka(newPlant)

                val id:Long? = baza.getPlantIdByName(newPlant.naziv)

                var image = TrefleDAO().getImage(newPlant)
                baza.addImage(id!!, image)

                setResult(Activity.RESULT_OK)
                finish()
            }

    }


    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission has already been granted
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start camera intent
                dispatchTakePictureIntent()
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(
                    this,
                    "Camera permission denied. Cannot open camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            slika.setImageBitmap(imageBitmap)
        }
    }

}
