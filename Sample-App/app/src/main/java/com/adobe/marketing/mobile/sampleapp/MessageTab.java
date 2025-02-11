package com.adobe.marketing.mobile.sampleapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.edge.identity.Identity;

import java.util.HashMap;
import java.util.Map;

public class MessageTab extends Fragment {
    TextView tvECID;
    Button btnFullScreenMessage;
    Button btnModalMessage;
    Button btnTopBanner;
    Button btnBottomBanner;
    Toast toast = null;

    public void showToast(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                }
                toast = Toast.makeText(MainApp.getAppContext(), message, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private static final String LOG_TAG = "Assurance Tab";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messaging_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvECID = view.findViewById(R.id.tv_lbl_ecidLabel);

        btnFullScreenMessage = getView().findViewById(R.id.btn_fullScreenMessage);
        btnFullScreenMessage.setOnClickListener(trackInAppMessage("sampleAppFullScreen"));

        btnModalMessage = getView().findViewById(R.id.btn_modalMessage);
        btnModalMessage.setOnClickListener(trackInAppMessage("sampleAppModal"));

        btnTopBanner = getView().findViewById(R.id.btn_topBanner);
        btnTopBanner.setOnClickListener(trackInAppMessage("sampleAppBannerTop"));

        btnBottomBanner = getView().findViewById(R.id.btn_bottomBanner);
        btnBottomBanner.setOnClickListener(trackInAppMessage("sampleAppBannerBottom"));
    }

    @NonNull
    private View.OnClickListener trackInAppMessage(String actionName) {
        return v -> {
            MobileCore.trackAction(actionName, null);
            showToast("Track action \"" + actionName + "\" triggered");
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();

        if (activity == null) return;

        Identity.getExperienceCloudId(new AdobeCallback<String>() {
            @Override
            public void call(final String ecid) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.format("Messaging SDK setup is complete with ECID - %s. \n\nFor more details please take a look at the documentation in the github repository.", ecid);
                        tvECID.setText(text);
                    }
                });
            }
        });
    }
}
