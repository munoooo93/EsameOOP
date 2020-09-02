# EsameOOP

Scopo di questo progetto è la creazione di una REST API che consenta di:

* Ottenere **metadati** delle sottocartelle e dei file presenti in una cartella Dropbox.
* Verificare la presenza di elementi cancellati (con relative dimensioni).
* Compilare statistiche per **dimensione** e **tipo** riguardanti ogni cartella.

L'acronimo REST sta per "**RE**presentational **S**tate **T**ransfer",  definisce delle linee guida da seguire durante lo sviluppo delle API per la gestione delle risorse remote ed indica come mappare le operazioni CRUD (**C**reate, **R**etrieve, **U**pdate, **D**elete) da eseguire sul dato, tramite i 4 metodi messi a disposizione dal protocollo HTTP: **GET**, **POST**, **PUT**, **DELETE**.

## Percorsi API

L'applicazione mette a disposizione 5 rotte distinte. In caso di errore viene restituita una **lista vuota**. Viene sempre restituito un **JSON**, fatta eccezione per il frontend dove viene restituito un **HTML**.

>**POST** */data*

Richiede un body con un solo parametro: "**path**: /**Nomecartella**/", contenente l'indirizzo della cartella da analizzare. Se assente, considera come default la cartella */root* . Per ogni file elenca: 

* Il nome del percorso.
* Estensione.
* Dimensione in byte.
* Se è stato cancellato.
* Se è scaricabile.

>**GET** */metadata*

Descrive la tipologia dei dati che vengono restituiti da */data*.

>**GET** */stats/overall*

Permette di eseguire un'analisi statistica sulla cartella, elenca:

* Estensione file.
* Il numero dei file con quell'estensione.
* Dimensione mininima.
* Dimensione massima.
* Dimensione media.

Il parametro opzionale */overall* permette di includere i file cancellati nell'analisi statistica. Vengono prese in esame **tutte** le cartelle presenti.

>**POST** */stats*

Richiede un body con 2 parametri: "**path**", "**include-deleted**". Il primo indica la cartella in cui cercare, il secondo indica se includere  i file eliminati nelle statistiche. É una richiesta simile alla precedente ma specifica ad una cartella. Se non vengono inclusi i parametri, i valori di default diventano: *"path":"/root"*, *"include-deleted":false*.

>/

Percorso per il **frontend**.

### Le chiamate

Ricapitolando:
| **Metodo** | **Richiesta** | **Risposta** |
| :--- | :---: | ---: |
| POST  | /data          | JSON |
| GET   | /metadata      | JSON |
| GET   | /stats/overall | JSON |
| POST  | /stats         | JSON |
| GET   | /              | HTML |

