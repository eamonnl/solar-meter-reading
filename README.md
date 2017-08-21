# Solar PV Meter Reading from Arduino with Spring Boot 

1. Run `mvn initialize` to install the RXTX jar in your local repo
3. `mvn package`
4. Execute with `java -jar target/solar-meter-reading-0.0.1-SNAPSHOT.jar`


This App will:
1. Read Events representing meter pulses off the serial port
2. Store these with timestamps in a DB
3. Calculate the current/hourly/daily output from this data
4. Maybe an API to export this data to web for graphing?


// TODO
// 1. Test framework in palce - mock values coming from PI
// 2. packaging to run on PI
// 3. Build out PvMeter class and required calculations
// 4. Add embedded DB to persist power generation values
// 5. API / UI to present data