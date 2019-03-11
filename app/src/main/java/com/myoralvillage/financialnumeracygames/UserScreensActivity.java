package com.myoralvillage.financialnumeracygames;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class UserScreensActivity extends AppCompatActivity {

    List<String> userNames = new ArrayList<>();
    boolean newProfile = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screens);
        DrawProfiles();
    }

    public void DrawProfiles() {

        if (12 - userNames.size() > 1) {
            LinearLayout ll = findViewById(R.id.unclaimedProfiles1);
            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
            llParams.weight = 0.3f;
            ll.setLayoutParams(llParams);
        }
        if (12 - userNames.size() > 7) {
            LinearLayout ll = findViewById(R.id.unclaimedProfiles2);
            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
            llParams.weight = 0.3f;
            ll.setLayoutParams(llParams);
        }
        if (userNames.size() > 5) {
            LinearLayout ll = findViewById(R.id.claimedProfiles2);
            LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
            llParams.weight = 0.3f;
            ll.setLayoutParams(llParams);
        }
        LinearLayout ll = findViewById(R.id.claimedProfiles1);
        LinearLayout.LayoutParams llParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
        llParams.weight = 0.3f;
        ll.setLayoutParams(llParams);

        int claimedCount = 0;
        int unclaimedCount = 0;

        for (int i = 1; i < 12; i++) {
            String filename = "user" + i;
            if (userNames.size() > 0) {
                if (userNames.contains(filename)) {
                    int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
                    claimedCount++;

                    String imgview_name = "claimedProfile" + claimedCount;


                    int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
                    ImageView iv = findViewById(res_id);
                    iv.setImageResource(img_id);
                    iv.setAlpha(0.5f);
                    iv.setVisibility(View.VISIBLE);
                    iv.setTag(filename);

                } else {
                    int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
                    unclaimedCount++;

                    String imgview_name = "unclaimedProfile" + unclaimedCount;

                    int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
                    ImageView iv = findViewById(res_id);
                    iv.setImageResource(img_id);
                    iv.setVisibility(View.VISIBLE);
                    iv.setTag(filename);
                }

            } else {
                int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());
                unclaimedCount++;

                String imgview_name = "unclaimedProfile" + unclaimedCount;

                int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
                ImageView iv = findViewById(res_id);
                iv.setImageResource(img_id);
                iv.setVisibility(View.VISIBLE);
                iv.setTag(filename);
            }
        }
        int adminId = userNames.size() + 1;
        String filename = "admin";
        int img_id = getResources().getIdentifier(filename, "drawable", getPackageName());

        String imgview_name = "claimedProfile" + adminId;
        int res_id = getResources().getIdentifier(imgview_name, "id", getPackageName());
        ImageView iv = (ImageView) findViewById(res_id);
        iv.setImageResource(img_id);
        iv.setAlpha(0.5f);
        iv.setVisibility(View.VISIBLE);
        iv.setTag(filename);
    }
}
