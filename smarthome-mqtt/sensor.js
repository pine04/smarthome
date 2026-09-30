const mqtt = require("mqtt");

class Sensor {
    #isOn;
    #client;
    #interval;

    constructor(sensorId, isOn = true) {
        this.sensorId = sensorId;
        this.#isOn = isOn;
    }

    getSensorId() {
        return this.sensorId;
    }

    getIsOn() {
        return this.#isOn;
    }

    connect(brokerUrl, options = { clientId: this.sensorId }) {
        this.#client = mqtt.connect(brokerUrl, options);

        this.#client.on("connect", () => {
            this.subscribeToTopics([this.sensorId, this.sensorId + "/action"]);
        });

        this.#client.on("message", (topic, message) => {
            if (topic === this.sensorId + "/action") {
                if (message.toString() === "turn on") {
                    this.turnOn();
                }
                if (message.toString() === "turn off") {
                    this.turnOff();
                }
            }
        });
    }

    disconnect() {
        if (this.#client === undefined) {
            throw new Error("This device is not connected.");
        }

        if (this.#interval !== undefined) {
            clearInterval(this.#interval);
        }

        this.#client.end();
    }

    subscribeToTopic(topic) {
        if (this.#client === undefined) {
            throw new Error("Call connect() first.");
        }
        if (this.#client.connected === false) {
            throw new Error("Please wait for the device to be connected to the broker before subscribing.")
        }

        this.#client.subscribe(topic);
    }

    subscribeToTopics(topics) {
        if (this.#client === undefined) {
            throw new Error("Call connect() first.");
        }
        if (this.#client.connected === false) {
            throw new Error("Please wait for the device to be connected to the broker before subscribing.")
        }

        topics.forEach(topic => {
            this.#client.subscribe(topic);
        });
    }

    emit(delay, dataGenerationCallback) {
        if (this.#interval !== undefined) {
            clearInterval(this.#interval);
        }

        this.#interval = setInterval(() => {
            if (this.#isOn) {
                const topic = this.sensorId + "/data";
                const message = `(${Math.floor(Date.now() / 1000)},${dataGenerationCallback()})`;
                console.log(`Publishing ${message} to topic ${topic}`);
                const options = {
                    qos: 1,
                    retain: true
                };

                this.#client.publish(topic, message, options);
            }            
        }, delay);
    }

    turnOn() {
        this.#isOn = true;

        if (this.#client !== undefined) {
            const options = {
                qos: 1,
                retain: true
            };
            this.#client.publish(this.sensorId + "/status", "on", options);
        }
    }

    turnOff() {
        this.#isOn = false;

        if (this.#client !== undefined) {
            const options = {
                qos: 1,
                retain: true
            };
            this.#client.publish(this.sensorId + "/status", "off", options);
        }
    }

    attachMessageHandler(onMessageCallback) {
        if (this.#client === undefined) {
            throw new Error("Call connect() first.");
        }

        this.#client.on("message", onMessageCallback);
    }

    attachConnectHandler(onConnectCallback) {
        if (this.#client === undefined) {
            throw new Error("Call connect() first.");
        }

        this.#client.on("connect", onConnectCallback);
    }
}

module.exports = {
    Sensor
}