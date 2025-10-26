# The Elder

An AI chat application for Android with **voice input** and **text-to-speech** capabilities. Talk to local AI models running entirely on your device!

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green" alt="Platform">
  <img src="https://img.shields.io/badge/Min%20SDK-26-blue" alt="Min SDK">
  <img src="https://img.shields.io/badge/License-Apache%202.0-orange" alt="License">
</p>

## Features

### Core Features
- 🗣️ **Voice Input**: Speak your questions using the device microphone
- 🔊 **Text-to-Speech**: Listen to AI responses with automatic audio playback
- 🤖 **Local AI Models**: Run GGUF format LLMs entirely on-device
- 💬 **Chat Management**: Organize conversations in folders
- ⚙️ **Model Configuration**: Adjust temperature, context length, and system prompts
- 🌐 **HuggingFace Integration**: Browse and download models directly from the app
- 📱 **Modern UI**: Material Design 3 with Jetpack Compose

### Voice & Audio Features
- **Real-time Speech Recognition**: Convert speech to text instantly
- **Smart TTS**: Automatically reads model responses aloud
- **TTS Toggle**: Easy on/off control for audio playback
- **Listening Indicator**: Visual feedback when recording voice
- **Permission Handling**: Seamless audio permission requests
- **Error Recovery**: Graceful error handling for voice recognition

## Installation

### Download APK
Download the latest APK from [Releases](../../releases)

### Build from Source

#### Prerequisites:
- Android Studio Hedgehog or later
- JDK 17+
- Android SDK API 35
- NDK 27.2.12479018

#### Build Steps:
```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/The_Elder_App.git
cd The_Elder_App

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

## Usage

### Getting Started
1. **Download a Model**: Browse HuggingFace or select a GGUF model
2. **Create a Chat**: Start a new conversation
3. **Choose Input Method**:
   - Type your message, OR
   - Tap the 🎤 microphone button and speak

### Voice Input
1. Tap the **microphone button** (between text field and send button)
2. Grant permission if prompted
3. Speak your question clearly
4. Text appears in the input field
5. Edit if needed, then tap send

### Text-to-Speech
- **Enable/Disable**: Tap the **speaker icon** (left of text field)
  - 🔊 Blue icon = TTS enabled (default)
  - 🔇 Gray icon = TTS disabled
- When enabled, AI responses are automatically spoken aloud
- Responses are read after generation completes

## Supported Models

The Elder supports any GGUF format model, including:
- Qwen 2.5 (0.5B, 1.5B, 3B)
- Llama 3.2 (1B, 3B)
- Phi-3.5 Mini
- Gemma 2 (2B)
- SmolLM (135M, 360M, 1.7B)
- And many more from HuggingFace!

## Technical Details

### Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Dependency Injection**: Koin
- **Database**: Room
- **LLM Engine**: llama.cpp (via NDK)
- **Markdown Rendering**: Markwon
- **Speech Recognition**: Android SpeechRecognizer API
- **Text-to-Speech**: Android TextToSpeech API

### Key Components
- **VoiceInputManager**: Handles speech recognition lifecycle and callbacks
- **TTSManager**: Manages text-to-speech with smart chunking for long texts
- **ChatActivity**: Main chat interface with integrated voice/TTS controls
- **SmolLMManager**: LLM inference management

### Performance
- Models run entirely on-device (no internet required after download)
- Context length configurable per chat
- CPU thread optimization available
- RAM usage displayed in UI

## Permissions

- **INTERNET**: Download models from HuggingFace
- **RECORD_AUDIO**: Voice input functionality

## Documentation

- [Full Modifications Summary](THE_ELDER_MODIFICATIONS.md) - Detailed list of all code changes
- [Original SmolChat README](README_ORIGINAL_SmolChat.md) - Original project documentation

## Troubleshooting

### Voice Input Not Working
- Ensure RECORD_AUDIO permission is granted
- Check device has Google voice input installed
- Try in a quiet environment
- Some devices may require internet for voice recognition

### TTS Not Speaking
- Check device TTS settings (Settings → Accessibility → Text-to-Speech)
- Ensure TTS engine is installed and enabled
- Verify language pack is downloaded
- Check device volume is not muted

### Model Loading Issues
- Ensure model is valid GGUF format
- Check available RAM (models need 2-4GB typically)
- Try reducing context length
- Verify model file wasn't corrupted during download

## Credits

- **Original SmolChat**: Created by [Shubham Panchal](https://github.com/shubham0204/SmolChat-Android)
- **llama.cpp**: [ggerganov/llama.cpp](https://github.com/ggerganov/llama.cpp)
- **Modifications**: Voice input and TTS features added

## License

```
Copyright (C) 2024 The Elder App

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Acknowledgments

Special thanks to:
- Shubham Panchal for the excellent SmolChat foundation
- The llama.cpp community for on-device LLM inference
- All contributors to the open-source Android ecosystem
