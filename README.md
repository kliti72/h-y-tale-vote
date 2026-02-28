# ClaimCommand — Documentazione

## Cos'è

`ClaimCommand` è il comando `/claim` del plugin **HytaleTopVote**. Permette ai player di riscattare la reward dopo aver votato per il server su HytaleTop.

---

## Configurazione (`config.yml`)

Tutti i comportamenti del comando dipendono dalla `Config`. Ecco i campi rilevanti:

| Campo | Tipo | Descrizione |
|---|---|---|
| `is_claim_enabled` | `boolean` | Abilita o disabilita il comando `/claim`. Se `false`, il comando non fa nulla. |
| `disable_claim_message` | `boolean` | Se `true`, non viene inviato nessun messaggio al player in caso di claim riuscito. |
| `error_secret_not_valid_message` | `String` | Messaggio mostrato se il secret API non è valido. |
| `vote_not_found_message` | `String` | Messaggio mostrato se il player non ha mai votato. |
| `vote_time_to_wait_message` | `String` | Messaggio mostrato se il player deve ancora aspettare prima di rivotare. |
| `vote_claim_message` | `String` | Messaggio mostrato quando il claim va a buon fine. |

---

## Placeholder nei messaggi

### `vote_not_found_message`
```
{server_name}   → nome del server su HytaleTop
{server_link}   → link diretto al server (es. https://h-y-tale-server.top/server/123)
{total_votes}   → voti totali del server
{player_name}   → username del player (lowercase)
```

### `vote_time_to_wait_message`
```
{time}   → tempo formattato rimanente prima di poter rivotare (es. "2h 30m")
{player_name}   →  username del player (lowercase)
```

---

## Stati della risposta API (`ClaimResponse.status`)

| Status | Costante | Comportamento |
|---|---|---|
| Secret API non valido | `SECRET_ERROR` | Invia `error_secret_not_valid_message` |
| Player non ha mai votato | `NEVER_VOTED` | Invia `vote_not_found_message` coi placeholder |
| Troppo presto per rivotare | `WAIT` | Invia `vote_time_to_wait_message` con `{time}` |
| Claim riuscito | `SUCCESS` | Invia `vote_claim_message` (se non disabilitato) |

---

## Utilizzo in game

```
/claim
```

Il comando non accetta argomenti. È eseguibile solo da player (estende `AbstractPlayerCommand`).

---

## Note

- Lo username del player viene convertito in **lowercase** prima di essere inviato all'API.
- Gli errori API vengono loggati lato server con prefisso `[CLAIM]` senza mostrare nulla al player.
- Per disabilitare temporaneamente il claim senza rimuovere il comando: impostare `is_claim_enabled: false`.

pkill -f -15 "java.*HytaleServer.jar"