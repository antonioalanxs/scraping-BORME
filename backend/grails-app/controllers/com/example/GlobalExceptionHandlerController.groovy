package com.example

import java.time.format.DateTimeParseException


class GlobalExceptionHandlerController {
    static responseFormats = ["json"]

    def handleDateTimeParseException(DateTimeParseException ignored) {
        respond([detail: "Invalid date format. Please, use 'YYYY-MM-DD'."], status: 400)
    }

    def handleGenericException(Exception exception) {
        log.error(exception.message, exception)
        respond([detail: "An error occurred while processing your request. Please, try again later."], status: 500)
    }
}
