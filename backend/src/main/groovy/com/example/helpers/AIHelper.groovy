package com.example.helpers

import groovy.json.JsonSlurper

class AIHelper {
    static String getPrompt(String text, String path) {
        /**
         * Returns the prompt template with the provided text inserted.
         *
         * @param text The text to insert into the prompt template.
         * @param path The path to the prompt template file.
         * @return String The complete prompt.
         */
        AIHelper.class.getResourceAsStream(path).withStream { stream ->
            String prompt = stream.text.replace("{{ text }}", text)
            return prompt
        }
    }

    static Map parseResponse(String output) {
        /**
         * Converts the output String from the AI service into a Map.
         *
         * @param output The raw output from the AI service.
         * @return Map The parsed response, or null if parsing fails.
         */
        try {
            return [
                result: new JsonSlurper().parseText(output).output[1].content[0].text
            ]
        } catch (Exception ignored) {
            return null
        }
    }
}
