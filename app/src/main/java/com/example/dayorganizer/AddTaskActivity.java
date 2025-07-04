package com.example.dayorganizer;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    private AutoCompleteTextView titleInput;
    private RadioGroup priorityGroup;
    private EditText dateInput, timeInput;
    private Button saveButton;
    private ChipGroup categoryChipGroup;
    private Chip personalChip, workChip, studyChip;

    private String selectedCategory = "Personal";
    private String selectedPriority = "Low";
    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Your Tasks");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        titleInput = findViewById(R.id.titleInput);
        priorityGroup = findViewById(R.id.priorityGroup);
        dateInput = findViewById(R.id.dateInput);
        timeInput = findViewById(R.id.timeInput);
        saveButton = findViewById(R.id.saveButton);
        categoryChipGroup = findViewById(R.id.categoryChipGroup);
        personalChip = findViewById(R.id.personalChip);
        workChip = findViewById(R.id.workChip);
        studyChip = findViewById(R.id.studyChip);

        // Enhanced suggestions for AutoCompleteTextView
        String[] suggestions = {
                "Meeting", "Workout", "Study", "Shopping", "Call", "Project", "Assignment", "Exam", "Appointment", "Birthday", "Anniversary", "Cleaning", "Cooking", "Reading", "Exercise", "Meditation", "Travel", "Presentation", "Interview", "Deadline", "Grocery", "Laundry", "Movie", "Party", "Visit", "Doctor", "Dentist", "Conference", "Seminar", "Workshop"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, suggestions);
        titleInput.setAdapter(adapter);
        titleInput.setThreshold(1); // Show suggestions after 1 character
        titleInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) titleInput.showDropDown();
        });
        titleInput.setOnClickListener(v -> titleInput.showDropDown());

        // Date picker
        dateInput.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                dateInput.setText(selectedDate);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Time picker
        timeInput.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                timeInput.setText(selectedTime);
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        // Category selection (only one at a time for simplicity)
        categoryChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.personalChip) selectedCategory = "Personal";
            else if (checkedId == R.id.workChip) selectedCategory = "Work";
            else if (checkedId == R.id.studyChip) selectedCategory = "Study";
        });

        // Priority selection
        priorityGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = findViewById(checkedId);
            if (rb != null) selectedPriority = rb.getText().toString();
        });

        saveButton.setOnClickListener(v -> saveTask());
    }

    private void saveTask() {
        String title = titleInput.getText().toString().trim();
        if (title.isEmpty() || selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        TaskDbHelper dbHelper = new TaskDbHelper(this);
        dbHelper.getWritableDatabase().execSQL(
                "INSERT INTO " + TaskDbHelper.TABLE_NAME + " (title, category, priority, date, time, completed) VALUES (?, ?, ?, ?, ?, 0)",
                new Object[]{title, selectedCategory, selectedPriority, selectedDate, selectedTime}
        );
        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
} 