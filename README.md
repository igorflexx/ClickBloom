# ClickBloom

Моя первая попытка создать Android-проект на Kotlin и Jetpack Compose.

Внутри:

- счётчик кликов
- плавная анимация числа
- анимированная кнопка
- звук клика
- кнопка сброса

## APK

Готовый файл для установки лежит отдельно в репозитории:

- `apk/ClickBloom-debug.apk`

Его можно скачать и установить на Android-устройство вручную.

## Как запустить проект в Android Studio

1. Открой Android Studio.
2. Нажми `Open` и выбери папку `/Users/a1/Projects/ClickBloom`.
3. Дождись `Gradle Sync`.
4. Если Android Studio попросит установить SDK, открой `SDK Manager` и установи нужные компоненты.
5. Открой `Device Manager`, создай виртуальный телефон и запусти его.
6. Нажми зелёную кнопку `Run`.

## Как собрать APK вручную

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

cd /Users/a1/Projects/ClickBloom
./gradlew assembleDebug
```

После сборки файл появляется в `app/build/outputs/apk/debug/app-debug.apk`.
