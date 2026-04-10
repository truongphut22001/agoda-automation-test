# Agoda Automation Test

Selenium + TestNG automation test for Agoda hotel search flow.

---

## Tech Stack

- Java 11+
- Selenium WebDriver 4.x
- TestNG
- Maven
- ChromeDriver

---

## Project Structure

\\\
automation/src/test/java/
├── pages/
│   ├── BasePage.java          # Shared WebDriver helpers (click, wait, type)
│   ├── HomePage.java          # Agoda homepage: search, date picker, occupancy
│   ├── SearchResultPage.java  # Search results: wait for load, select hotel
│   └── HotelPage.java         # Hotel detail page: verify price displayed
├── tests/
│   ├── BaseTest.java          # setUp / tearDown using DriverFactory
│   └── AgodaTest.java         # Test cases
└── utils/
    ├── DriverFactory.java     # ThreadLocal WebDriver (parallel-safe)
    └── DateUtils.java         # Date helpers for test data
\\\

---

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Google Chrome (latest)
- ChromeDriver matching your Chrome version

---

## Setup

1. Clone the repository:
\\\ash
git clone <repo-url>
cd agoda-test-run
\\\

2. Add ChromeDriver to your system PATH, or place it in the project root.

3. Install dependencies:
\\\ash
mvn clean install -DskipTests
\\\

---

## Run Tests

Run all tests:
\\\ash
mvn test
\\\

Run a specific test class:
\\\ash
mvn test -Dtest=AgodaTest
\\\

---

## Test Cases

| Test | Description |
|------|-------------|
| testHotelPriceDisplayed | Search Muong Thanh Saigon Centre Hotel, check-in D+2, check-out D+3, 1 room / 4 adults / 2 children, verify price displayed |

---

## Notes

- Tests run on Chrome by default.
- DriverFactory uses ThreadLocal, safe for parallel execution.
- Do not commit target/, *.class, or driver binaries (covered by .gitignore).
