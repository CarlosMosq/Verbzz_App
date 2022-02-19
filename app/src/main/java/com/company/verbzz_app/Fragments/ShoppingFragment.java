package com.company.verbzz_app.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.company.verbzz_app.R;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {

    BillingClient billingClient;
    TextView disableAds;
    Button priceButton;
    Activity activity;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        disableAds = view.findViewById(R.id.disableAds);
        priceButton = view.findViewById(R.id.priceButton);
        activity = getActivity();

        billingClient = BillingClient
                .newBuilder(view.getContext())
                .enablePendingPurchases()
                .setListener((billingResult, list) -> {
                    if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                        for(Purchase purchase : list) {
                            if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED
                            && !purchase.isAcknowledged()) {
                                //finish setting up communication with firebase
                            }
                        }
                    }
                })
                .build();
        connectToGooglePlayBilling();


        return view;
    }


    private void connectToGooglePlayBilling() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                connectToGooglePlayBilling();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    getProductDetails();
                }
            }
        });
    }

    private void getProductDetails() {
        List<String> productIds = new ArrayList<>();
        productIds.add("remove_ads");
        SkuDetailsParams getProductDetailsQuery = SkuDetailsParams
                .newBuilder()
                .setSkusList(productIds)
                .setType(BillingClient.SkuType.INAPP)
                .build();
        billingClient.querySkuDetailsAsync(getProductDetailsQuery, (billingResult, list) -> {
            if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                SkuDetails itemsInfo = list.get(0);
                disableAds.setText(getString(R.string.disable));
                priceButton.setText(itemsInfo.getPrice());
                priceButton.setOnClickListener(view ->
                        billingClient.launchBillingFlow(activity
                        , BillingFlowParams
                                .newBuilder()
                                .setSkuDetails(itemsInfo)
                                .build()));
            }
        });
    }

}