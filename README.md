## oui
kotlin oui/mac vendor lookup

### why?
i wanted my own mac vendor lookup library as well as some kotlin practice<br>

### features
- [X] downloader
- [X] importer for persistence
- [X] lookup

### usage
#### root build.gradle
add as [dependency](https://jitpack.io/#smthnspcl/oui)

```kotlin
// application context is needed because of paper db
val oui = OUI(CONTEXT) // automatically downloads and imports 
val entry1 = oui.lookup("A4:45:19:DE:AD:ME") // entry1 now holds an entry with the organizationName "XIAOME"
val entry2 = oui.lookupByOrgName("amazon")
```
