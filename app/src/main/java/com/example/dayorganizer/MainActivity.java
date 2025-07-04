package com.example.dayorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private RecyclerView taskRecyclerView;
    private FloatingActionButton fab;
    private TextView summaryTextView;
    private Button shareButton;
    private long selectedDateMillis;
    private TaskAdapter taskAdapter;
    private TaskDbHelper dbHelper;
    private String selectedDateString;
    private TextView greetingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calendarView = findViewById(R.id.calendarView);
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        fab = findViewById(R.id.fab);
        summaryTextView = findViewById(R.id.summaryTextView);
        shareButton = findViewById(R.id.shareButton);
        greetingTextView = findViewById(R.id.greetingTextView);

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedDateMillis = calendarView.getDate();

        dbHelper = new TaskDbHelper(this);
        taskAdapter = new TaskAdapter(this, List.of(), (task, isChecked) -> {
            dbHelper.updateTaskCompleted(task.getId(), isChecked);
            taskRecyclerView.post(() -> {
                loadTasksForDate(selectedDateString);
                if (isChecked) {
                    String compliment = getCompliment(task);
                    Snackbar.make(taskRecyclerView, compliment, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(taskRecyclerView, "Task marked as incomplete!", Snackbar.LENGTH_SHORT).show();
                }
            });
        });
        taskAdapter.setOnTaskDeleteClickListener(task -> {
            dbHelper.deleteTaskById(task.getId());
            taskRecyclerView.post(() -> {
                loadTasksForDate(selectedDateString);
                Snackbar snackbar = Snackbar.make(taskRecyclerView, "Task deleted", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", v -> {
                    dbHelper.getWritableDatabase().execSQL(
                        "INSERT INTO " + TaskDbHelper.TABLE_NAME + " (title, category, priority, date, time, completed) VALUES (?, ?, ?, ?, ?, ?) ",
                        new Object[]{task.getTitle(), task.getCategory(), task.getPriority(), task.getDate(), task.getTime(), task.isCompleted() ? 1 : 0}
                    );
                    loadTasksForDate(selectedDateString);
                });
                snackbar.show();
            });
        });
        taskRecyclerView.setAdapter(taskAdapter);
        selectedDateString = getDateStringFromMillis(selectedDateMillis);
        loadTasksForDate(selectedDateString);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(v -> shareTasks());

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDateString = year + "-" + (month + 1) + "-" + dayOfMonth;
            loadTasksForDate(selectedDateString);
        });

        setGreeting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasksForDate(selectedDateString);
    }

    private void loadTasksForDate(String date) {
        List<Task> tasks = dbHelper.getTasksByDate(date);
        taskAdapter.setTaskList(tasks);
        int[] summary = dbHelper.getTaskSummary(date);
        summaryTextView.setText("Completed: " + summary[0] + "/" + summary[1]);
        if (tasks.isEmpty()) {
            Snackbar.make(taskRecyclerView, "No tasks for this day. Add one!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void shareTasks() {
        List<Task> tasks = dbHelper.getTasksByDate(selectedDateString);
        if (tasks.isEmpty()) {
            summaryTextView.setText("No tasks to share.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Tasks for ").append(selectedDateString).append(":\n");
        for (Task t : tasks) {
            sb.append("- ").append(t.getTitle())
              .append(" [").append(t.getCategory()).append("] ")
              .append("(").append(t.getPriority()).append(") ")
              .append(t.getTime())
              .append(t.isCompleted() ? " [Done]" : "")
              .append("\n");
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share tasks via"));
    }

    private String getDateStringFromMillis(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    private void setGreeting() {
        String greeting;
        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (hour < 12) greeting = "Good morning!";
        else if (hour < 18) greeting = "Good afternoon!";
        else greeting = "Good evening!";
        greetingTextView.setText(greeting + "\nWelcome to Day Organizer!");
    }

    private String getCompliment(Task task) {
        String title = task.getTitle().toLowerCase();
        String category = task.getCategory().toLowerCase();
        if (category.contains("work")) return "Awesome! You crushed a work task!";
        if (category.contains("study")) return "Great job studying! Keep it up!";
        if (category.contains("personal")) return "Way to go on your personal goal!";
        if (title.contains("workout") || title.contains("exercise")) return "You're a fitness star! 💪";
        if (title.contains("meeting")) return "Meeting done! You're on top of things!";
        if (title.contains("call")) return "Call completed! Stay connected!";
        if (title.contains("project")) return "Project progress! Well done!";
        if (title.contains("shopping") || title.contains("grocery")) return "Shopping done! You're organized!";
        if (title.contains("clean") || title.contains("laundry")) return "Clean and fresh! Nice work!";
        if (title.contains("read") || title.contains("book")) return "Knowledge unlocked! 📚";
        return "Great job completing your task!";
    }
}