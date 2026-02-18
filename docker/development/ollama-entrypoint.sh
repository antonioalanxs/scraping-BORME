#!/bin/sh

set -e

usage() {
    echo "Entrypoint script for Ollama Docker container."
    echo
    echo "It starts the Ollama server, waits for it to be ready, checks if the specified model is"
    echo "installed, and if not, downloads it. Finally, it keeps the server running in the foreground."
    echo
    echo "The model to install must be set via the 'MODEL' environment variable."
    echo "Example - MODEL=llama3"
    exit 1
}

check_environment_variables() {
    if [ -z "$MODEL" ]; then
        usage
    fi
}

start_server() {
    echo "Starting server in background..."
    ollama serve &
    SERVER_PID=$!
}

wait_for_server() {
    : '
        Ollama 0.12 does not include a health check endpoint or netcat, so there is no reliable 
        way to verify when the server is fully ready to accept connections.

        As a temporary workaround, we use a fixed sleep delay before continuing.
        
        These kind of tools are planned for version 0.13, but it has not been released yet.

        https://github.com/ollama/ollama/issues/5389
    '
    echo "Waiting for server to be ready..."
    sleep 25 
    echo "Server is ready."
}

install_model_if_needed() {
    ENGINE="$1"
    echo "Checking if model '$ENGINE' is installed..."
    if ! ollama list | grep -q "^$ENGINE"; then
        echo "Model '$ENGINE' not found. Downloading..."
        ollama pull "$ENGINE"
        echo "Model '$ENGINE' installed successfully."
    else
        echo "Model '$ENGINE' is already installed."
    fi
}

keep_server_foreground() {
    echo "Keep server running in foreground..."
    wait $SERVER_PID
}

main() {
    check_environment_variables
    start_server
    wait_for_server
    install_model_if_needed "$MODEL"
    keep_server_foreground
}

main
