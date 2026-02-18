package com.example.helpers

import grails.util.Holders

import java.time.LocalDate

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.jsoup.Jsoup
import org.jsoup.HttpStatusException


class BORMEHelper {
    private static String getDatePathParameter(LocalDate date) {
        /***
         * Returns a slash-separated date string for URL path parameters.
         *
         * @param date The date to be formatted.
         * @return String The slash-separated date string.
         */
        return date
                .toString()
                .replaceAll("-", "/")
    }

    private static String getUri(String pathParameter) {
        /***
         * Constructs the full URI for accessing documents based on the given date path parameter.
         *
         * @param pathParameter The date path parameter.
         * @return String The full URI for accessing documents.
         */
        String uri = Holders.config.properties?.borme?.uri
        uri = "$uri/$pathParameter"
        return uri
    }

    private static List<String> fetchBodyLinks(String uri) {
        /**
         * Fetches all PDF document links from the given URI.
         *
         * @param uri The URI to fetch documents from.
         * @return List<String> A list of absolute URIs to PDF documents.
         */
        try {
            return Jsoup.connect(uri)
                    .get()
                    .select("a[href\$=.pdf]")
                    .collect { it.absUrl("href") }
        } catch (HttpStatusException exception) {
            return Collections.emptyList()
        }

    }

    private static String parseDocument(String document) {
        /***
         * Cleans a document by normalizing spaces, and removing headers, margins and footers.
         *
         * @param document The raw document text.
         * @return String The cleaned document text.
         */
        return document
                .replaceAll(~"\\s+", " ")
                .replaceAll(~"(?i)BOLETÍN OFICIAL DEL REGISTRO MERCANTIL\\s+Núm\\.\\s*\\d+\\s+.*?Pág\\.\\s*\\d+", "")
                .replaceAll(/(?i)c\s*v\s*e:.*$/, "")
                .replaceAll(/(?i)Verificable en https:\/\/www\.boe\.es/, "")
                .trim()
    }

    private static String getDocument(String uri) {
        /***
         * Extracts text from a PDF document at the given URI.
         *
         * @param uri The URI of the PDF document.
         * @return String The cleaned text content of the document.
         */
        InputStream stream = new URL(uri).openStream()
        PDDocument file = PDDocument.load(stream)

        String document = new PDFTextStripper().getText(file)
        document = parseDocument(document)

        file.close()
        stream.close()

        return document
    }

    private static List<String> parseConstitutions(String document) {
        /***
         * Splits a document into blocks and filter those related to "Constitución.".
         *
         * @param document The document text.
         * @return List<String> "Constitución."-related text blocks.
         */
        return document
                .split(/(?=\d{6}\s*-\s*[A-Z])/)
                .findAll { it.contains("Constitución.") }
    }

    static List<Map<String, String>> getConstitutions(LocalDate date) {
        /**
         * Retrieves and parses constitution-related text blocks for a given date.
         *
         * This method builds the URI associated with the specified date, fetches all available PDF documents
         * from that URI, extracts their text content and collects the sections that reference "Constitución.".
         *
         * @param date The date for which to retrieve constitution-related text blocks.
         * @return A list of strings with all "Constitución."-related text blocks.
         */
        List<Map<String, String>> result = new ArrayList<>()

        String parameter = getDatePathParameter(date)
        String uri = getUri(parameter)
        List<String> links = fetchBodyLinks(uri)

        links.each { String link ->
            String document = getDocument(link)
            List<String> constitutions = parseConstitutions(document)

            constitutions.each { String constitution ->
                result.add([source: link, value: constitution])
            }
        }

        return result
    }
}
