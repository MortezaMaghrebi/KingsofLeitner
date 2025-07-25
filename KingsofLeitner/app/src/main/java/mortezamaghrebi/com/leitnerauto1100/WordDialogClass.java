package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class WordDialogClass extends Dialog  {

    public Activity c;
    Context context;
    Controller controller;
    Class activity;
    Boolean decreaseheart=false;
    wordItem witem;
    public WordDialogClass(Activity a,wordItem witem,Controller controller) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.witem=witem;
        this.context= a;
        this.controller=controller;
    }
    RelativeLayout btnok;
    TextView txtword,txtcontent;
    ImageView imgword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_word);
        Handler timerHandler = new Handler(Looper.getMainLooper());
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                btnok = (RelativeLayout)findViewById(R.id.lytok);
                txtword=(TextView)findViewById(R.id.txtword);
                txtcontent=(TextView)findViewById(R.id.txtcontent);
                txtword.setText(witem.word);
                txtcontent.setText(witem.pronounce+"\n"+witem.definition+"\n"+ witem.persian+"\nExample: "+witem.example.replace("_______",witem.word)+"\n"+witem.examplefa);
                imgword=(ImageView)findViewById(R.id.imgword);
                try {
                    getImage(witem.word);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    ((Dialog) WordDialogClass.this).getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                                WordDialogClass.this.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
            }
        };
        timerHandler.postDelayed(timerRunnable, 20);
    }

    final String uri_getimage = "http://kingsofleitner.ir/words1100/webservice.php?get_command=getwordimage,";

    void getImage(final String word) throws UnsupportedEncodingException {
        Bitmap bit =controller.getWordImage(word);
        if(bit!=null) {
            Display display = c.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            imgword.getLayoutParams().height=size.x/2;
            imgword.getLayoutParams().width=size.x/2;
            imgword.requestLayout();
            imgword.setImageBitmap(bit);
        }else {
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest postRequest = new StringRequest(Request.Method.POST, uri_getimage + word,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                if (response.length() > 200) {

                                    Bitmap bit = controller.setWordImage(word,response);
                                    if(bit!=null) {
                                        imgword.setImageBitmap(bit);
                                        Display display = c.getWindowManager().getDefaultDisplay();
                                        Point size = new Point();
                                        display.getSize(size);
                                        imgword.getLayoutParams().height = size.x / 2;
                                        imgword.getLayoutParams().width = size.x / 2;
                                        imgword.requestLayout();
                                    }
                                }
                            } catch (Exception e) {
                                int a = 1;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            //Toast.makeText(SecondActivity.this,"Error\n"+"Could not connect to server",Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(postRequest);
        }
    }

}