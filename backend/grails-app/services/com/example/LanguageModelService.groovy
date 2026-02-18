package com.example

import com.example.helpers.AIHelper

import groovy.json.JsonOutput

import org.springframework.beans.factory.annotation.Value


class LanguageModelService {
    @Value('${properties.llm.model}')
    String model

    @Value('${properties.llm.uri}')
    String uri

    @Value('${properties.llm.apiKey}')
    String apiKey

    @Value('${properties.llm.maximumThreadRequests}')
    int maximumThreadRequests

    private static final ThreadLocal<Integer> threadRequestCounter =
            ThreadLocal.withInitial { 0 }

    static void clearThreadLocal() {
        threadRequestCounter.remove()
    }

    Map call(String target) {
        int currentThreadRequestCounter = threadRequestCounter.get()

        if (currentThreadRequestCounter >= this.maximumThreadRequests) {
            println "[${this.class.name}] Target '${target}' omitted because the maximum requests per execution has been reached for thread '${Thread.currentThread().name}'"
            return null
        }

        threadRequestCounter.set(currentThreadRequestCounter + 1)

        Map payload = [model: model,
                       input: AIHelper.getPrompt(target, "/prompt.txt"),]

        URLConnection connection = new URL(uri).openConnection()
        connection.setRequestMethod("POST")
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "Bearer ${apiKey}")
        connection.outputStream.withWriter("UTF-8") { it << JsonOutput.toJson(payload) }

        Map response = AIHelper.parseResponse(connection.inputStream.text)
        connection.inputStream.close()

        return response
    }
}
