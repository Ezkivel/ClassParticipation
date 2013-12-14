package edu.unitec.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by Henry on 12-13-13.
 */
public class CapitalizedTextView  extends TextView implements ViewTreeObserver.OnPreDrawListener {

    public CapitalizedTextView(Context context) {
        super(context);
    }

    public CapitalizedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CapitalizedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text.toString().toUpperCase(), type);
    }

}
