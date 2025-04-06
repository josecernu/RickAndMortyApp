package com.josecernu.rickandmortyapp.data.domain.util.extension

import junit.framework.TestCase
import org.junit.Test
import java.util.Locale

class StringExtensionTest {
    @Test
    fun `toDate when return date`() {
        val dateSrt = "2023-11-24T13:50:54.025Z"
        val result = dateSrt.toDate()
        TestCase.assertNotNull(result)
    }

    @Test
    fun `toDate when return null`() {
        val dateSrt = ""
        val result = dateSrt.toDate()
        TestCase.assertNull(result)
    }

    @Test
    fun `formated success`() {
        Locale.setDefault(Locale.ENGLISH)
        val date = "04 Jun 1993".toDateFormat(DATE_FORMAT, Locale.ENGLISH)
        TestCase.assertEquals(date, "04 JUN 1993")
    }

    @Test
    fun `formated fail`() {
        val date = "error".toDateFormat()
        TestCase.assertEquals(date, "error")
    }

    @Test
    fun `capitalize first letter succes if all are uppercase`() {
        val result = "FEB".capitalizeFirstLetter()
        TestCase.assertEquals("Feb", result)
    }

    @Test
    fun `capitalize first letter succes if all are lowercase`() {
        val result = "feb".capitalizeFirstLetter()
        TestCase.assertEquals("Feb", result)
    }

    @Test
    fun `capitalize first letter succes if only has one letter and lowercase`() {
        val result = "f".capitalizeFirstLetter()
        TestCase.assertEquals("F", result)
    }

    @Test
    fun `capitalize first letter succes if only has one letter and uppercase`() {
        val result = "F".capitalizeFirstLetter()
        TestCase.assertEquals("F", result)
    }

    @Test
    fun `capitalize first letter succes if only has one letter and uppercase with numbers`() {
        val result = "23 F".capitalizeFirstLetter()
        TestCase.assertEquals("23 f", result)
    }

    @Test
    fun `capitalize first letter succes if empty String`() {
        val result = "".capitalizeFirstLetter()
        TestCase.assertEquals("", result)
    }

    @Test
    fun `capitalizeFirstLetter should handle mixed case word`() {
        val input = "hElLo"
        val expected = "Hello"
        TestCase.assertEquals(expected, input.capitalizeFirstLetter())
    }

    @Test
    fun `capitalizeDate first letter succes if only has one letter and uppercase2`() {
        val result = "23 F".capitalizeDate()
        TestCase.assertEquals("23 F", result)
    }
}
