package com.eamonnlinehan.pvmeter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

// Run as ROOT on PI to have access to serial port

public class ArduinoSerialCommunication implements SerialPortEventListener {

	private static final Logger log = LoggerFactory.getLogger(ArduinoSerialCommunication.class);

	private final SerialPort serialPort;

	private InputStream input = null;

	public ArduinoSerialCommunication(SerialPort serialPort) {
		this.serialPort = serialPort;
		try {
			this.serialPort.connect();
			this.input = this.serialPort.getInputStream();
		} catch (UnsupportedCommOperationException | NoSuchPortException | PortInUseException | IOException e) {
			log.error("Could not connect to " + serialPort.getPortName(), e);
		}

	}

	@Override
	public void serialEvent(SerialPortEvent evt) {

		switch (evt.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE:
			try {

				if (input.available() > 0) {

					int pulseCount = input.read();
					int pulseCountTimePeriod = input.read();

					log.info("Meter pulsed " + pulseCount + " times in last " + pulseCountTimePeriod + "ms. ");

				}

			} catch (Exception e) {
				log.error("Failed to read pulse count from micro-controller.", e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {

			List<String> localPortIdentifiers = new ArrayList<>();
			Enumeration<CommPortIdentifier> thePorts = CommPortIdentifier.getPortIdentifiers();
			while (thePorts.hasMoreElements()) {
				CommPortIdentifier com = thePorts.nextElement();
				switch (com.getPortType()) {
				case CommPortIdentifier.PORT_SERIAL:
					localPortIdentifiers.add(com.getName());
				}
			}

			// dump the list of available ports on the current system
			for (Iterator<String> iterator = localPortIdentifiers.iterator(); iterator.hasNext();) {
				log.debug(iterator.next());
			}

			// Now pick the one that the Arduino is on and connect - not sure
			// logic
			if (localPortIdentifiers.contains("/dev/ttyUSB0"))
				new ArduinoSerialCommunication(new RxtxSerialPort("/dev/ttyUSB0"));
			else
				log.error("Could not identify which com port to connect to: "
						+ StringUtils.collectionToDelimitedString(localPortIdentifiers, ","));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
