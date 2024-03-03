package com.example.trialproject.Adapters;

import static com.example.trialproject.DbQuery.ANSWERED;
import static com.example.trialproject.DbQuery.NOT_VISITED;
import static com.example.trialproject.DbQuery.REVIEW;
import static com.example.trialproject.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.trialproject.DbQuery;
import com.example.trialproject.QuestionsActivity;
import com.example.trialproject.R;

public class QuestionGridAdapter extends BaseAdapter {


    private Context context;
    private int noOfQues;


    public QuestionGridAdapter(Context context,int noOfQues) {
        this.noOfQues = noOfQues;
        this.context=context;
    }

    @Override
    public int getCount() {
        return noOfQues;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View myview;
        if (view==null)
        {
            myview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item,viewGroup,false);
        }
        else
        {
            myview=view;
        }

        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof QuestionsActivity)
                    ((QuestionsActivity)context).goToQuestion(i);
            }
        });

        TextView quesTV=myview.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(i+1));

        //Log.d("LOGGGGGGGGGGGG",String.valueOf(DbQuery.g_quesList.get(i).getStatus()));

        switch (DbQuery.g_quesList.get(i).getStatus())
        {
            case NOT_VISITED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.grey)));
                 break;
            case UNANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.red)));
                break;
            case ANSWERED :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.green)));
                break;
            case REVIEW :
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myview.getContext(),R.color.pink)));
                break;
            default:
                break;

        }


        return myview;
    }
}
