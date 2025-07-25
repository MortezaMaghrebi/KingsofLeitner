package mortezamaghrebi.com.leitnerauto1100;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import java.io.UnsupportedEncodingException;

public class NoInternetDialogClass extends Dialog  {

    public MainActivity c;
    Context context;
    Controller controller;
    int times=0;
    public NoInternetDialogClass(MainActivity a,Controller controller) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.controller=controller;
        this.context= a;
    }
     RelativeLayout btnok,btnoffline,lytnointernet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_nointernet);
        btnok = (RelativeLayout)findViewById(R.id.lytok);
        btnoffline=(RelativeLayout)findViewById(R.id.lytofflinework);
        lytnointernet=(RelativeLayout)findViewById(R.id.lytnointernet);

        try {
            ((Dialog) NoInternetDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ((Dialog)NoInternetDialogClass.this).setCancelable(false);
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
                        try {
                            c.CheckInternet(false);
                            times++;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        NoInternetDialogClass.this.dismiss();
                        return true;
                }
                return false;
            }
        });
        btnoffline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnoffline.setBackgroundResource(R.drawable.outline_button1b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnoffline.setBackgroundResource(R.drawable.outline_button1);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnoffline.setBackgroundResource(R.drawable.outline_button1);
                        try {
                            c.CheckInternet(true);
                            times++;
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        NoInternetDialogClass.this.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        if(times>2)
        {
            if(controller.getDBVersion()>0) {
                lytnointernet.setVisibility(View.INVISIBLE);
                btnoffline.setVisibility(View.VISIBLE);
            }
        }
        super.onStart();
    }
}