package com.eamonnlinehan.pvmeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Random;
import java.util.TooManyListenersException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

// Kicks off a thread to regularly send sample/random PV data back
public class MockSerialPort implements SerialPort {

	private SerialPortEventListener listener;

	// Writing data here has it pop out on the Serial ports InputStream
	private PipedOutputStream outputStream;
	
	private Thread microController;

	@Override
	public void disconnect() {
		try {
			this.microController.stop();
			this.outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addEventListener(SerialPortEventListener listener) throws TooManyListenersException {
		this.listener = listener;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		PipedInputStream inputStream = new PipedInputStream();
		this.outputStream = new PipedOutputStream();
		inputStream.connect(outputStream);
		return inputStream;
	}

	@Override
	public void connect() throws UnsupportedCommOperationException, NoSuchPortException, PortInUseException {
		// Kick Off the Thread

		this.microController = new Thread(new Runnable() {

			@Override
			public void run() {

				Random rn = new Random();

				try {
					for (;;) {
						
						int millis = rn.nextInt(10 - 5 + 1) + 5; // 5 -> 10
						
						outputStream.write(rn.nextInt(1000 - 100 + 1) + 100); // 100 -> 1000
						outputStream.write(millis); 
						listener.serialEvent(new SerialPortEvent(null, SerialPortEvent.DATA_AVAILABLE, true, true));
						
						// wait
						try {
							Thread.sleep(millis);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
		this.microController.start();

	}

	@Override
	public String getPortName() {
		return this.getClass().getSimpleName();
	}

}
