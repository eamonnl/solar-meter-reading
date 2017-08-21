/**
 * 
 */
package com.eamonnlinehan.pvmeter;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @author eamonn.linehan
 *
 */
public class RxtxSerialPort implements com.eamonnlinehan.pvmeter.SerialPort {

	private final String portName;

	private gnu.io.SerialPort serialPort;

	public RxtxSerialPort(String portName)
			throws UnsupportedCommOperationException, NoSuchPortException, PortInUseException {

		this.portName = portName;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eamonnlinehan.pvmeter.SerialPortInterface#connect(java.lang.String)
	 */
	@Override
	public void connect() throws UnsupportedCommOperationException, NoSuchPortException, PortInUseException {

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use " + portName);
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

			if (commPort instanceof gnu.io.SerialPort) {
				this.serialPort = (gnu.io.SerialPort) commPort;
				this.serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

			} else {
				System.out.println("Error: Only serial ports are handled by this example.");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eamonnlinehan.pvmeter.SerialPortInterface#disconnect()
	 */
	@Override
	public void disconnect() {
		// close the serial port
		try {

			serialPort.removeEventListener();
			serialPort.close();

			// logText = "Disconnected.";

		} catch (Exception e) {
			// logText = "Failed to close " + serialPort.getName()
			// + "(" + e.toString() + ")";

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.eamonnlinehan.pvmeter.SerialPortInterface#addEventListener(gnu.io.
	 * SerialPortEventListener)
	 */
	@Override
	public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException {
		this.serialPort.addEventListener(listener);
		this.serialPort.notifyOnDataAvailable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.eamonnlinehan.pvmeter.SerialPortInterface#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return this.serialPort.getInputStream();
	}

	public String getPortName() {
		return portName;
	}

}
