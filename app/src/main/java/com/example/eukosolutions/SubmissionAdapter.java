package com.example.eukosolutions;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for binding a list of submissions to a RecyclerView.
 * Used in AdminDashboardActivity to display submission summaries.
 */
public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {
    private List<SubmissionModel> submissionList;
    private AdminDashboardActivity context;

    /**
     * Constructor for SubmissionAdapter.
     * @param submissionList List of submissions to display.
     * @param context The context (AdminDashboardActivity) where this adapter is used.
     */
    public SubmissionAdapter(List<SubmissionModel> submissionList, AdminDashboardActivity context) {
        this.submissionList = new ArrayList<>(submissionList);
        this.context = context;
    }
    /**
     * Called when RecyclerView needs a new ViewHolder of a given type.
     * Inflates the layout for a single list item (submission).
     */
    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout defined in res/layout/submission_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submission_item, parent, false);
        return new SubmissionViewHolder(view);
    }
    /**
     * Called to display data at the specified position.
     * Binds each SubmissionModel object to its corresponding ViewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {
        // Get the SubmissionModel object for the current position
        SubmissionModel submission = submissionList.get(position);

        holder.textViewCompanyName.setText(submission.getCompanyName() != null ? submission.getCompanyName() : "N/A"); // Display Company Name
        holder.textViewDeadline.setText(submission.getDeadline() != null ? "Deadline: " + submission.getDeadline() : "No Deadline"); // Display Deadline

        // Set the click listener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubmissionDetailsActivity.class);
            // Pass the document ID to the details activity
            intent.putExtra("DOCUMENT_ID", submission.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the list
        return submissionList != null ? submissionList.size() : 0; // Added null check
    }

    /**
     * Updates the adapter with a new list of submissions.
     * Notifies RecyclerView that the data has changed so it can refresh the UI.
     * @param submissions The new list of submissions to display.
     */
    public void setSubmissions(List<SubmissionModel> submissions) {
        this.submissionList.clear();
        if (submissions != null) {
            this.submissionList.addAll(submissions);
        }
        notifyDataSetChanged(); //  Tells the RecyclerView to refresh
    }

    /**
     * ViewHolder class holds the views for each item in the RecyclerView.
     * Avoids repeatedly calling findViewById, improving performance.
     */
    public static class SubmissionViewHolder extends RecyclerView.ViewHolder {
        // Declare the TextViews defined in submission_item.xml
        TextView textViewCompanyName, textViewDeadline;

        public SubmissionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the TextViews by their IDs within the inflated item view
            // Ensure these IDs (textViewCompanyName, textViewDeadline) match your submission_item.xml
            textViewCompanyName = itemView.findViewById(R.id.textViewCompanyName);
            textViewDeadline = itemView.findViewById(R.id.textViewDeadline);
        }
    }
}