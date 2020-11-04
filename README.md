## oui
kotlin oui/mac vendor lookup

### why?
i wanted my own mac vendor lookup library as well as some kotlin practice<br>

### features
- [X] cross-platform 
- [X] downloader
- [X] importer for persistence
- [X] lookup
- [X] mac generation

### usage
#### root build.gradle
add as [dependency](https://jitpack.io/#nbdy/oui)<br>
with android you might need to add 'android:usesCleartextTraffic="true"' to the application tag in your manifest.

#### code
```kotlin
// application context to find the storage path
val oui = AndroidOUI(CONTEXT) // automatically downloads and imports 
val entry1 = oui.lookup("A4:45:19:DE:AD:ME") // entry1 now holds an entry with the organizationName "XIAOME"
val entry2 = oui.lookupByOrgName("amazon")
val entry3 = oui.lookupByOrgAddress("somewhere, silicon valley")
```
#### extend
check [here](https://github.com/nbdy/oui/blob/master/app/src/main/java/io/eberlein/oui/AndroidOUI.kt) for an example
