Link to main smart home app: https://github.com/104222196/smarthome

This repository contains a NodeJS program that simulates the temperature, humidity, and light sensors that send data to the Smart Home Android app.

# Setup instruction

1. This project requires an MQTT broker to be running on the local machine (localhost) at port 1883. One broker option is Mosquitto, which can be downloaded on [this link](https://mosquitto.org/download/). 

2. The broker will need to support persistence to retain received messages after it is shut down and restarted. This is important because some topics do not receive messages at regular intervals and future subscribing clients need to read the latest message after the broker is restarted. If you are using Mosquitto, you can find a configuration file named `mosquitto.conf` file in the folder in which Mosquitto is installed. In this file, add the following lines:

```
persistence true
persistence_file mosquitto.db
persistence_location path_to_where_you_want_the_persistence_file_to_be
```

You can rename `mosquitto.db` to whatever you want and change the `persistence_location` to whatever directory you desire.

3. The broker also needs to support annonymous connections. In the same configuration file, add the following:

```
listener 1883 0.0.0.0
allow_anonymous true
```

This is not a security best practice but it saves us the headache of having to configure passwords and such :)

2. If you use Mosquitto, it is recommended to run the broker in verbose mode to view the data coming in and out of the broker. The terminal command for this is:

```
mosquitto -v -c "your config file"
```

where `-v` stands for verbose and `-c "your config file"` represents the path to the configuration file with which you run the broker. 

2. After having the MQTT broker up and running, `cd` to this directory.

3. Type `npm install` to install all the dependencies. You can also you `yarn install` if you use yarn.

4. Type `node index.js` to run the project. To confirm that the virtual sensors are emitting data, you can check the log of the MQTT broker in the terminal