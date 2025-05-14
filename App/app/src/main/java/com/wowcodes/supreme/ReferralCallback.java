package com.wowcodes.supreme;

import com.wowcodes.supreme.Activity.referralCard;

import java.util.ArrayList;

public interface ReferralCallback {
        void onReferralsLoaded(ArrayList<referralCard> list);
        void onReferralsFailure(String errorMessage);
    }
