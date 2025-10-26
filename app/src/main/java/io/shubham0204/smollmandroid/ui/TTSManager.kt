/*
 * Copyright (C) 2024 The Elder App
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shubham0204.smollmandroid.ui

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TTSManager(
    private val context: Context,
    private val onInitialized: (Boolean) -> Unit
) {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    var isTTSEnabled = true

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTSManager", "Language not supported")
                    isInitialized = false
                    onInitialized(false)
                } else {
                    isInitialized = true
                    textToSpeech?.setPitch(1.0f)
                    textToSpeech?.setSpeechRate(1.0f)
                    setupUtteranceListener()
                    onInitialized(true)
                    Log.d("TTSManager", "TTS initialized successfully")
                }
            } else {
                Log.e("TTSManager", "TTS initialization failed")
                isInitialized = false
                onInitialized(false)
            }
        }
    }

    private fun setupUtteranceListener() {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d("TTSManager", "TTS started speaking: $utteranceId")
            }

            override fun onDone(utteranceId: String?) {
                Log.d("TTSManager", "TTS finished speaking: $utteranceId")
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Log.e("TTSManager", "TTS error: $utteranceId")
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                Log.e("TTSManager", "TTS error: $utteranceId, code: $errorCode")
            }
        })
    }

    fun speak(text: String) {
        if (!isInitialized) {
            Log.e("TTSManager", "TTS not initialized")
            return
        }

        if (!isTTSEnabled) {
            Log.d("TTSManager", "TTS is disabled")
            return
        }

        if (text.isBlank()) {
            Log.w("TTSManager", "Empty text provided")
            return
        }

        try {
            // Stop any ongoing speech before starting new one
            textToSpeech?.stop()

            // Split long text into chunks if needed (TTS has character limits)
            val chunks = splitTextIntoChunks(text, 4000)
            chunks.forEachIndexed { index, chunk ->
                val queueMode = if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
                textToSpeech?.speak(chunk, queueMode, null, "utterance_$index")
            }

            Log.d("TTSManager", "Speaking ${chunks.size} chunk(s)")
        } catch (e: Exception) {
            Log.e("TTSManager", "Failed to speak", e)
        }
    }

    private fun splitTextIntoChunks(text: String, maxChunkSize: Int): List<String> {
        if (text.length <= maxChunkSize) {
            return listOf(text)
        }

        val chunks = mutableListOf<String>()
        var currentIndex = 0

        while (currentIndex < text.length) {
            val endIndex = minOf(currentIndex + maxChunkSize, text.length)
            var chunkEnd = endIndex

            // Try to break at sentence or word boundary
            if (endIndex < text.length) {
                val sentenceEnd = text.lastIndexOfAny(charArrayOf('.', '!', '?', '\n'), endIndex)
                if (sentenceEnd > currentIndex) {
                    chunkEnd = sentenceEnd + 1
                } else {
                    val wordEnd = text.lastIndexOf(' ', endIndex)
                    if (wordEnd > currentIndex) {
                        chunkEnd = wordEnd
                    }
                }
            }

            chunks.add(text.substring(currentIndex, chunkEnd))
            currentIndex = chunkEnd
        }

        return chunks
    }

    fun stop() {
        textToSpeech?.stop()
    }

    fun isSpeaking(): Boolean {
        return textToSpeech?.isSpeaking ?: false
    }

    fun toggleTTS(): Boolean {
        isTTSEnabled = !isTTSEnabled
        if (!isTTSEnabled) {
            stop()
        }
        return isTTSEnabled
    }

    fun destroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
    }
}
