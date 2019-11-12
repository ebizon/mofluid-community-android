package com.ebizon.fluid.Utils;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by manish on 02/02/16.
 */
public class ViewUtils {
    public static void setToTextView(View rootView, int id, String text) {
        TextView textView = (TextView) rootView.findViewById(id);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public static void setToEditText(View rootView, int id, String text) {
        EditText textView = (EditText) rootView.findViewById(id);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public static String getTextViewText(View rootView, int id) {
        TextView textView = (TextView) rootView.findViewById(id);
        if (textView != null) {
            return textView.getText().toString();
        }

        return "";
    }

    public static String getEditTextText(View rootView, int id) {
        EditText textView = (EditText) rootView.findViewById(id);
        if (textView != null) {
            return textView.getText().toString();
        }

        return "";
    }

    public static void setTextViewVisibility(View rootView, int id, int visibility) {
        TextView textView = (TextView) rootView.findViewById(id);
        if (textView != null) {
            textView.setVisibility(visibility);
        }
    }

    public static void setEditTextVisibility(View rootView, int id, int visibility) {
        EditText textView = (EditText) rootView.findViewById(id);
        if (textView != null) {
            textView.setVisibility(visibility);
        }
    }

    public static void setTextViewEnabled(View rootView, int id, boolean enabled) {
        TextView textView = (TextView) rootView.findViewById(id);
        if (textView != null) {
            textView.setEnabled(enabled);
        }
    }

    public static void setTextViewFont(View rootView, int id, Typeface font) {
        TextView textView = (TextView) rootView.findViewById(id);
        if (textView != null) {
            textView.setTypeface(font);
        }
    }

    public static void setAllTextViewFont(final View rootView, Typeface font) {
        try {
            ViewGroup viewGroup = (ViewGroup) rootView;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTypeface(font);
                }else{
                    ViewUtils.setAllTextViewFont(child, font);
                }
            }
        }catch(Exception e){
        }
    }
}
