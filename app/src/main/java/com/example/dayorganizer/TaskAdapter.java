package com.example.dayorganizer;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private Context context;
    private OnTaskCheckedChangeListener listener;
    private OnTaskDeleteClickListener deleteListener;

    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(Task task, boolean isChecked);
    }

    public interface OnTaskDeleteClickListener {
        void onTaskDelete(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskCheckedChangeListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    public void setOnTaskDeleteClickListener(OnTaskDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTextView.setText(task.getTitle());
        holder.categoryTextView.setText(task.getCategory());
        holder.priorityTextView.setText("Priority: " + task.getPriority());
        holder.dateTimeTextView.setText(task.getDate() + " " + task.getTime());
        holder.completedCheckBox.setChecked(task.isCompleted());
        holder.completedCheckBox.setOnCheckedChangeListener(null);
        holder.completedCheckBox.setChecked(task.isCompleted());
        holder.completedCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onTaskCheckedChanged(task, isChecked);
        });
        if (task.isCompleted()) {
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.titleTextView.setPaintFlags(holder.titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.deleteTaskButton.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onTaskDelete(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, categoryTextView, priorityTextView, dateTimeTextView;
        CheckBox completedCheckBox;
        ImageButton deleteTaskButton;
        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.taskTitleTextView);
            categoryTextView = itemView.findViewById(R.id.taskCategoryTextView);
            priorityTextView = itemView.findViewById(R.id.taskPriorityTextView);
            dateTimeTextView = itemView.findViewById(R.id.taskDateTimeTextView);
            completedCheckBox = itemView.findViewById(R.id.taskCompletedCheckBox);
            deleteTaskButton = itemView.findViewById(R.id.deleteTaskButton);
        }
    }
} 