package com.example.trialproject.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trialproject.DbQuery;
import com.example.trialproject.Models.TestModel;
import com.example.trialproject.R;
import com.example.trialproject.StartTestActivity;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.viewHolder> {

    private List<TestModel> testList;

    public TestAdapter(List<TestModel>testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.viewHolder holder, int position) {
        int progress=testList.get(position).getTopScore();
        holder.setData(position,progress);

    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView testNo;
        private TextView topScore;
        private ProgressBar progressBar;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            testNo=itemView.findViewById(R.id.testno);
            topScore=itemView.findViewById(R.id.scoretext);
            progressBar=itemView.findViewById(R.id.testprogressBar);

        }

       private void setData(int pos,int progress){
            testNo.setText("Test No : "+String.valueOf(pos +1));
            topScore.setText(String.valueOf(progress) + "%");
            progressBar.setProgress(progress);

           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    DbQuery.g_selected_test_index=pos;

                   Intent intent=new Intent(itemView.getContext(), StartTestActivity.class);
                   itemView.getContext().startActivity(intent);
               }
           });

       }
    }
}
