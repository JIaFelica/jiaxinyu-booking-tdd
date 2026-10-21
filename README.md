# jiaxinyu-booking-tdd

Study Room Booking system built with Test-Driven Development (Java + JUnit).

## Build & Test Commands

```bash
javac -d out -cp junit.jar src/main/java/*.java src/test/java/*.java
java -jar junit.jar execute --class-path out --scan-class-path
```

The first line compiles everything into `out/`; the second runs every test and prints a summary.
They will fail until your first test is written — that is normal.
