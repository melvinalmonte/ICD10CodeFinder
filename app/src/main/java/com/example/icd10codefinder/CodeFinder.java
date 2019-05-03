package com.example.icd10codefinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;


public class CodeFinder extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter codeAdapter;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    Button searchButton;
    EditText searchBar;
    ArrayList<String> diagnosis = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_finder);

        recyclerView = findViewById(R.id.diagnosisList);
        searchButton = findViewById(R.id.searchButton);
        searchBar = findViewById(R.id.searchBar);
        final RequestQueue queue = Volley.newRequestQueue(this);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ICD10Search = searchBar.getText().toString();
                diagnosis.clear();
                String url ="https://clinicaltables.nlm.nih.gov/api/icd10cm/v3/search?sf=code,name&terms=" + ICD10Search ;
                if (ICD10Search.isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(CodeFinder.this).create();
                    alertDialog.setTitle("Warning");
                    alertDialog.setMessage("No Input Detected: Please Enter your Diagnosis in the Search Bar.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CONTINUE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String resolveResponse = response.replaceAll("[-+^\\[\\]\"]", "");
                                    String[] parseCodes = resolveResponse.split(",");
                                    int counterDiagnosis = parseCodes.length - 1;
                                    while (!parseCodes[counterDiagnosis].equals("null")){
                                        diagnosis.add(parseCodes[counterDiagnosis]);
                                        counterDiagnosis --;
                                    }

                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(layoutManager);
                                    codeAdapter = new MyAdapter(diagnosis);
                                    recyclerView.setAdapter(codeAdapter);

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            diagnosis.add("Sorry, this service is currently unavailable. Please Please Check Your " +
                                    "Internet Connection and Try Again");
                        }
                    });
                    queue.add(stringRequest);
                }

            }
        });

    }
}
