package com.spirala1

object BiljkeSingletone {
    private var plantsList = mutableListOf<Biljka>()

    /*init {
        plantsList.addAll(getBiljke())
    }*/

    fun getPlants(): MutableList<Biljka> {
        return plantsList
    }

    fun addPlant(plant: Biljka) {
        plantsList.add(plant)
    }
/*
    private fun getBiljke(): List<Biljka> {
        return listOf(
            Biljka(
                naziv = "Bosiljak (Ocimum basilicum)",
                porodica = "Lamiaceae (usnate)",
                medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE
                ),
                profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
                jela = listOf("Salata od paradajza", "Punjene tikvice"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            ), Biljka(
                naziv = "Nana (Mentha spicata)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA
                ),
                profilOkusa = ProfilOkusaBiljke.MENTA,
                jela = listOf("Jogurt sa voćem", "Gulaš"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
                zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
            ), Biljka(
                naziv = "Kamilica (Matricaria chamomilla)",
                porodica = "Asteraceae (glavočike)",
                medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
                medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Čaj od kamilice"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
            ), Biljka(
                naziv = "Ružmarin (Rosmarinus officinalis)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Pečeno pile", "Grah", "Gulaš"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
                zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO, Zemljiste.KRECNJACKO)
            ), Biljka(
                naziv = "Lavanda (Lavandula angustifolia)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Jogurt sa voćem"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
            ), Biljka(
                naziv = "Majčina dušica (Thymus vulgaris)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i osobe sa bolestima bubrega.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.PROTUUPALNO,
                    MedicinskaKorist.PODRSKAIMUNITETU
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Pileći paprikaš", "Rižoto"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            ),
            Biljka(
                naziv = "Peršun (Petroselinum crispum)",
                porodica = "Apiaceae (štitarka)",
                medicinskoUpozorenje = "Osobe alergične na peršun bi trebale izbjegavati konzumaciju.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.REGULACIJAPROBAVE,
                    MedicinskaKorist.PROTUUPALNO
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Paradajz sos", "Salate"),
                klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.GLINENO)
            ),
            Biljka(
                naziv = "Svježi korijander (Coriandrum sativum)",
                porodica = "Apiaceae (štitarka)",
                medicinskoUpozorenje = "Nije preporučljivo za osobe sa niskim krvnim pritiskom.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.PROTIVBOLOVA,
                    MedicinskaKorist.PROTUUPALNO
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Gvakamole", "Tajlandska kuhinja"),
                klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            ),
            Biljka(
                naziv = "Limun trava (Cymbopogon citratus)",
                porodica = "Poaceae (trave)",
                medicinskoUpozorenje = "Nije preporučljivo za osobe sa gastroezofagealnim refluksom.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.SMIRENJE,
                    MedicinskaKorist.REGULACIJAPRITISKA
                ),
                profilOkusa = ProfilOkusaBiljke.CITRUSNI,
                jela = listOf("Tajlandska kuhinja", "Azijska jela"),
                klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
            ),
            Biljka(
                naziv = "Origano (Origanum vulgare)",
                porodica = "Lamiaceae (metvice)",
                medicinskoUpozorenje = "Osobe alergične na peršun bi trebale izbjegavati konzumaciju.",
                medicinskeKoristi = listOf(
                    MedicinskaKorist.PROTUUPALNO,
                    MedicinskaKorist.SMIRENJE
                ),
                profilOkusa = ProfilOkusaBiljke.AROMATICNO,
                jela = listOf("Pizza", "Pasta"),
                klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
                zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.GLINENO)
            )
        )
    }*/
}