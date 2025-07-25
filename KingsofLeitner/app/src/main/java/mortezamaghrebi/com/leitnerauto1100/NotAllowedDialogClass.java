package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class NotAllowedDialogClass extends Dialog  {

    public Activity c;
    Context context;
    Controller controller;
    int times=0;
    public NotAllowedDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
    }
     RelativeLayout btnok,btnoffline,lytnointernet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notallowed);
        btnok = (RelativeLayout)findViewById(R.id.lytok);
        try {
            ((Dialog) NotAllowedDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ((Dialog)NotAllowedDialogClass.this).setCancelable(false);
        }catch (Exception e){}
        btnok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnok.setBackgroundResource(R.drawable.outline_button1b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnok.setBackgroundResource(R.drawable.outline_button1);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnok.setBackgroundResource(R.drawable.outline_button1);
                        NotAllowedDialogClass.this.dismiss();
                        return true;
                }
                return false;
            }
        });

    }


}