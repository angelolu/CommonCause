package com.exceptionlabs.cc.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exceptionlabs.cc.R;
import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by Angelo on 2/4/2017.
 */

public class DashboardIconView extends CardView {
    private String mLabel = ""; // TODO: use a default from R.string...
    private int mColour = ContextCompat.getColor(getContext(), R.color.colorPrimary); // TODO: use a default from R.color...
    private View mIconView;
    private int sPayload = 0;
    private TextView tvLabel, tvShort;
    private RelativeLayout rlButton;
    public static final int ANIM_DURATION = 200;

    public DashboardIconView(Context context) {
        super(context, null, 0);
        initialize(context, null, 0);
    }

    public DashboardIconView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initialize(context, attrs, 0);
    }

    public DashboardIconView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs, defStyle);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mIconView = inflater.inflate(R.layout.dashboard_icon, this, true);
        tvLabel = (TextView) mIconView.findViewById(R.id.tvLabel);
        tvShort = (TextView) mIconView.findViewById(R.id.tvButton);
        rlButton = (RelativeLayout) mIconView.findViewById(R.id.rlButton);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DashboardIconView, defStyle, 0);

        mLabel = a.getString(
                R.styleable.DashboardIconView_cc_label);
        mColour = a.getColor(
                R.styleable.DashboardIconView_cc_background,
                mColour);

        a.recycle();


        setUseCompatPadding(true);
        setRadius(getResources().getDimensionPixelSize(R.dimen.card_corner_radius));
        setClickable(true);

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

        setForeground(ContextCompat.getDrawable(context, outValue.resourceId));
        setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
        setPreventCornerOverlap(false);

        // Update View
        invalidateProperties();
    }

    private void invalidateProperties() {
        tvLabel.setText(mLabel);
        if (mLabel != null && !mLabel.equals("")) {
            tvShort.setText(mLabel.substring(0, 1));
        }
        rlButton.setBackgroundColor(mColour);
    }

    /**
     * Use sparingly.
     */

    public String getmLabel() {
        return mLabel;
    }

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
        invalidateProperties();
    }

    public int getPayload() {
        return sPayload;
    }

    public void setPayload(int sPayload) {
        this.sPayload = sPayload;
    }

    public int getColour() {
        return mColour;
    }

    public void setColour(int mColour) {
        this.mColour = mColour;
        invalidateProperties();
    }


    public void dismiss() {
        dismiss(false);
    }

    public void dismiss(boolean animate) {
        if (!animate) {
            setVisibility(View.GONE);
        } else {
            animate().scaleY(0.1f).alpha(0.1f).setDuration(ANIM_DURATION);
        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }
}
