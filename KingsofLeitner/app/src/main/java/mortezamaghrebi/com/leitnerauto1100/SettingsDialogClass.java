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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsDialogClass extends Dialog  {

    public SecondActivity c;
    Context context;
    Controller controller;
    Boolean decreaseheart=false;
    public SettingsDialogClass(SecondActivity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.context= a;
        controller = new Controller(context,false);
    }
     RelativeLayout btnok;
    TextView txttimelearn,txttimetest;
    SeekBar seekmain,seekgame,seekbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings);
        Handler timerHandler = new Handler(Looper.getMainLooper());
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                btnok = (RelativeLayout)findViewById(R.id.lytok);
                txttimelearn=(TextView)findViewById(R.id.txttimelearn);
                txttimetest=(TextView)findViewById(R.id.txttimetest);
                txttimelearn.setText(""+controller.getTimeLearn());
                txttimetest.setText(""+controller.getTimeTest());
                seekbutton=(SeekBar)findViewById(R.id.seek3);
                seekgame=(SeekBar)findViewById(R.id.seek2);
                seekmain=(SeekBar)findViewById(R.id.seek1);
                seekgame.setProgress(controller.getVolumeGame());
                seekmain.setProgress(controller.getVolumeMain());
                seekbutton.setProgress(controller.getVolumeButtons());
                try {
                    ((Dialog) SettingsDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                                int lt=25;
                                int tt=40;
                                int isok=0;
                                try {
                                    lt=Integer.parseInt(txttimelearn.getText().toString());
                                    if(lt>5)
                                    {
                                        isok++;
                                    }
                                    else Toast.makeText(context,"Time must not be less than 5 seconds!",Toast.LENGTH_SHORT).show();
                                }catch (Exception e)
                                {
                                    Toast.makeText(context,"Please enter learn time per question.",Toast.LENGTH_SHORT).show();
                                }
                                try {
                                    tt=Integer.parseInt(txttimetest.getText().toString());
                                    if(tt>5)
                                    {
                                        isok++;
                                    }
                                    else Toast.makeText(context,"Time must not be less than 5 seconds!",Toast.LENGTH_SHORT).show();
                                }catch (Exception e)
                                {
                                    Toast.makeText(context,"Please enter test time per question.",Toast.LENGTH_SHORT).show();
                                }
                                if(isok==2)
                                {
                                    controller.setTimeLearn(lt);
                                    controller.setTimeTest(tt);
                                    controller.setVolumeButtons(seekbutton.getProgress());
                                    controller.setVolumeMain(seekmain.getProgress());
                                    controller.setVolumeGame(seekgame.getProgress());
                                    c.setVolumes();
                                    SettingsDialogClass.this.dismiss();
                                }
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