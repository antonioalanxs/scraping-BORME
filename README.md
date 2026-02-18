# scraping-BORME

## Table of Contents

- [scraping-BORME](#scraping-borme)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [API Endpoints](#api-endpoints)
  - [Installation](#installation)
    - [Prerequisites](#prerequisites)
    - [Run the application](#run-the-application)
  - [AI Integration](#ai-integration)
  - [Background Processing (Recommended Improvement)](#background-processing-recommended-improvement)
  - [Contributions](#contributions)
  - [License](#license)

## Overview

This project is a full-stack application (built using [Groovy](https://groovy-lang.org/), [Grails](https://grails.apache.org/), [Docker](https://www.docker.com/), [MySQL](https://www.mysql.com/), [Groq](https://groq.com/), [TypeScript](https://www.typescriptlang.org/) and [Svelte](https://svelte.dev/)) that extracts company incorporations published in the **BORME** (**Boletín Oficial del Registro Mercantil**) from the official website of the **Boletín Oficial del Estado** ([BOE](https://www.boe.es/)).

Given a specific date, the system:

1. Locates the corresponding BORME daily publication
2. Downloads the relevant PDF files
3. Extracts incorporation entries
4. Uses a Language Model ([LLM](https://en.wikipedia.org/wiki/Language_model)) to parse unstructured legal text into structured JSON
5. Persists the data in a relational database
6. Exposes the results through a REST API
7. Displays the information in a frontend interface

The official BORME publications are available at [https://www.boe.es/borme/dias/YYYY/MM/DD](https://www.boe.es/borme/dias/2025/09/01).

## API Endpoints

| Method | Path                          | Description                                                                                                      |
|:------:|:-----------------------------|:-----------------------------------------------------------------------------------------------------------------|
| GET    | `/constitutions?date=<date>` | Returns company incorporations for the given date. If entries already exist in the database, they are returned immediately. Otherwise, the system processes, persists and returns them. |
| GET    | `/constitutions/<identifier>`| Returns detailed information for a specific company incorporation, including structured JSON and links to the original PDF. |

## Installation

### Prerequisites

- Install [Node.js](https://nodejs.org/es/download) 22.20.0 or higher:
    ```bash
    sudo apt update
    sudo apt install -y curl
    curl -fsSL https://deb.nodesource.com/setup_22.x | sudo -E bash -
    sudo apt install -y nodejs
    ```

- Have Java Development Kit ([JDK](https://www.oracle.com/es/java/technologies/javase/jdk11-archive-downloads.html)) 11 installed on your machine

- Have [Docker](https://docs.docker.com/engine/install) installed on your machine

- Have a [Docker Hub](https://hub.docker.com/) account

### Run the application

- Clone the repository:
    ```shell
    git clone https://github.com/antonioalanxs/scraping-BORME
    cd scraping-BORME
    ```

- Copy `.env.example` to `.env` and configure your environment:
    ```js
    BORME_URI=https://www.boe.es/borme/dias

    LLM_URI=llm_uri_placeholder
    LLM_API_KEY=llm_api_key_placeholder
    LLM_MODEL=llm_model_placeholder
    LLM_MAXIMUM_THREAD_REQUESTS=llm_maximum_thread_requests_placeholder_if_needed

    DATABASE_NAME=database_name_placeholder
    DATABASE_USER=database_user_placeholder
    DATABASE_PASSWORD=database_password_placeholder
    DATABASE_ROOT_USER=database_root_user_placeholder
    DATABASE_ROOT_PASSWORD=database_root_password_placeholder

    CORS_URL_PATTERN=cors_url_pattern_placeholder
    CORS_ALLOWED_ORIGINS=cors_allowed_origins_placeholder
    CORS_ALLOWED_METHODS=cors_allowed_methods_placeholder
    CORS_ALLOWED_HEADERS=cors_allowed_headers_placeholder
    CORS_ALLOW_CREDENTIALS=cors_allow_credentials_placeholder

    VITE_API_URI=http://localhost:8080/api
    ```

- Go to the Docker development directory and Start the database service using the `up.sh` script:
    ```shell
    cd scraping-BORME/docker/development
    ./up.sh
    ```

- **Backend** - navigate to the backend directory, install dependencies and run the server:
    ```shell
    cd ../../backend
    ./gradlew assemble
    ./gradlew bootRun
    ```

> The backend API will be available at [http://localhost:8080/api](http://localhost:8080/api).

- **Frontend** - navigate to the frontend directory, install dependencies and start the server:
    ```shell
    cd ../frontend
    npm install
    npm run dev
    ```

> The frontend interface will be available at [http://localhost:5173](http://localhost:5173).

## AI Integration

The project currently uses a Language Model API via Groq to transform raw legal text from BORME PDFs into structured JSON, performing tasks such as extracting incorporation details, normalizing dates and names, and converting unstructured text into a format suitable for database storage and API consumption.

Example input:

```shell
389826 - MEDITERRAMOVING SL.
Constitución. Comienzo de operaciones: 9.07.25. Objeto social: Transporte de mercancías, mudanzas, almacenajes de
mercancías. Guardamuebles, logística. Servicio de embalaje. Alquiler de vehículos con y sin conductor. Importación y
exportación de mercancías. Todo tipo de actividades relacionadas con trabajos de albañilería, reformas y mantenimiento
de comunidades. Domicilio: PTDA DE ALZABARES BAJO 46 (ELCHE). Capital: 3.000,00 Euros. Declaración de unipersonalidad.
Socio único: VIDAL RICO RICARDO. Nombramientos. Adm. Unico: VIDAL RICO RICARDO. Datos registrales. S 8 , H A 199888,
I/A 1 (25.08.25).
```

Example output:

```javascript
{
  "entity": {
    "code": "389826",
    "name": "MEDITERRAMOVING, S. L."
  },
  "start_of_operations": "2025-07-09",
  "social_object": "Transporte, logística y almacenaje de mercancías, mudanzas, alquiler de vehículos, embalaje y trabajos de albañilería, reformas y mantenimiento.",
  "address": "PTDA DE ALZABARES BAJO 46 (ELCHE)",
  "capital": 3.000,
  "single_person_declaration": true,
  "sole_partner": "Vidal Rico, Ricardo",
  "appointment": {
    "sole_administrator": "Vidal Rico, Ricardo"
  },
  "registry": {
    "section": "8",
    "page": "199888",
    "entry": "1",
    "date": "2025-08-25"
  }
}
```

Originally, the project was designed to run a local language model via [Ollama](https://hub.docker.com/r/ollama/ollama) inside Docker. Due to resource constraints, it now uses the Groq API. Free-tier limitations may restrict the number of requests per execution thread, so some entries may be skipped.

Example backend log showing skipped entries due to request limits:

```shell
[com.example.LanguageModelService] Target '389831 - BOLUCAMVA SL. Constitución. Comienzo de operaciones: 14.07.25. Objeto social: 1. Actividades de gestión de fondos propios con exclusión de fondos ajenos. 2. Actividades de apoyo a la agricultura. 3. Alquiler de bienes inmobiliarios por cuenta propia. 4. Silvicultura y otras actividades forestales. Domicilio: C/ L'ESPART 5 (ALTEA). Capital: 3.000,00 Euros. Declaración de unipersonalidad. Socio único: CAMPOMANES EGUIGUREN LUIS. Nombramientos. Adm. Unico: CAMPOMANES EGUIGUREN LUIS. Datos registrales. S 8 , H A 199939, I/A 1 (25.08.25). ' omitted because the maximum requests per execution has been reached for thread 'qtp99823907-48'
[com.example.LanguageModelService] Target '389835 - TRIUNFO IBERICO SL. Constitución. Comienzo de operaciones: 30.07.25. Objeto social: Restaurantes y puestos de comidas. Domicilio: C/ HERNAN CORTES 3 (SAN VICENTE DEL RASPEIG). Capital: 3.000,00 Euros. Nombramientos. Adm. Unico: LILLO MARTINEZ VICENTE. Datos registrales. S 8 , H A 199944, I/A 1 (25.08.25). ' omitted because the maximum requests per execution has been reached for thread 'qtp99823907-48'
...
[com.example.LanguageModelServiceRequestCleanerFilter] Cleared ThreadLocal for request '/api/constitutions'
```

## Background Processing (Recommended Improvement)

At present, the endpoint `GET /constitutions?date=<date>` handles the entire workflow — downloading PDFs, extracting text, calling the language model multiple times and persisting results — within a single HTTP request. While this approach works for demonstration purposes, it is not optimized for production environments, especially when processing large numbers of incorporations.

Ideally, this workflow would be offloaded to an asynchronous background job. In such a setup, the API endpoint would first check the database and immediately return existing results. If data for the requested date is not available, a background job would be triggered to perform the PDF downloads, parsing, LLM processing and database persistence. The client could then poll the API or subscribe to updates until the results are ready.

This improvement has not been implemented in the current version due to time constraints and the scope of this project. Nevertheless, the codebase has been structured so that integrating background jobs in the future would be straightforward.

## Contributions

If you would like to contribute to the project, please follow these steps:

1. Fork the repository
2. Create a new branch for your feature or bug fix (`git checkout -b feature/new-feature`)
3. Make your changes and commit them following the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) format (`git commit -m 'feat: add new feature'` or `fix: correct a bug`)
4. Push your branch to the remote repository (`git push origin feature/new-feature`)
5. Create a Pull Request

## License

This project is licensed under the [Apache License 2.0](./LICENSE).
