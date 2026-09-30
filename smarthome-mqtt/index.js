const fs = require("fs/promises");
const { Sensor } = require("./sensor");

const sensorDataFile = "sensor_data.json";
const mqttBrokerUrl = "mqtt://localhost:1883";

async function readSensorStateFromFile(file) {
    try {
        const data = await fs.readFile(file, { encoding: "utf-8" });
        return JSON.parse(data);
    } catch (error) {
        console.log(error.message);
        return null;
    }
}

async function writeSensorStateToFile(file, dataObject) {
    try {
        await fs.writeFile(file, JSON.stringify(dataObject), { encoding: "utf-8" });
    } catch (error) {
        console.log(error);
    }
}

async function startSensors() {
    const savedSensorData = await readSensorStateFromFile(sensorDataFile);

    const sensorStates = {
        temperatureSensor: savedSensorData !== null ? (savedSensorData["temperatureSensor"] ?? true) : true,
        humiditySensor: savedSensorData !== null ? (savedSensorData["humiditySensor"] ?? true) : true,
        lightSensor: savedSensorData !== null ? (savedSensorData["lightSensor"] ?? true) : true
    };

    const temperatureSensor = new Sensor("temperatureSensor", sensorStates["temperatureSensor"]);
    temperatureSensor.connect(mqttBrokerUrl);
    temperatureSensor.emit(5000, getRandomTemperature);

    const humiditySensor = new Sensor("humiditySensor", sensorStates["humiditySensor"]);
    humiditySensor.connect(mqttBrokerUrl);
    humiditySensor.emit(5000, getRandomHumidity);

    const lightSensor = new Sensor("lightSensor", sensorStates["lightSensor"]);
    lightSensor.connect(mqttBrokerUrl);
    lightSensor.emit(5000, getRandomLight);


    process.on("SIGINT", async () => {
        console.log("Disconnecting all clients...");

        const latestSensorStates = {
            temperatureSensor: temperatureSensor.getIsOn(),
            humiditySensor: humiditySensor.getIsOn(),
            lightSensor: lightSensor.getIsOn(),
        }

        try {
            await writeSensorStateToFile(sensorDataFile, latestSensorStates);
    
            temperatureSensor.disconnect();
            humiditySensor.disconnect();
            lightSensor.disconnect();
        } catch (error) {
            console.log(error);
        }
    });
}

function getRandomTemperature() {
    return Math.round(Math.random() * 50).toString();
}

function getRandomHumidity() {
    return Math.round(Math.random() * 100).toString();
}

function getRandomLight() {
    return Math.round(Math.random() * 1000).toString();
}

startSensors();