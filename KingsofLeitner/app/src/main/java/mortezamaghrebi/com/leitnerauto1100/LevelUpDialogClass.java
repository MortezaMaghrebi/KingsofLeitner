package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LevelUpDialogClass extends Dialog  {

    public Activity c;
    Context context;
    Controller controller;
    MediaPlayer mp_win;

    public LevelUpDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
        controller = new Controller(context,false);
    }
     RelativeLayout btnok;
     TextView txtLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_newlevel);
        btnok = (RelativeLayout)findViewById(R.id.lytok);
        txtLevel = (TextView) findViewById(R.id.txtlevel);
        txtLevel.setText(""+controller.getLevel());
        mp_win = MediaPlayer.create(context, R.raw.levelup);
        mp_win.seekTo(0);mp_win.start();
        controller.setLevelUpShown(controller.getLevel());
        controller = null;
        try {
            ((Dialog) LevelUpDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                        LevelUpDialogClass.this.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        try{
            mp_win.stop();
            mp_win.release();
        }catch (Exception e){}
        super.onStop();
    }
}