package com.simplemobiletools.camera.activities

import org.junit.Assert.*

class MainActivityTest {

    @org.junit.Test
    fun helloWorldReturnsGenericMessage() {
        val mainActivity = MainActivity()
        assertEquals("Hello, World!", mainActivity.helloWorld())
    }
    @org.junit.Test
    fun incorrectHelloWorldReturnsGenericMessage() {
        val mainActivity = MainActivity()
        assertEquals("Hello, X World!", mainActivity.helloWorld())
    }

}
