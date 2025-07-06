# Day Organizer - Android App

A modern, feature-rich daily planner and task management Android application built with Java and Material Design.

## Features

### Core Features
- **Daily Task Management**: Add, edit, and organize tasks by date
- **Modern Calendar Interface**: MaterialCalendarView with dark/light theme support
- **Task Categories**: Work, Study, Personal, Health, Shopping, and more
- **Priority Levels**: High, Medium, Low priority with visual indicators
- **Task Completion Tracking**: Mark tasks as complete with personalized compliments
- **Date and Time Selection**: Pick specific dates and times for tasks
- **Task Suggestions**: AutoCompleteTextView with common task suggestions

### Advanced Features
- **SQLite Database**: Persistent storage for all tasks
- **Task Sharing**: Share task lists via implicit intents
- **Undo Functionality**: Restore deleted tasks with Snackbar
- **Personalized Greetings**: Time-based welcome messages
- **Material Design**: Modern UI with dark/light theme support
- **Responsive Layout**: Works on various screen sizes

### Technical Implementation
- **Explicit Intents**: Navigation between activities
- **Implicit Intents**: Task sharing functionality
- **RecyclerView**: Efficient task list display
- **Custom Adapters**: TaskAdapter for list management
- **Database Helper**: TaskDbHelper for SQLite operations
- **Material Components**: FloatingActionButton, Snackbar, Chips, etc.

## Screenshots

The app features a clean, modern interface with:
- Dark theme optimized for better readability
- Material Design components throughout
- Intuitive task management workflow
- Professional calendar interface

## Project Structure

```
app/src/main/java/com/example/dayorganizer/
├── MainActivity.java          # Main activity with calendar and task list
├── AddTaskActivity.java       # Task creation activity
├── Task.java                  # Task data model
├── TaskAdapter.java           # RecyclerView adapter for tasks
├── TaskDbHelper.java          # SQLite database helper
└── CalendarDecorator.java     # MaterialCalendarView styling

app/src/main/res/
├── layout/                    # UI layouts
├── values/                    # Colors, strings, themes
└── drawable/                  # App icons and graphics
```

## Libraries Used

- **Material Design Components**: Modern UI components
- **MaterialCalendarView**: Professional calendar interface
- **AndroidX**: Latest Android support libraries
- **SQLite**: Local database storage

## Installation

1. Clone or download the project
2. Open in Android Studio
3. Sync Gradle dependencies
4. Build and run on Android device/emulator

## Usage

1. **View Tasks**: Select a date on the calendar to view tasks
2. **Add Task**: Tap the floating action button to create new tasks
3. **Complete Tasks**: Check the checkbox to mark tasks as complete
4. **Delete Tasks**: Long press to delete tasks (with undo option)
5. **Share Tasks**: Use the share button to export task lists

## Technical Highlights

- **Modern Architecture**: Follows Android best practices
- **Theme Support**: Seamless dark/light mode switching
- **Database Design**: Efficient SQLite schema for task storage
- **User Experience**: Intuitive navigation and feedback
- **Code Quality**: Clean, well-documented Java code

## Future Enhancements

- Task reminders and notifications
- Task categories with color coding
- Data backup and sync
- Widget support
- Task templates and recurring tasks

---

**Developed for Android Mini Project**  
*College Project Submission*
