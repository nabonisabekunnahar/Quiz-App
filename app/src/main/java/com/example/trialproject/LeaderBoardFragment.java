package com.example.trialproject;

import static com.example.trialproject.DbQuery.g_usersCount;
import static com.example.trialproject.DbQuery.g_usersList;
import static com.example.trialproject.DbQuery.myperformance;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trialproject.Adapters.RankAdapter;


public class LeaderBoardFragment extends Fragment {

    private TextView totalUsersTV,myImgTextTV,myScoreTV,myRankTV;
    private RecyclerView usersView;

    private RankAdapter adapter;

    private Dialog progressDialog;

    private TextView dialogText;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeaderBoardFragment() {}

    public static LeaderBoardFragment newInstance(String param1, String param2) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_leader_board, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");

        initViews(view);

        progressDialog=new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");
        progressDialog.show();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(linearLayoutManager);

        adapter=new RankAdapter(DbQuery.g_usersList);

        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if (myperformance.getScore() != 0)
                {
                    if (! DbQuery.isMeOnTopList)
                    {
                        calculateRank();

                    }

                    myScoreTV.setText("Score : " + myperformance.getScore());
                    myRankTV.setText("Rank - " + myperformance.getRank());

                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Something went wrong ! PLease try again later !",
                        Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


        totalUsersTV.setText("Total users : " + DbQuery.g_usersCount);

        myImgTextTV.setText(myperformance.getName().toUpperCase().substring(0,1));

        return view;
    }

    private void initViews(View view)
    {
        totalUsersTV=view.findViewById(R.id.total_users);
        myImgTextTV=view.findViewById(R.id.img_text);
        myScoreTV=view.findViewById(R.id.total_score);
        myRankTV=view.findViewById(R.id.rank);
        usersView=view.findViewById(R.id.users_view);

    }

    private void calculateRank()
    {
       int lowTopScore=g_usersList.get(g_usersList.size()-1).getScore();

       int remaining_slots=g_usersCount-20;

       int mySlot=(myperformance.getScore()*remaining_slots)/lowTopScore;

       int rank;

       if (lowTopScore != myperformance.getScore())
       {
           rank=g_usersCount-mySlot;
       }
       else
       {
           rank=21;
       }

       myperformance.setRank(rank);

    }
}