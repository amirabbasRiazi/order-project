## Order Statistics Tool for Dott

### Key Features:
  * Data Filtering and Grouping: Filters orders based on date intervals and groups them by product age to reveal sales trends.
  * Customizable Intervals: Supports both default and custom age intervals for flexible analysis.
  * Scala-Centric Implementation: Leverages the power of Scala for concise and expressive code.
  * Efficient Execution Context: Employs a FixedThreadPool for managed concurrency and predictable resource usage.
  * Robust Data Persistence: Utilizes PostgreSQL for reliable storage and querying of order data.
  * Slick for Seamless Database Interaction: Simplifies database interactions with a functional and type-safe approach.
  * Asynchronous Programming with Futures: Enables non-blocking operations for enhanced performance and responsiveness.
  * Lazy Initialization for Database: Ensures efficient database initialization only when needed.
  * Pattern Matching for Elegant Logic: Leverages pattern matching for concise and expressive code structure.

### Requirements:
Scala version: 2.13.12


### Libraries:
 * Date parsing and data structures (e.g., java.time, scala.time)
 * Database interaction (Slick)
 * Asynchronous programming (Future)
 * PostgreSQL database
### Usage:

Compile the code.
Run the program with arguments:
$ order "2023-01-01" "2023-12-11" [optional_custom_intervals]