Per il testing è stato creato un account di prova (con relativo **token** per l'accesso) dove abbiamo una cartella così composta:

![Foldlist2.png]()

#### */data*

Un esempio di body:

```json

{
    "path": "/Cose importanti/"
}

```

La cui risposta sarà:

```json
[
 {
     "ext":"cpp",
     "path":"/Cose importanti/port-scan.cpp",
     "downloadable":true,
     "deleted":true,
     "size":1895,
     "name":"port-scan.cpp"
 },
 {
     "ext":"unknown",
     "path":"/Cose importanti/port-scan",
     "downloadable":true,
     "deleted":true,
     "size":18672,
     "name":"port-scan"
 },
 {
     "ext":"unknown",
     "path":"/Cose importanti/makefile",
     "downloadable":true,
     "deleted":true,
     "size":68,
     "name":"makefile"
 },
 {
     "ext":"png","path":"/Cose importanti/Immagini importanti/albero.png",
     "downloadable":true,
     "deleted":false,
     "size":89574,
     "name":"albero.png"
 },
 {
     "ext":"png","path":"/Cose importanti/Immagini importanti/bradipo.png",
     "downloadable":true,
     "deleted":false,
     "size":268231,
     "name":"bradipo.png"
 },
 {
     "ext":"jpeg",
     "path":"/Cose importanti/Immagini importanti/baking.jpeg",
     "downloadable":true,
     "deleted":false,
     "size":244904,
     "name":"baking.jpeg"
 }
 {
     "ext":"jpeg",
     "path":"/Cose importanti/Immagini importanti/pipboy.jpeg",
     "downloadable":true,
     "deleted":false,
     "size":32075,
     "name":"pipboy.jpeg"
 }
 {
     "ext":"pdf",
     "path":"/Cose importanti/char_sheets_3.5.pdf",
     "downloadable":true,
     "deleted":false,"size":1505985,
     "name":"char_sheets_3.5.pdf"
 }
 {
     "ext":"mp3",
     "path":"/Cose importanti/Darude - Sandstorm.mp3",
     "downloadable":true,
     "deleted":false,
     "size":4745461,
     "name":"Darude - Sandstorm.mp3"
 }
 {
     "ext":"pdf",
     "path":"/Cose importanti/Doc acquisito.pdf",
     "downloadable":true,
     "deleted":false,
     "size":679657,
     "name":"Doc acquisito.pdf"
 }
 {
     "ext":"unknown",
     "path":"/Cose importanti/What's song is this?",
     "downloadable":true,
     "deleted":false,
     "size":25,
     "name":"What's song is this?"
 }
]

```

#### */metadata*

La risposta, in questo caso sarà:

```json
[
 {
     "sourcefield":"name",
     "type":"string",
 },
 {
     "sourcefield":"path",
     "type":"string"
 },
 {
     "sourcefield":"ext",
     "type":"string"
 },
 {
     "sourcefield":"size",
     "type":"integer"
 },
 {
     "sourcefield":"deleted",
     "type":"boolean"
 },
 {
     "sourcefield":"downloadable",
     "type":"boolean"
 }
]

```

#### */stats/overall*

Avremo una risposta così composta:

```json
[
 {
     "ext":"paper",
     "count":1,
     "max-size":200,
     "avg-size":200,
     "min-size":200
 },
 {
     "ext":"png",
     "count":2,
     "max-size":268231,
     "avg-size":178902,
     "min-size":89574
 },
 {
     "ext":"jpeg",
     "count":2,
     "max-size":244904,
     "avg-size":138489,
     "min-size":32075
 },
 {
     "ext":"pdf",
     "count":2,
     "max-size":1505985,
     "avg-size":1092821,
     "min-size":679657
 },
 {
     "ext":"mp3",
     "count":1,
     "max-size":4745461,
     "avg-size":4745461,
     "min-size":4745461
 },
 {
     "ext":"unknown",
     "count":1,
     "max-size":25,
     "avg-size":25,
     "min-size":25
 }
]

```

#### */stats*

Un esempio di body:

```json
{
    "path":"/Cose importanti/",
    "include-deleted":false
}

```

Come risposta otterremo:

```json
[
 {
     "ext":"pdf",
     "count":2,
     "max-size":1505985,
     "avg-size":1092821,
     "min-size":679657
 },
 {
     "ext":"mp3",
     "count":1,
     "max-size":4745461,
     "avg-size":4745461,
     "min-size":4745461
 },
 {
     "ext":"unknown",
     "count":1,
     "max-size":25,
     "avg-size":25,
     "min-size":25
 }
]
```

### Il frontend

![Home.png]()

Tramite un frontend è possibile interfacciarsi con le API ed effettuare le varie richieste disponibili. È presente una casella di testo per inserire il percorso da ricercare, ove possibile.

![Immagini importanti.png]()

Nell'immagine, viene eseguita una ricerca nella cartella "Immagini importanti", contenuta in "Cose importanti"(tramite **"Ottieni i dati"**, richiamando POST /data). Seguono altri screenshot che illustrano il comportamento delle rispettive chiamate.

![Metadati.png]()

**"Ottieni metadati"** = GET /metadata

![Stats cartella.png]()

**"Ottieni statistiche cartella"** = POST /stats

![Tutte le stats.png]()

**"Ottieni tutte le statistiche"** = GET /stats/overall

Tramite una checkbox è possibile includere i file cancellati nelle statistiche.

## UML

L'UML (**U**nified **M**odeling **L**anguage) é un linguaggio di modellazione che si ispira al paradigma orientato ad oggetti per la rappresentazione e la visualizzazione del codice e delle sue specifiche. Attraverso dei **diagrammi** aiuta a definire in maniera chiara ed univoca i processi che regolano la concettualizzazione e la costruzione di un progetto.

### Class Diagram

Il **diagramma delle classi** rappresentano, a vari livelli di astrazione, il **contesto** in cui un sistema software deve operare, è utile anche nella progettazione delle parti che lo compongono e delle loro relazioni.

### Use Case Diagram

Il **diagramma dei casi d'uso**, tramite l'utilizzo di **attori**, descrive le funzioni o i servizi messi a disposizione dal sistema.

![IMM UML]()

### Sequence Diagram

Il **diagramma di sequenza** individua relazioni, in particolare i **messaggi** che intercorrono tra le entità rappresentate.

#### */data*

![IMM SEQ /DATA]()

#### */metadata*

![IMM SEQ /METADATA]()

#### */stats/overall*

![IMM SEQ /STATS7OVERALL]()

#### */stats*

![IMM SEQ /STATS]()

## Software Utilizzati

* [SpringBoot](https://spring.io/projects/spring-boot) - Framework per applicazioni Java.
* [Eclipse](https://www.eclipse.org/) - IDE per linguaggio Java.
* [Visual Studio Code](https://code.visualstudio.com/) - Frontend.

## Reference API

* **Dropbox:** [list folder](https://www.dropbox.com/developers/documentation/http/documentation#files-list_folder), [list revisions](https://www.dropbox.com/developers/documentation/http/documentation#files-list_revisions)

## Autore

* **Emanueale Ballarini** - [GitHub](https://github.com/munoooo93)