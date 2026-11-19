## 🎮 SURVIVAL RPG

**SURVIVAL RPG** è un gioco di ruolo (RPG) esplorativo e gestionale realizzato in **Java 25** con interfaccia grafica **JavaFX**. Il giocatore veste i panni di un sopravvissuto a una catastrofe che deve riuscire a sopravvivere in mezzo al mare, gestendo la sua energia, procacciandosi le risorse primarie, come cibo e acqua, riuscendo a sopravvivere fin quando non si riesce a creare dei remi per poter sbarcare su un'isola. Lo stato della partita può essere salvato e ripreso in qualsiasi momento grazie alla persistenza su file in formato **JSON** 

## 🎲 Come eseguire il progetto

### Prerequisiti
* **Java 25** (LTS) o superiore
* **Gradle** (nel progetto è incluso il wrapper `./gradlew`, quindi non necessita di un'installazione separata)

### 📖 Istruzioni
```bash
git clone https://github.com/LorenzoPalazzi/Progetto-MGC-Lorenzo-Palazzi-131056.git
cd Progetto-MGC-Lorenzo-Palazzi-131056
```
### 🧱 Build del progetto

```bash
./gradlew build
```

### 🚀 Esecuzione

```bash
./gradlew run
```
##
## 🏛️ Architettura del Software
Il codice del gioco separa la logica di sopravvivenza dall'interfaccia, seguendo il pattern **MVC (Model View Controller)** e segue i principi **SOLID**, il tutto organizzato all'interno del package ```it.unicam.cs.mpgc.rpg131056```:
* **`model`** — Contiene le entità di dominio e lo stato del giocatore (`Player`, `Inventory`), le condizioni ambientali e le varie modalità della partita (`Weather`, `Modalita`) e la gerarchia polimorfica degli oggetti (`Item`, `AbstractItem`, `Food`, `Water`, `Material`, `ItemType`) con il relativo adattatore per la serializzazione (`ItemAdapter`). È completamente indipendente dall'interfaccia grafica.
* **`view`** — Gestisce l'interfaccia grafica realizzata in JavaFX (`StartMenuView`, `GameView`, `GameApp`) e l'avvio dell'applicazione (`AppLauncher`), limitandosi a mostrare lo stato del personaggio e a inoltrare i comandi dell'utente al controller.
* **`controller`** — Coordina la logica applicativa e l'avanzamento della partita tramite `GameController` e `GameState`, fungendo da intermediario tra la vista, le azioni del giocatore e il salvataggio.
* **`actions`** — Incapsula ogni singola azione di sopravvivenza in una classe dedicata che implementa l'interfaccia comune `Action` (`Fish`, `FishingRod`, `CookingFish`, `Eat`, `CollectWater`, `Drink`, `Scavenge`, `BuildOars`, `Rest`), rendendo il sistema facilmente estendibile con nuove meccaniche senza modificare il codice esistente.
* **`persistence`** — Gestisce il salvataggio e il caricamento della partita tramite l'interfaccia `SaveManager` e la sua implementazione basata su Gson (`JsonSaveManager`), disaccoppiando la logica di gioco dal formato di archiviazione.
> 👉 Per approfondire il ruolo delle singole classi, la gestione dei salvataggi e le possibilità di estensione futura, consulta la **Wiki** del repository.

## 🤖 Tecnologie AI
Durante lo sviluppo del progetto sono stati utilizzati strumenti di intelligenza artificiale come supporto:
* **Revisione dell'architettura software** — analisi e miglioramento della separazione delle responsabilità tra i package (`model`, `actions`, `controller`, `view`, `persistence`) in linea con il pattern MVC e i principi SOLID.
* **Debugging** — supporto nell'individuazione e nella risoluzione di errori di compilazione, gestione della serializzazione JSON e anomalie nella logica di gioco.
* **Configurazione dell'ambiente di build** — assistenza nell'aggiornamento e nell'allineamento di `build.gradle.kts` e del Gradle Wrapper per **Java 25**, **JavaFX 25** e **Gradle 9.1.0**.
* **Stesura della documentazione** — supporto nella redazione, organizzazione e formattazione del `README.md` e della Wiki del repository.

**Ogni proposta fornita dall'AI — riguardante frammenti di codice, soluzioni tecniche o migliorie — è stata attentamente revisionata e validata da me prima di essere integrata. L'intelligenza artificiale ha svolto quindi un ruolo prettamente ausiliario, mentre io ho sempre mantenuto il pieno controllo sulla progettazione e sull'implementazione del progetto.**

> 👉 Per una descrizione più dettagliata delle modalità di utilizzo dell'AI nel progetto, consulta la **Wiki** del repository.

## ⚙️ Tecnologie Utilizzate
* **Java 25** — linguaggio di programmazione principale
* **JavaFX 25** — interfaccia grafica (`javafx.controls`)
* **Gradle 9.1.0** — build automation e gestione delle dipendenze
* **Gson 2.10.1** — serializzazione e deserializzazione JSON per la persistenza
* **JUnit 5** — testing automatico della logica di gioco
