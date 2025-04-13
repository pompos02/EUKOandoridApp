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

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder> {
    private List<SubmissionModel> submissionList;
    private AdminDashboardActivity context;

    public SubmissionAdapter(List<SubmissionModel> submissionList, AdminDashboardActivity context) {
        this.submissionList = submissionList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubmissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.submission_item, parent, false);
        return new SubmissionViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull SubmissionViewHolder holder, int position) {
        SubmissionModel submission = (SubmissionModel) submissionList.get(position);
        holder.textViewProjectName.setText(submission.getProjectName());
        holder.textViewTechStack.setText(submission.getTechStack());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SubmissionDetailsActivity.class);
            intent.putExtra("DOCUMENT_ID", submission.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return submissionList.size();
    }

    public void setSubmissions(ArrayList<SubmissionModel> submissions) {
        this.submissionList = submissions;
        notifyDataSetChanged();
    }

    static class SubmissionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewProjectName, textViewTechStack;

        public SubmissionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
            textViewTechStack = itemView.findViewById(R.id.textViewTechStack);
        }
    }
}
