# Repository per il corso di Prova Finale di Ingegneria del Software.

### Docente di riferimento: Alessandro Margara

### Membri del gruppo: Matteo Colombi, Andrea Colombo, Leonardo Carlo Conti, Lorenzo Demontis

## Scopo del progetto 
Implementazione tramite Java del gioco Codex Naturalis (di Cranio Creations) tramite interfaccia grafica e linea di comando. Intendiamo realizzare tre funzioni avanzate:

* **Partite multiple:** Il server è realizzato in modo che possa gestire più partite contemporaneamente. In fase di ingresso, ai giocatori sarà consentito di scegliere a quale partita aperta e non ancora iniziata collegarsi o creare una nuova partita.
* **Persistenza:** Il server fa in modo di salvare periodicamente lo stato della partita su disco, in modo che l'esecuzione possa riprendere da dove si è interrotta anche a seguito del crash del server stesso. Per riprendere una partita, i giocatori si dovranno ricollegare al server utilizzando gli stessi nickname una volta che questo sia tornato attivo. Si assume che il disco della macchina su cui gira il server costituisca una memoria totalmente affidabile.
* **Chat:** Client e server permettono ai giocatori di chattare fra di loro durante la partita, tramite messaggi indirizzati a tutta la lobby o a un singolo giocatore


