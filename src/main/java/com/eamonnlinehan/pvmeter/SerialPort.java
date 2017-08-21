package com.eamonnlinehan.pvmeter;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

public interface SerialPort {

	void connect() throws UnsupportedCommOperationException, NoSuchPortException, PortInUseException;

	void disconnect();

	void addEventListener(SerialPortEventListener listener) throws TooManyListenersException;

	InputStream getInputStream() throws IOException;

	String getPortName();
}