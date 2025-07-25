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

public class NotEnoughDialogClass extends Dialog  {

    public Activity c;
    Context context;
    Controller controller;

    public NotEnoughDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
        controller = new Controller(context,false);
    }
     RelativeLayout btnok,lytnoheart,lytnopotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_notenough);
        btnok = (RelativeLayout)findViewById(R.id.lytok);
        lytnoheart = (RelativeLayout)findViewById(R.id.lytnoheart);
        lytnopotion = (RelativeLayout)findViewById(R.id.lytnopotion);
        try {
            ((Dialog) NotEnoughDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){}
        if((controller.gethearts()+controller.getheartsBought())>0)
        {
            lytnoheart.getLayoutParams().height=0;
            lytnoheart.requestLayout();
        }
        if(controller.getExir()>0)
        {
            lytnopotion.getLayoutParams().height=0;
            lytnopotion.requestLayout();
        }
        controller = null;
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
                        NotEnoughDialogClass.this.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

}