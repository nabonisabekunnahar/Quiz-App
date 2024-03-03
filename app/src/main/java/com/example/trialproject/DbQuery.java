package com.example.trialproject;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.example.trialproject.Models.CategoryModel;
import com.example.trialproject.Models.ProfileModel;
import com.example.trialproject.Models.QuestionModel;
import com.example.trialproject.Models.RankModel;
import com.example.trialproject.Models.TestModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
    public static FirebaseFirestore g_firestore;

    public static List<CategoryModel> g_categoryModelList = new ArrayList<>();

    public static int g_selected_cat_index=0;

    public static List<TestModel> g_testList=new ArrayList<>();

    public static int g_selected_test_index=0;

    public static List<QuestionModel> g_quesList=new ArrayList<>();

    public static List<RankModel> g_usersList=new ArrayList<>();

    public static int g_usersCount=0;

    public static boolean isMeOnTopList=false;

    public static ProfileModel myProfile=new ProfileModel("NA",null,null);

    public static RankModel myperformance=new RankModel("NULL",0,-1);

    public static final int NOT_VISITED=0;
    public static final int UNANSWERED=1;
    public static final int ANSWERED=2;
    public static final int REVIEW=3;


    public static void createUserData(String email, String name, MyCompleteListener completeListener) {

        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }

    public static void saveProfileData(String name,String roll,MyCompleteListener completeListener)
    {
        Map<String,Object> profileData=new ArrayMap<>();
        profileData.put("NAME",name);

        if (roll != null)
            profileData.put("ROLL",roll);

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        myProfile.setName(name);
                        if (roll != null)
                            myProfile.setRoll(roll);

                        completeListener.onSuccess();
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       completeListener.onFailure();
                    }
                });

    }

    public static void getUserData(final MyCompleteListener completeListener)
    {
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL_ID"));

                        if (documentSnapshot.getString("ROLL") != null)
                            myProfile.setRoll(documentSnapshot.getString("ROLL"));

                        myperformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());

                        myperformance.setName(documentSnapshot.getString("NAME"));

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


    public static void loadMyScores(MyCompleteListener completeListener)
    {

        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        for (int i=0;i<g_testList.size();i++)
                        {
                            int top=0;
                            if (documentSnapshot.get(g_testList.get(i).getTestID()) != null)
                            {

                                top=documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }

                            g_testList.get(i).setTopScore(top);

                        }

                        completeListener.onSuccess();

                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }

    public static void getTopUsers(final MyCompleteListener completeListener)
    {
        g_usersList.clear();
        String myUID=FirebaseAuth.getInstance().getUid();
        g_firestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE",0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                          int rank=1;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            g_usersList.add(new RankModel(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));

                            if (myUID.compareTo(doc.getId()) == 0)
                            {

                                isMeOnTopList=true;
                                myperformance.setRank(rank);
                            }

                            rank++;

                        }

                        completeListener.onSuccess();

                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

    }

    public static void getUsersCount(MyCompleteListener completeListener)
    {
        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        g_usersCount=documentSnapshot.getLong("COUNT").intValue();


                        completeListener.onSuccess();

                    }
                }).
                     addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void saveResult(int score,MyCompleteListener completeListener)
    {
        WriteBatch batch=g_firestore.batch();
        DocumentReference userDoc=g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        batch.update(userDoc,"TOTAL_SCORE",score);
        if (score>g_testList.get(g_selected_test_index).getTopScore())
        {
            DocumentReference scoreDoc=userDoc.collection("USER_DATA").document("MY_SCORES");

            Map<String,Object> testdata=new ArrayMap<>();
            testdata.put(g_testList.get(g_selected_test_index).getTestID(),score);

            batch.set(scoreDoc,testdata, SetOptions.merge());

        }
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (score > g_testList.get(g_selected_test_index).getTopScore())
                    g_testList.get(g_selected_test_index).setTopScore(score);

                myperformance.setScore(score);

                completeListener.onSuccess();
            }
        }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();
                    }
                });


    }

    public static void loadCategories(final MyCompleteListener completeListener) {
        g_categoryModelList.clear();
        g_firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            docList.put(doc.getId(), doc);
                        }

                        QueryDocumentSnapshot catListDoc = docList.get("Categories");
                        if (catListDoc != null) {
                            Long catCount = catListDoc.getLong("COUNT");
                            if (catCount != null) {
                                for (int i = 1; i <= catCount; i++) {
                                    String catID = catListDoc.getString("CAT" + i + "_ID");
                                    QueryDocumentSnapshot catDoc = docList.get(catID);
                                    if (catDoc != null) {
                                        Long noOfTests = catDoc.getLong("NO_OF_TESTS");
                                        if (noOfTests != null) {
                                            int noOfTest = noOfTests.intValue();
                                            String catName = catDoc.getString("NAME");
                                            g_categoryModelList.add(new CategoryModel(catID, catName, noOfTest));
                                        }
                                    }
                                }
                            }
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadQuestions(final MyCompleteListener completeListener)
    {
        g_quesList.clear();
        g_firestore.collection("Questions").whereEqualTo("CATEGORY",g_categoryModelList.get(g_selected_cat_index).getDocId())
                .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot doc:queryDocumentSnapshots)
                        {
                            g_quesList.add(new QuestionModel(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),-1,
                                    NOT_VISITED
                            ));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();
                    }
                });

    }

    public static void loadTestData(final MyCompleteListener completeListener)
    {
        g_testList.clear();
        g_firestore.collection("QUIZ").document(g_categoryModelList.get(g_selected_cat_index).getDocId())
                .collection("TESTS_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int noOfTests=g_categoryModelList.get(g_selected_cat_index).getNoOfTests();

                        for (int i=1;i<=noOfTests;i++)
                        {
                            g_testList.add(new TestModel(
                                    documentSnapshot.getString("TEST" + String.valueOf(i) + "_ID"),
                                    0,
                                    documentSnapshot.getLong("TEST" + String.valueOf(i) +"_TIME").intValue()
                            ));

                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       completeListener.onFailure();
                    }
                });
    }

    public static void loadData(MyCompleteListener completeListener)
    {
        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(completeListener);
                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }
}