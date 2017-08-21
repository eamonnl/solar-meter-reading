/*
  Detect the flash of a LED on a PV solar panel meter

  Reads an analog input pin, maps the result to a range from 0 to 255, looks for a change in light
  level above a threshold and prints the results to the serial monitor. The built-in LED will
  flash each time a meter pulse is detected

  The circuit:
   Connect one end of the photocell to 5V, the other end to Analog 0.
   Then connect one end of a 10K resistor from Analog 0 to ground
   LED connected from digital pin 9 to ground

  See circuit at https://learn.adafruit.com/photocells/using-a-photocell

  Created by Eamonn Linehan 21 April 2016

*/

int photocellPin = 0;     // the cell and 10K pulldown are connected to a0
int photocellReading;     // the analog reading from the analog resistor divider
int triggerThreshold = 20; // The relative change in light level that indicates a pulse

void setup() {
  // We'll send sensor values via the Serial interface whenever a flash is detected
  Serial.begin(9600);

  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED_BUILTIN, OUTPUT);

  // Read an initial value
  photocellReading = map(analogRead(photocellPin), 0, 1023, 0, 255);
}

void loop() {

  int newPhotocellReading = map(analogRead(photocellPin), 0, 1023, 0, 255);

  // for debugging and tuning the threshold print these values
  Serial.println(newPhotocellReading);

  // TODO might be better to count pulses and output the number of pulses every minute on the serial port

  if (abs(newPhotocellReading - photocellReading) > triggerThreshold)
  {
    digitalWrite(LED_BUILTIN, HIGH); // turn the LED on (HIGH is the voltage level)
    Serial.print("PV_Meter_Pulse ");
    Serial.print(newPhotocellReading);
    Serial.print(" ");
    Serial.print(millis());
    Serial.print("/n/r");
    delay(50); // led on time delay - May want to adjust this depending on length of pulse (Suspect its < 1ms)
    digitalWrite(LED_BUILTIN, LOW);    // turn the LED off by making the voltage LOW
  }

  photocellReading = newPhotocellReading;

  // wait 2 milliseconds before the next loop
  // for the analog-to-digital converter to settle
  // after the last reading:
  delay(2);
}
