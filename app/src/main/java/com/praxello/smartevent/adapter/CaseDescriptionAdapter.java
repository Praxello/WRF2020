package com.praxello.smartevent.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.praxello.smartevent.R;
import com.praxello.smartevent.activity.CaseDescriptionActivity;
import com.praxello.smartevent.model.NotificationData;
import com.praxello.smartevent.model.allcases.AllCasesData;
import com.praxello.smartevent.utility.CommonMethods;
import com.praxello.smartevent.utility.ConfiUrl;
import com.praxello.smartevent.utility.AllKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaseDescriptionAdapter extends RecyclerView.Adapter<CaseDescriptionAdapter.CaseDesciptionViewHolder> {

    public Context context;
    public ArrayList<AllCasesData> allCasesDataArrayList;

    public static final String TAG="CaseDescriptionAdapter";

    public CaseDescriptionAdapter(Context context, ArrayList<AllCasesData> allCasesDataArrayList) {
        this.context = context;
        this.allCasesDataArrayList = allCasesDataArrayList;
    }

    @NonNull
    @Override
    public CaseDesciptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.case_description_row, parent, false);
        return new CaseDesciptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaseDesciptionViewHolder holder, int position) {

        holder.tvTitle.setText(allCasesDataArrayList.get(position).getCaseTitle());
        holder.tvSummary.setText(allCasesDataArrayList.get(position).getCaseDetails());

        holder.webView.getSettings().setLoadsImagesAutomatically(true);
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setBuiltInZoomControls(true);
        holder.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if(allCasesDataArrayList.get(position).getPdflink()==null || allCasesDataArrayList.get(position).getPdflink().isEmpty()
                || allCasesDataArrayList.get(position).getPdflink().equals("null")){
            Toast.makeText(context, "Pdf not available", Toast.LENGTH_SHORT).show();
        }else{
            //holder.webView.loadUrl("https://docs.google.com/viewer?url="+allCasesDataArrayList.get(position).getPdflink());
            holder.webView.loadUrl(allCasesDataArrayList.get(position).getPdflink());
        }

        holder.btnDiagonisis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //builder.setCancelable(false);
                ViewGroup viewGroup = ((Activity)context).findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottom_sheet_row, viewGroup, false);

                EditText etSuggesstion=dialogView.findViewById(R.id.et_suggestion);
                AppCompatButton btnSubmitSuggesstion=dialogView.findViewById(R.id.btn_submit);

                if(allCasesDataArrayList.get(position).getSubmission()==null || allCasesDataArrayList.get(position).getSubmission().isEmpty() ){
                    etSuggesstion.setText(null);

                }else{
                    etSuggesstion.setText(allCasesDataArrayList.get(position).getSubmission());
                }

                btnSubmitSuggesstion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etSuggesstion.getText().toString().isEmpty()){
                            Toast.makeText(context, "Suggesstion required!", Toast.LENGTH_LONG).show();
                        }else{
                            submitSuggesstion(etSuggesstion.getText().toString(),allCasesDataArrayList.get(position).getCaseId());
                        }
                    }
                });

                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        /*holder.ivPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCasesDataArrayList.get(position).getPdflink()==null || allCasesDataArrayList.get(position).getPdflink().isEmpty()
                || allCasesDataArrayList.get(position).getPdflink().equals("null")){
                    Toast.makeText(context, "Pdf not available", Toast.LENGTH_SHORT).show();
                }else{
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, WebviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title","PDF Viewer");
                    intent.putExtra("type","PDF");
                    intent.putExtra("url",allCasesDataArrayList.get(position).getPdflink());
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            }
        });*/

       /* holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

       /* holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCasesDataArrayList.get(position).getPhotoUrl()==null || allCasesDataArrayList.get(position).getPhotoUrl().isEmpty()
                        || allCasesDataArrayList.get(position).getPhotoUrl().equals("null")){
                    Toast.makeText(context, "Photo not available", Toast.LENGTH_SHORT).show();
                }else{
                    Activity activity = (Activity) context;
                    Intent intent=new Intent(context, WebviewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("title","Photo Viewer");
                    //intent.putExtra("type","PDF");
                    intent.putExtra("url",allCasesDataArrayList.get(position).getPhotoUrl());
                    context.startActivity(intent);
                    activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                }
            }
        });

        holder.ivLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }



    @Override
    public int getItemCount() {
        return allCasesDataArrayList.size();
    }


    public class CaseDesciptionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        public TextView tvTitle;
        @BindView(R.id.tv_summary)
        public TextView tvSummary;
        @BindView(R.id.webView)
        public WebView webView;
        @BindView(R.id.btn_diagonisis)
        public AppCompatButton btnDiagonisis;
        /*@BindView(R.id.iv_pdf)
        public ImageView ivPdf;
        @BindView(R.id.iv_video)
        public ImageView ivVideo;
        @BindView(R.id.iv_photo)
        public ImageView ivPhoto;
        @BindView(R.id.iv_link)
        public ImageView ivLink;*/

        public CaseDesciptionViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

    public void submitSuggesstion(String suggesstion,String caseId){
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please wait");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfiUrl.SAVE_CASE_DIAGNOSIS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();

                NotificationData notificationData = gson.fromJson(response, NotificationData.class);

                Log.e(TAG, "onResponse: "+response );
                if (notificationData.getResponsecode().equals("200")) {
                    progress.dismiss();
                    Toast.makeText(context, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, CaseDescriptionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                   /* llForgotPassword.setVisibility(View.GONE);
                    llCreateNewPassword.setVisibility(View.VISIBLE);*/
                } else {
                    progress.dismiss();
                    Toast.makeText(context, notificationData.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("userid", CommonMethods.getPrefrence(context, AllKeys.USER_ID));
                params.put("caseid", caseId);
                params.put("details", suggesstion);
                Log.e(TAG, "getParams: " + params);
                return params;
            }
        };
        RequestQueue mQueue = Volley.newRequestQueue(context);
        mQueue.add(stringRequest);
    }
}
