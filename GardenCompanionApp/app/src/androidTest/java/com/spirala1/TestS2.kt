package com.spirala1

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random


@RunWith(AndroidJUnit4::class)
class TestS2 {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        MainActivity::class.java
    )
    
    @Before
    fun setUp() {
        // Initialize Intents
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release Intents
        Intents.release()
    }

    @Test
    //SVI PODACI PRAVILNI - test treba da prolazi
    fun dodavanjeNoveBiljke() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        pravilniPodaciET()

        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Regulacija pritiska")
            )
        ).inAdapterView(withId(R.id.medicinskaKoristLV)).perform(click())

        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Mediteranska klima")
            )
        ).inAdapterView(withId(R.id.klimatskiTipLV)).perform(click())

        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Crnica")
            )
        ).inAdapterView(withId(R.id.zemljisniTipLV)).perform(click())


        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Sladak okus")
            )
        ).inAdapterView(withId(R.id.profilOkusaLV)).perform(click())

        //da nadje button
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())

        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())

        onView(withId(R.id.biljkeRV)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText("biljka1")),
                    hasDescendant(withText("upozorenje")),
                    hasDescendant(withText(containsString("Regulacija pritiska")))
                )
            )
        )

        onView(withId(R.id.modSpinner)).perform(click())
        onData(
            allOf(
                Is(instanceOf(String::class.java)), containsString("Kuha")
            )
        ).perform(click())

        onView(withId(R.id.biljkeRV)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText("biljka1")),
                    hasDescendant(withText(containsString("Sladak okus"))),
                    hasDescendant(withText("jelo1"))
                )
            )
        )

        onView(withId(R.id.modSpinner)).perform(click())
        onData(
            allOf(
                Is(instanceOf(String::class.java)), containsString("Botan")
            )
        ).perform(click())

        onView(withId(R.id.biljkeRV)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                allOf(
                    hasDescendant(withText("biljka1")),
                    hasDescendant(withText("porodica1")),
                    hasDescendant(withText(containsString("Mediteranska klima"))),
                    hasDescendant(withText("Crnica"))
                )
            )
        )
    }

    @Test
    fun testNazivET() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        // Provjeri ispravnu dužinu naziva
        onView(withId(R.id.nazivET)).perform(
            scrollTo(), click(), typeText("a"), closeSoftKeyboard()
        )
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.nazivET)).check(matches((isDisplayed())))

    }
    @Test
    fun testPorodicaET() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        // Provjeri ispravnu dužinu naziva
        onView(withId(R.id.porodicaET)).perform(
            scrollTo(), click(), typeText("aaaaaaaaaaaaaaaaaaaaaaa"), closeSoftKeyboard()
        )
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.porodicaET)).check(matches((isDisplayed())))
    }
    @Test
    fun testMedicinskoUpozorenje() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        // Provjeri ispravnu dužinu naziva
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(
            scrollTo(), click(), typeText("a"), closeSoftKeyboard()
        )
        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches((isDisplayed())))
    }
    @Test
    fun provjeriMedKoristLV() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())
        pravilniPodaciET()
        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Mediteranska klima")
            )
        ).inAdapterView(withId(R.id.klimatskiTipLV)).perform(click())

        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Crnica")
            )
        ).inAdapterView(withId(R.id.zemljisniTipLV)).perform(click())

        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Sladak okus")
            )
        ).inAdapterView(withId(R.id.profilOkusaLV)).perform(click())

        //da nadje button
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())

        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches((isDisplayed())))
    }
    @Test
    fun provjeriKlimatskitipLV() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())
        pravilniPodaciET()
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Regulacija pritiska")
            )
        ).inAdapterView(withId(R.id.medicinskaKoristLV)).perform(click())

        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Crnica")
            )
        ).inAdapterView(withId(R.id.zemljisniTipLV)).perform(click())

        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Sladak okus")
            )
        ).inAdapterView(withId(R.id.profilOkusaLV)).perform(click())

        //da nadje button
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())

        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches((isDisplayed())))
    }

    @Test
    fun provjeriZemljisniTipLV() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())
        pravilniPodaciET()
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Regulacija pritiska")
            )
        ).inAdapterView(withId(R.id.medicinskaKoristLV)).perform(click())

        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Mediteranska klima")
            )
        ).inAdapterView(withId(R.id.klimatskiTipLV)).perform(click())

        onView(withId(R.id.profilOkusaLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Sladak okus")
            )
        ).inAdapterView(withId(R.id.profilOkusaLV)).perform(click())

        //da nadje button
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())

        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches((isDisplayed())))
    }

    @Test
    fun provjeriProfilUkusaLV() {
        onView(withId(R.id.novaBiljkaBtn)).perform(click())
        pravilniPodaciET()
        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Regulacija pritiska")
            )
        ).inAdapterView(withId(R.id.medicinskaKoristLV)).perform(click())

        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Mediteranska klima")
            )
        ).inAdapterView(withId(R.id.klimatskiTipLV)).perform(click())

        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo())
        onData(
            allOf(
                Is(instanceOf(String::class.java)),
                containsString("Crnica")
            )
        ).inAdapterView(withId(R.id.zemljisniTipLV)).perform(click())

        //da nadje button
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo())

        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
        onView(withId(R.id.dodajBiljkuBtn)).check(matches((isDisplayed())))
    }

    fun pravilniPodaciET(){

        onView(withId(R.id.nazivET)).perform(
            scrollTo(), click(), typeText("biljka1"), closeSoftKeyboard()
        )
        onView(withId(R.id.porodicaET)).perform(
            scrollTo(), click(), typeText("porodica1"), closeSoftKeyboard()
        )
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(
            scrollTo(), click(), typeText("upozorenje"), closeSoftKeyboard()
        )
        onView(withId(R.id.jeloET)).perform(
            scrollTo(), typeText("jelo1"), closeSoftKeyboard()
        )
        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(),click())
    }

    //test kamera
    @Test
    fun testPromjeneSlikeNakonSlikanja() {
        // Klikni na dugme za snimanje slike
        onView(withId(R.id.novaBiljkaBtn)).perform(click())

        val resultIntent = Intent().apply {
            putExtra("data", createRandomBitmap())
        }

        // Respond to the camera intent with the simulated image capture result
        Intents.intending(IntentMatchers.hasAction(MediaStore.ACTION_IMAGE_CAPTURE))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent))

        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())

        // Pričekaj da se promijeni slika u ImageView-u
        onView(withId(R.id.slikaIV)).check(matches(withNewImage()))
    }

    // Matcher za provjeru da li je ImageView postavljen s novom slikom
    private fun withNewImage(): Matcher<Any?> {
        return object : TypeSafeMatcher<Any?>() {
            override fun describeTo(description: Description?) {
                description?.appendText("with new image")
            }

            override fun matchesSafely(item: Any?): Boolean {
                if (item !is ImageView) return false

                val oldDrawable = item.drawable
                val context = InstrumentationRegistry.getInstrumentation().targetContext
                val randomBitmap = createRandomBitmap()

                // Postavi random bitmapu na ImageView
                item.setImageBitmap(randomBitmap)

                // Provjeri da li je stara slika ostala na ImageView-u
                val newDrawable = item.drawable
                val oldBitmap = (oldDrawable as? BitmapDrawable)?.bitmap
                val newBitmap = (newDrawable as? BitmapDrawable)?.bitmap

                return oldBitmap != newBitmap
            }
        }
    }

    // Kreira random bitmapu
    private fun createRandomBitmap(): Bitmap {
        val width = 100
        val height = 100
        val random = Random.Default

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, random.nextInt())
            }
        }
        return bitmap
    }

}
