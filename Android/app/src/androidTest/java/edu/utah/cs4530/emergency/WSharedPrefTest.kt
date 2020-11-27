package edu.utah.cs4530.emergency

import androidx.test.ext.junit.runners.AndroidJUnit4
import edu.utah.cs4530.emergency.util.WSharedPref
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class WSharedPrefTest
{
    val TEST_KEY_STRING = "TEST_KEY_STRING"
    val TEST_KEY_INT = "TEST_KEY_INT"
    val TEST_KEY_BOOLEAN = "TEST_KEY_BOOLEAN"

    @Before
    fun clean()
    {
        WSharedPref.setString(TEST_KEY_STRING, null)
        WSharedPref.setInt(TEST_KEY_INT, null)
        WSharedPref.setBoolean(TEST_KEY_BOOLEAN, null)
    }

    @Test
    fun stringSaveAndRead()
    {
        //Nothing was saved
        assertEquals("NULL", WSharedPref.getString(TEST_KEY_STRING, "NULL"))

        //After saved
        WSharedPref.setString(TEST_KEY_STRING, "HELLO WORLD")
        assertEquals("HELLO WORLD", WSharedPref.getString(TEST_KEY_STRING, "NULL"))

        //After deleted
        WSharedPref.setString(TEST_KEY_STRING, null)
        assertEquals("NULL", WSharedPref.getString(TEST_KEY_STRING, "NULL"))
    }

    @Test
    fun intSaveAndRead()
    {
        //Nothing was saved
        assertEquals(-1, WSharedPref.getInt(TEST_KEY_INT, -1))

        //After saved
        WSharedPref.setInt(TEST_KEY_INT, 1)
        assertEquals(1, WSharedPref.getInt(TEST_KEY_INT, -1))

        //After deleted
        WSharedPref.setInt(TEST_KEY_INT, null)
        assertEquals(-1, WSharedPref.getInt(TEST_KEY_INT, -1))
    }

    @Test
    fun booleanSaveAndRead()
    {
        //Nothing was saved
        assertEquals(false, WSharedPref.getBoolean(TEST_KEY_BOOLEAN, false))

        //After saved
        WSharedPref.setBoolean(TEST_KEY_BOOLEAN, true)
        assertEquals(true, WSharedPref.getBoolean(TEST_KEY_BOOLEAN, false))

        //After deleted
        WSharedPref.setBoolean(TEST_KEY_BOOLEAN, null)
        assertEquals(false, WSharedPref.getBoolean(TEST_KEY_BOOLEAN, false))
    }
}