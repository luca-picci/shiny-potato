# Shiny Potato - Sicurezza e Autenticazione

Questo progetto utilizza **Spring Security** con **JSON Web Token (JWT)** per proteggere le risorse e gestire l'autenticazione degli utenti. La configurazione include la gestione dei ruoli per differenziare l'accesso a determinati endpoint.

## Accesso e Autenticazione

L'applicazione utilizza **autenticazione tramite token JWT**, quindi per accedere alle risorse protette, l'utente deve autenticarsi e ottenere un token JWT. Questo token deve essere incluso nell'header delle richieste successive.

### Endpoint Protetti

- **/venues/**: Questo endpoint è protetto e può essere accessibile solo agli utenti con il ruolo `MANAGER`.
- **POST /events**, **PUT /events/{id}**, e **DELETE /events/{id}**: Questi endpoint sono accessibili solo ai `MANAGER`.
- **GET /events**: Accessibile a tutti gli utenti autenticati, indipendentemente dal ruolo.

### Autenticazione JWT

Gli utenti devono autenticarsi utilizzando l'endpoint `/auth/login`, che restituisce un token JWT in caso di credenziali valide. Il token deve essere incluso nell'header `Authorization` di tutte le richieste successive:

### Endpoint di Autenticazione

- **POST /auth/register**: Permette la registrazione di un nuovo utente (ruolo predefinito: `CLIENT`).
- **POST /auth/login**: Permette l'autenticazione di un utente esistente e restituisce un token JWT.

### Esempio di Flusso di Autenticazione

1. **Registrazione**: Un nuovo utente si registra tramite `/auth/register`.
2. **Login**: L'utente effettua il login tramite `/auth/login` e riceve un token JWT.
3. **Accesso alle risorse**: L'utente include il token JWT nell'header delle richieste per accedere agli endpoint protetti.

## Ruoli e Utenti

Due ruoli principali sono definiti nel sistema:

1. **CLIENT**: Utenti regolari con accesso limitato.
2. **MANAGER**: Amministratori con accesso completo alle risorse protette.

### Utenti di Test nel Database

1. **Utente Client (CLIENT)**:
   - **Nome**: Giulia
   - **Email**: `giulia@example.com`
   - **Password**: `password1` (La password è crittografata, quindi non può essere visualizzata direttamente.)
   - **Ruolo**: CLIENT

2. **Utente Manager (MANAGER)**:
   - **Nome**: Lorenzo
   - **Email**: `lorenzo@example.com`
   - **Password**: `password2` (La password è crittografata, quindi non può essere visualizzata direttamente.)
   - **Ruolo**: MANAGER

Questi utenti possono essere utilizzati per effettuare login e testare l'accesso a risorse protette.

## Configurazione del Token JWT

Il token JWT è firmato utilizzando una chiave segreta definita nel backend. Il token include:
- L'email dell'utente come soggetto (`sub`).
- La data di creazione (`iat`) e la data di scadenza (`exp`).

I token scadono dopo 1 ora.

### Validazione del Token

I token sono convalidati automaticamente tramite un filtro personalizzato (`JwtAuthenticationFilter`), che verifica:
- La firma del token.
- La scadenza del token.
- La corrispondenza tra l'email del token e l'utente autenticato.

### Gestione degli Errori

Se un token è mancante, non valido o scaduto, il sistema restituisce un errore 401 (Non autorizzato).

---

## Tecnologie Utilizzate

- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**

---

## Avvio del Progetto

1. Eseguire il backend utilizzando Spring Boot.
2. Registrare o autenticare un utente tramite gli endpoint `/auth/register` o `/auth/login`.
3. Utilizzare il token JWT generato per accedere agli endpoint protetti.

---
