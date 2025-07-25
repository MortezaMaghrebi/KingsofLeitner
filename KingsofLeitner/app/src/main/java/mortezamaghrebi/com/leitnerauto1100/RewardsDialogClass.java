package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RewardsDialogClass extends Dialog  {

    public Activity c;
    Context context;
    Controller controller;
    Class activity;
    Boolean decreaseheart=false;
    public RewardsDialogClass(Activity a,Class activity,boolean decreaseheart) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
        this.activity=activity;
        this.decreaseheart = decreaseheart;
        controller = new Controller(context,false);
    }
     RelativeLayout btnok,lytnoheart,lytnopotion;
    TextView txtexcellent,txtsoso,txtbad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rewards);
        Handler timerHandler = new Handler(Looper.getMainLooper());
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                btnok = (RelativeLayout)findViewById(R.id.lytok);
                txtbad=(TextView)findViewById(R.id.txtbad);
                txtexcellent=(TextView)findViewById(R.id.txtexcelent);
                txtsoso=(TextView)findViewById(R.id.txtsoso);
                txtexcellent.setText(""+controller.RewardExcelent+"+ Corrects" );
                txtsoso.setText(""+controller.RewardSoSo+"+ Corrects" );
                txtbad.setText(""+(controller.RewardBad-1)+"- Corrects" );
                try {
                    ((Dialog) RewardsDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                                Intent intent = new Intent(c, activity);
                                c.startActivity(intent);
                                c.overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                                if(decreaseheart) controller.decreaseheart();
                                RewardsDialogClass.this.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
            }
        };
        timerHandler.postDelayed(timerRunnable, 20);
    }

}