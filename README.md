# Shiny Potato - Sicurezza e Autenticazione

Questo progetto utilizza **Spring Security** per proteggere le risorse e gestire l'autenticazione degli utenti. La configurazione prevede l'uso di autenticazione HTTP Basic e la gestione dei ruoli per differenziare l'accesso a determinati endpoint.

## Accesso e Autenticazione

L'applicazione utilizza **autenticazione HTTP Basic**, quindi per accedere alle risorse protette, l'utente deve fornire il proprio nome utente e la password in un dialogo di autenticazione del browser.

### Endpoint Protetti

- **/venues/**: Questo endpoint è protetto e può essere accessibile solo agli utenti con il ruolo `MANAGER`.
- Altri endpoint richiedono l'autenticazione, ma non ci sono restrizioni sui ruoli per accedervi.

## Ruoli e Utenti

Due utenti predefiniti sono configurati per scopi di test. Gli utenti sono definiti in memoria e i loro dettagli sono i seguenti:

1. **Utente Client (CLIENT)**:
   - Nome utente: `user`
   - Password: `password`
   - Ruolo: `CLIENT`

2. **Utente Manager (MANAGER)**:
   - Nome utente: `admin`
   - Password: `password`
   - Ruolo: `MANAGER`

Questi utenti possono essere utilizzati per effettuare login e testare l'accesso a risorse protette.

### Accesso ai Ruoli

- Gli utenti con il **ruolo `CLIENT`** possono accedere a tutte le risorse, ma non possono accedere agli endpoint protetti da `MANAGER`.
- Gli utenti con il **ruolo `MANAGER`** possono accedere sia agli endpoint generali che a quelli protetti da `/venues/**`.

## Autenticazione HTTP Basic

Quando si accede a un endpoint protetto, il sistema presenterà una finestra di dialogo di autenticazione del browser, dove l'utente dovrà inserire il nome utente e la password.

### Esempio di richiesta di autenticazione

- **Username**: `user` / `admin`
- **Password**: `password`