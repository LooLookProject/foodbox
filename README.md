# 🍽️ Foodbox

## Overview

Foodbox is a tool designed to make it easy for employees to check the daily lunch menu. The lunch vendor posts the menu
on their website as an image, and this project automates the process of sharing that information with everyone. The key
features include:

- **Crawling**: Fetching the daily menu image from the food vendor's website.
- **OCR Processing**: Using Tesseract OCR to extract text from the menu image.
- **Data Structuring**: Analyzing and organizing the menu for each date into a structured format.
- **Notification**: Sending the lunch menu via a Slack bot at the beginning of each workday, so everyone knows what's
  for lunch.
- **Web Interface**: Users can also visit a simple web page to view the lunch menu for any given day.

The goal is to ensure everyone has quick and easy access to the lunch menu, without needing to search for it manually.

## Deployment
```bash
git clone https://github.com/ArgonetDevStudio/foodbox.git
cd foodbox
./gradlew clean build
docker compose up -d
```

## Development Environment Setup

### 1. Install Tesseract

To extract text from menu images, you'll need to install Tesseract OCR.

**MacOS**

```bash
brew install tesseract
```

**Ubuntu**

```bash
sudo apt install tesseract-ocr
sudo apt install libtesseract-dev
```

### 2. Download Traindata

To improve accuracy for Korean text, download `kor.traineddata`
from [tesseract-ocr/tessdata_best](https://github.com/tesseract-ocr/tessdata_best) and place it in your Tesseract data
directory.

### 3. Note for Apple Silicon Users

If you encounter an `UnsatisfiedLinkError` related to `libtesseract.dylib` on macOS with Apple Silicon (M1/M2/M3...),
it's likely because the native Tesseract library cannot be found by the Java application. To resolve this, configure
your application to tell JNA where to find the Tesseract library:

1. **Option A: In Code**
   Add this line before using Tess4J:

   ```java
   System.setProperty("jna.library.path", "/opt/homebrew/opt/tesseract/lib");
   ```

2. **Option B: In Gradle Build File**
   If you're using Gradle, add the following to your `build.gradle`:

   ```groovy
   test {
       systemProperty "jna.library.path", "/opt/homebrew/opt/tesseract/lib"
   }

   bootRun {
       systemProperty "jna.library.path", "/opt/homebrew/opt/tesseract/lib"
   }
   ```

