# LibraryManager

A Java application for managing a library.
The project allows you to add, edit, remove, search for, and sort books, with support for both in-memory management and data persistence to files.
The various operations are being designed by the principles of design patterns such as:
- *Decorator pattern*, for the search operations
- *Command pattern*, for handling addition and removal of books
- *Strategy pattern*, for the ordering of the results

## Key Features

- Adding new books
- Editing and removing existing books
- Searching using various criteria like author or genre
- Sorting results by number of reviews
- Saving and loading data with CSV or JSON files
- Graphical user interface with Java SWING

## Technologies Used

- Java 21
- Maven
- JUnit 5
- FlatLaf

## Project Structure

- `src/main/java`
  - `commands`: commands for Command Pattern
  - `entities`: domain entities
  - `gui`: graphical user interface and controllers
  - `library`: library management logic
  - `ordering`: sorting strategies
  - `search`: filters and search criteria
- `books.csv` / `books.json`: sample data

## Requirements

- Java 21 or higher
- Maven 3.x


<img width="2172" height="1638" alt="GUI Library Manager" src="https://github.com/user-attachments/assets/57be8015-10a9-4f61-9c68-8b6bfedd514f" />

