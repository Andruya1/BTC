# BTC Price Widget (Android)

Widget minimalista que muestra **BTC $precio** sin decimales. Actualiza de forma periódica y al tocar el widget.

## Características
- Texto minimalista: `BTC $12345`
- Tap para actualizar al instante.
- Configuración: intervalo (10/15/30/60 min), color (blanco/negro) y modo 10 min (exact alarm).
- Manejo de errores: si falla la red, mantiene el último precio y reintenta.
- Compatible con Widgets en pantalla de inicio. En Samsung, la presencia en la pantalla de bloqueo depende de One UI del dispositivo. En Android 16+ (QPR1) se esperan widgets en lock screen compatibles.

## Límite de Android sobre intervalos
- **WorkManager** permite intervalos mínimos de 15 min. (Play-compliant).
- Para **10 min**, se ofrece un modo opcional con **AlarmManager exacto**. Requiere que el usuario habilite *Alarmas y recordatorios* (SCHEDULE_EXACT_ALARM). Este modo puede consumir más batería.

## API de precio
- Fuente: CoinGecko `/simple/price`. Sin API key. Se redondea a entero.

## Cómo compilar
1. Abrir en Android Studio Iguana/ Jellyfish o superior.
2. Sincronizar Gradle.
3. Ejecutar en un dispositivo físico o emulador con Google APIs.

## Permisos
- `INTERNET`
- `RECEIVE_BOOT_COMPLETED` para reprogramar al reiniciar.
- `SCHEDULE_EXACT_ALARM` (opcional) para 10 min.
- `POST_NOTIFICATIONS` solo si se habilita un servicio en primer plano (no usado por defecto).

## Notas de Lock Screen (Samsung / Android 16)
- Samsung One UI 6.1+ admite widgets en lock screen principalmente de apps de Samsung.
- One UI 8 (Android 16) y Android 16 QPR1 incorporan soporte más amplio para widgets de terceros.
- Este proyecto usa `android:widgetCategory="home_screen|keyguard"`. La disponibilidad efectiva depende del firmware.

## Estructura
- `widget/` Provider del AppWidget.
- `workers/` WorkManager para actualizaciones periódicas.
- `schedule/` AlarmManager exacto opcional.
- `ui/ConfigurationActivity` Configuración inicial y ajustes.

