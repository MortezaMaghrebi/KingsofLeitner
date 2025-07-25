package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import android.os.Bundle;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondActivity extends AppCompatActivity {

    TextView txtheart1, txtheart2, txtexir1, txtexir2, txtstar1, txtstar2, txtuser, txtavatarname, txtnummessages;
    RelativeLayout prgheart, prgexir, prgstar, prgref, btnmessages, btnusersetting, lytone;
    LinearLayout buyh1, buyh2, buyh3, buyp1, buyp2, buyp3, lyttwo, lytthree, lytbody,lytfour,lytfive;
    ImageView imghome, imgchat, imgmarket,imgsearch,imgusers, imgacceptavatar, imgacceptuser, imgnextavatar, imgprevavatar, imgavatarbuying,imgsynced,imginfo,imgsettings;
    ProgressBar prgavatar;
    Button btnsend;
    EditText txtsend,txtminlevel,txtmaxlevel,txtcountuser;
    ListView lstchats,lstusers;
    ImageButton btnrefresh;
    int progrefwidth = 0;
    Controller controller;
    private Handler mHandler;
    Date heartaddedtime, exirchangedtime;
    int HeartIncreasTime = 540; //seconds
    int HeartsMaximum = 12; //
    boolean heartmax = false;
    boolean exirzero = true;
    Boolean inited = false;
    MediaPlayer mPlayer;
    MediaPlayer mpbutton;// = MediaPlayer.create(this, R.raw.select2);

    ImageView imgavatar;
    //String urp = "http://kingsofleitner.ir/words1100/webservice.php?command=avatar,";
    final String uri_getimage = "http://kingsofleitner.ir/words1100/webservice.php?get_command=getimage,";
    final String uri_getavatar = "http://kingsofleitner.ir/words1100/webservice.php?get_command=getavatar,";
    final String url = "http://kingsofleitner.ir/words1100/payservice.php";
    final String uri = "http://kingsofleitner.ir/words1100/webservice.php";

    Boolean longs = false;
    int avatarindex = 1;

    void initControls() {
        mpbutton = MediaPlayer.create(SecondActivity.this, R.raw.clicksound);
        mpbutton.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
        txtheart1 = (TextView) findViewById(R.id.txtheart1);
        txtheart2 = (TextView) findViewById(R.id.txtheart2);
        txtexir1 = (TextView) findViewById(R.id.txtexir1);
        txtexir2 = (TextView) findViewById(R.id.txtexir2);
        txtstar1 = (TextView) findViewById(R.id.txtstar1);
        txtstar2 = (TextView) findViewById(R.id.txtstar2);
        txtuser = (TextView) findViewById(R.id.txtuser);
        txtavatarname = (TextView) findViewById(R.id.txtavatarname);
        txtnummessages = (TextView) findViewById(R.id.txtnummessages);
        prgref = (RelativeLayout) findViewById(R.id.prg_ref);
        prgheart = (RelativeLayout) findViewById(R.id.prgheart);
        prgexir = (RelativeLayout) findViewById(R.id.prgexir);
        prgstar = (RelativeLayout) findViewById(R.id.prgstar);
        btnmessages = (RelativeLayout) findViewById(R.id.btnmessages);
        btnusersetting = (RelativeLayout) findViewById(R.id.btnusersetting);
        imgchat = (ImageView) findViewById(R.id.imgchat);
        imghome = (ImageView) findViewById(R.id.imghome);
        imgmarket = (ImageView) findViewById(R.id.imgmarket);
        imgsearch = (ImageView) findViewById(R.id.imgsearch);
        imgusers = (ImageView) findViewById(R.id.imgusers);
        imgsynced = (ImageView) findViewById(R.id.imgsynced);
        imginfo = (ImageView) findViewById(R.id.btninfo);
        imgsettings = (ImageView) findViewById(R.id.btnsettings);
        lytone = (RelativeLayout) findViewById(R.id.lytone);
        lyttwo = (LinearLayout) findViewById(R.id.lyttwo);
        lytthree = (LinearLayout) findViewById(R.id.lytthree);
        lytfour = (LinearLayout) findViewById(R.id.lytfour);
        lytfive = (LinearLayout) findViewById(R.id.lytfive);
        lytbody = (LinearLayout) findViewById(R.id.lytbody);
        txtsend = (EditText) findViewById(R.id.txtsend);
        txtminlevel = (EditText) findViewById(R.id.txtminlevel);
        txtmaxlevel = (EditText) findViewById(R.id.txtmaxlevel);
        txtcountuser= (EditText) findViewById(R.id.txtcountusers);
        btnsend = (Button) findViewById(R.id.btnsend);
        btnrefresh = (ImageButton) findViewById(R.id.btnrefresh);
        lstchats = (ListView) findViewById(R.id.lstchat);
        lstusers = (ListView) findViewById(R.id.lstusers);
        imgavatar = (ImageView) findViewById(R.id.imgavatar);
        imgavatarbuying = (ImageView) findViewById(R.id.imgavatarbuy);
        imgnextavatar = (ImageView) findViewById(R.id.imgrightavatar);
        imgprevavatar = (ImageView) findViewById(R.id.imgleftavatar);
        imgacceptavatar = (ImageView) findViewById(R.id.imgacceptavatar);
        imgacceptuser = (ImageView) findViewById(R.id.imgacceptusers);
        prgavatar = (ProgressBar) findViewById(R.id.prgavatar);
        imgsynced.setVisibility(View.INVISIBLE);
        txtuser.setText(controller.getUser());
        progrefwidth = controller.getProgWidth();
        mHandler = new Handler();
        if (progrefwidth < 1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progrefwidth = prgref.getWidth();
                    if (progrefwidth > 1) controller.setProgWidth(progrefwidth);
                }
            }, 4000);
        }
        inited = true;
        initBuyControls();

        imghome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                int height = lytbody.getHeight();
                lyttwo.getLayoutParams().height = 0;
                lytthree.getLayoutParams().height = 0;
                lytfour.getLayoutParams().height = 0;
                lytfive.getLayoutParams().height = 0;
                lytfive.requestLayout();
                imgusers.setImageResource(R.drawable.userss);
                lytone.getLayoutParams().height = height;
                lyttwo.requestLayout();
                lytone.requestLayout();
                lytfour.requestLayout();
                lytthree.requestLayout();
                imghome.setImageResource(R.drawable.homee2);
                imgchat.setImageResource(R.drawable.chatt);
                imgmarket.setImageResource(R.drawable.baskett);
                imgsearch.setImageResource(R.drawable.searchh);
                bookcontentlayout=0;
            }
        });
        imgmarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                int height = lytbody.getHeight();
                lyttwo.getLayoutParams().height = height;
                lytone.getLayoutParams().height = 0;
                lytthree.getLayoutParams().height = 0;
                lytfour.getLayoutParams().height = 0;
                lytfive.getLayoutParams().height = 0;
                lytfive.requestLayout();
                imgusers.setImageResource(R.drawable.userss);
                lyttwo.requestLayout();
                lytone.requestLayout();
                lytthree.requestLayout();
                lytfour.requestLayout();
                imghome.setImageResource(R.drawable.homee);
                imgchat.setImageResource(R.drawable.chatt);
                imgmarket.setImageResource(R.drawable.baskett1);
                imgsearch.setImageResource(R.drawable.searchh);prgavatar.setVisibility(View.INVISIBLE);
                try {
                    getAvatar(avatarindex);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                bookcontentlayout=0;
            }
        });
        imgchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                int height = lytbody.getHeight();
                lyttwo.getLayoutParams().height = 0;
                lytone.getLayoutParams().height = 0;
                lytfour.getLayoutParams().height = 0;
                lytthree.getLayoutParams().height = height;
                lytfive.getLayoutParams().height = 0;
                lytfive.requestLayout();
                imgusers.setImageResource(R.drawable.userss);
                lyttwo.requestLayout();
                lytone.requestLayout();
                lytthree.requestLayout();
                lytfour.requestLayout();
                imghome.setImageResource(R.drawable.homee);
                imgchat.setImageResource(R.drawable.chatt2);
                imgmarket.setImageResource(R.drawable.baskett);
                imgsearch.setImageResource(R.drawable.searchh);
                try {
                    GetChats();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                bookcontentlayout=0;
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                int height = lytbody.getHeight();
                lyttwo.getLayoutParams().height = 0;
                lytone.getLayoutParams().height = 0;
                lytfour.getLayoutParams().height = height;
                lytthree.getLayoutParams().height = 0;
                lytfive.getLayoutParams().height = 0;
                lytfive.requestLayout();
                imgusers.setImageResource(R.drawable.userss);
                lyttwo.requestLayout();
                lytone.requestLayout();
                lytthree.requestLayout();
                lytfour.requestLayout();
                imghome.setImageResource(R.drawable.homee);
                imgchat.setImageResource(R.drawable.chatt);
                imgmarket.setImageResource(R.drawable.baskett);
                imgsearch.setImageResource(R.drawable.searchh2);
                GetWeeks(height);
                bookcontentlayout=0;
            }
        });
        imgusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                if(controller.getLevel()>=4) {
                    int height = lytbody.getHeight();
                    lyttwo.getLayoutParams().height = 0;
                    lytone.getLayoutParams().height = 0;
                    lytfour.getLayoutParams().height = 0;
                    lytthree.getLayoutParams().height = 0;
                    lytfive.getLayoutParams().height = height;
                    lytfive.requestLayout();
                    imgusers.setImageResource(R.drawable.userss1);
                    lyttwo.requestLayout();
                    lytone.requestLayout();
                    lytthree.requestLayout();
                    lytfour.requestLayout();
                    imghome.setImageResource(R.drawable.homee);
                    imgchat.setImageResource(R.drawable.chatt);
                    imgmarket.setImageResource(R.drawable.baskett);
                    imgsearch.setImageResource(R.drawable.searchh);
                    try {
                        GetUsers();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    bookcontentlayout = 0;
                }
                else {
                    NotAllowedDialogClass cdd = new NotAllowedDialogClass(SecondActivity.this);
                    cdd.show();
                }
            }
        });
        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                try {
                    GetChats();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                try {
                    SendChat(txtsend.getText().toString());
                    hideSoftKeyboard(SecondActivity.this);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        imgavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!longs) {
                    mpbutton.seekTo(0);mpbutton.start();
                    try {
                        Intent intent = new Intent(SecondActivity.this, UserActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                longs=false;
            }
        });

        imgavatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    longs = true;
                    mpbutton.seekTo(0);mpbutton.start();
                    getImage();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        imgacceptavatar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                 switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgacceptavatar.setBackgroundResource(R.drawable.accept2);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        imgacceptavatar.setBackgroundResource(R.drawable.accept);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        imgacceptavatar.setBackgroundResource(R.drawable.accept);
                        try {
                            acceptAvatar();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return false;
                }
                return false;
            }
        });
        imgacceptuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                try {
                    GetUsers();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        imgnextavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                try {
                    getAvatar(avatarindex + 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        imgprevavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                try {
                    if (avatarindex > 1) getAvatar(avatarindex - 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        imgsynced.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgsynced.setBackgroundResource(R.drawable.synced2);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        imgsynced.setBackgroundResource(R.drawable.synced);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        imgsynced.setBackgroundResource(R.drawable.synced);
                        try {
                            Save();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return false;
                }
                return false;
            }
        });
        imginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                InfoDialogClass cdd = new InfoDialogClass(SecondActivity.this);
                cdd.show();
                controller.setInfoShown();
                imginfo.clearAnimation();
            }
        });
        imgsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                SettingsDialogClass cdd = new SettingsDialogClass(SecondActivity.this);
                cdd.show();
            }
        });
        if(!controller.getInfoShown())
        {
            Animation connectingAnimation = AnimationUtils.loadAnimation(SecondActivity.this, R.anim.acceptbuttonavatar);
            imginfo.startAnimation(connectingAnimation);
        }
    }

    String order = "";
    int quantity = 0;
    int price = 0;
    boolean sendbuy = true;
    Boolean touched = false;

    void initBuyControls() {
        Intent in = getIntent();
        Uri data = in.getData();
        if (data != null) {

            String rdata = data.toString().replace("varchar://", "");
            if (rdata.equals("1")) {
                Toast.makeText(getBaseContext(), "موفقیت", Toast.LENGTH_LONG).show();
                try {
                    GetBuys();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getBaseContext(), "عدم موفقیت", Toast.LENGTH_LONG).show();
            }
        }
        buyh1 = (LinearLayout) findViewById(R.id.buyh1);
        buyh2 = (LinearLayout) findViewById(R.id.buyh2);
        buyh3 = (LinearLayout) findViewById(R.id.buyh3);
        buyp1 = (LinearLayout) findViewById(R.id.buyp1);
        buyp2 = (LinearLayout) findViewById(R.id.buyp2);
        buyp3 = (LinearLayout) findViewById(R.id.buyp3);
        buyh1.setTag("1");
        buyh2.setTag("2");
        buyh3.setTag("3");
        buyp1.setTag("4");
        buyp2.setTag("5");
        buyp3.setTag("6");
        View.OnTouchListener buys = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                String tag = view.getTag().toString();
                LinearLayout btn = (LinearLayout) view;
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touched = true;
                        btn.setBackgroundResource(R.drawable.curve_buyheartitemb);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        touched = false;
                        btn.setBackgroundResource(R.drawable.curve_buyheartitem);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        btn.setBackgroundResource(R.drawable.curve_buyheartitem);
                        if (touched) {
                            switch (tag) {
                                case "1":
                                    order = "heart";
                                    quantity = 5;
                                    price = 300;
                                    break;
                                case "2":
                                    order = "heart";
                                    quantity = 10;
                                    price = 500;
                                    break;
                                case "3":
                                    order = "heart";
                                    quantity = 20;
                                    price = 1200;
                                    break;
                                case "4":
                                    order = "potion";
                                    quantity = 5;
                                    price = 1000;
                                    break;
                                case "5":
                                    order = "potion";
                                    quantity = 10;
                                    price = 1500;
                                    break;
                                case "6":
                                    order = "potion";
                                    quantity = 20;
                                    price = 2750;
                                    break;
                            }
                            try {
                                if(controller.getBuyEnabled()) {
                                    String postData = "command=" + "buypotionheart," + controller.getUser() + "~" + order + "~" + price + "~" + quantity + "";
                                    Intent viewIntent =
                                            new Intent("android.intent.action.VIEW",
                                                    Uri.parse(url + "?" + postData));
                                    startActivity(viewIntent);
                                }else {
                                    Toast.makeText(SecondActivity.this,"این قابلیت به زودی افزوده می شود",Toast.LENGTH_SHORT).show();
                                }
                                //WebView webView = (WebView)findViewById(R.id.webv);
                                //webView.postUrl(url,postData.getBytes());
                                //BuyOnline(controller.getUser());
                            } catch (Exception e) {
                                Toast.makeText(SecondActivity.this, "Error Catch", Toast.LENGTH_SHORT).show();
                            }
                            touched = false;
                        }
                        return true;
                }
                return false;
            }
        };
        buyh1.setOnTouchListener(buys);
        buyh2.setOnTouchListener(buys);
        buyh3.setOnTouchListener(buys);
        buyp1.setOnTouchListener(buys);
        buyp2.setOnTouchListener(buys);
        buyp3.setOnTouchListener(buys);
    }

    public void BuyOnline(final String user) throws UnsupportedEncodingException {
        String uri = url + "?" + "command=buypotionheart," + user + "~" + order + "~" + price + "~" + quantity + "";
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Toast.makeText(SecondActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                        //String resp = response.substring(response.indexOf("{")+1,response.indexOf("}"));

                        // Log.d("Response", resp);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(SecondActivity.this, "Could not connect to server" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("command", "buypotionheart,"+user+"~"+order+"~"+price+"~"+quantity+"");
                //params.put("domain", "http://itsalif.info");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> head = super.getHeaders();
                return head;
            }
        };
        queue.add(postRequest);

    }

    void setLevelUI() {
        txtstar1.setText("" + controller.getLevel());
    }

    void setProgressUI() {
        txtstar2.setText(controller.getProgressString());
        if (progrefwidth > 0) {
            prgstar.getLayoutParams().width = controller.getProgressWidthByRef(progrefwidth);
        } else prgstar.getLayoutParams().width = 0;
        prgstar.requestLayout();
    }

    void setHeartUI() {
        txtheart1.setText("" + (controller.gethearts() + controller.getheartsBought()));
        txtheart2.setText("");
    }

    void setExirUI() {
        int dec = controller.getDecreasingExir();
        if (dec > 0) {
            Toast.makeText(SecondActivity.this, "You have lost " + controller.decreaseExir(dec) + " drops of potion in time", Toast.LENGTH_LONG).show();
        }
        exirchangedtime = controller.getExirChangedTime();
        txtexir1.setText("" + controller.getExir());
        if (controller.getExir() == 0) {
            txtexir1.setText("");
            txtexir2.setText("empty");
        } else txtexir2.setText("");
    }

    void setExirPercent(int percent) {
        if (progrefwidth > 0) {
            prgexir.getLayoutParams().width = percent * progrefwidth / 100;
        } else prgexir.getLayoutParams().width = 0;
        prgexir.requestLayout();
    }

    void setHeartPercent(int percent) {
        if (progrefwidth > 0) {
            prgheart.getLayoutParams().width = percent * progrefwidth / 100;
        } else prgheart.getLayoutParams().width = 0;
        prgheart.requestLayout();
    }

    void setTimer() {
        mHandler = new Handler();
        seconds = 59;
        mHandler.postDelayed(mUpdate, 100);
    }

    void setAvatarUI() {
        try {
            imgavatar.setImageBitmap(controller.getAvatar());
            getImage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    void setMessagesUI() {
        if (controller.getMessagesCount() < controller.getMessagesShownCount())
            controller.setMessagesShownCount(0);
        if ((controller.getMessagesCount() - controller.getMessagesShownCount()) > 0) {
            txtnummessages.setText("" + (controller.getMessagesCount() - controller.getMessagesShownCount()));
            txtnummessages.setVisibility(View.VISIBLE);
        } else {
            txtnummessages.setVisibility(View.INVISIBLE);
        }
    }

    int seconds = 0;
    private Runnable mUpdate = new Runnable() {
        public void run() {
            seconds++;
            Date date1 = new Date();
            if (seconds % 3 == 0) {
                long mills = date1.getTime() - exirchangedtime.getTime();
                int mins = (int) (mills / (1000 * 60));
                int t = controller.getDecreasExirTime();
                if (mins >= t) {
                    controller.decreaseExir(controller.getDecreasingExir());
                    exirchangedtime = controller.getExirChangedTime();
                    mins = mins % t;
                    txtexir1.setText("" + controller.getExir());
                    if (controller.getExir() == 0) txtexir1.setText("");
                    if (controller.getExir() == 0) exirzero = true;
                    else exirzero = false;
                }
                if (exirzero) {
                    txtexir2.setText("empty");
                    setExirPercent(0);
                } else {
                    txtexir2.setText(mins + "'/" + t + "'");
                    setExirPercent(100 - mins * 100 / t);
                }
            }
            {
                long mills = date1.getTime() - heartaddedtime.getTime();
                int sec = (int) (mills / (1000));
                if (sec >= HeartIncreasTime) {
                    controller.addheart((int) (sec / HeartIncreasTime));
                    heartaddedtime = controller.getHeartAddedTime();
                    if (controller.gethearts() == HeartsMaximum) heartmax = true;
                    else heartmax = false;
                    setHeartUI();
                }
                sec = sec % HeartIncreasTime;
                int reverse = HeartIncreasTime - sec;
                int m = reverse / 60;
                int s = reverse % 60;
                String ms = (m < 10) ? "0" + m : "" + m;
                String ss = (s < 10) ? "0" + s : "" + s;
                if (heartmax) {
                    txtheart2.setText("Full");
                    setHeartPercent(0);
                } else {
                    txtheart2.setText(ms + ":" + ss);
                    if (seconds % 2 == 0) {
                        setHeartPercent((sec + 1) * 100 / HeartIncreasTime);
                    }
                }
            }
            if (!paused) {
                mHandler = new Handler();
                mHandler.postDelayed(mUpdate, 1000);
            }
        }
    };

    Boolean paused = false;

    @Override
    protected void onPause() {
        paused = true;
        pauseMusic();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        paused = false;
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        TextView txtlevel, txtprogress;
        heartaddedtime = controller.getHeartAddedTime();
        exirchangedtime = controller.getExirChangedTime();
        HeartsMaximum = controller.getHeartsMaximum();
        HeartIncreasTime = controller.getHeartIncreaseTime();
        progrefwidth = controller.getProgWidth();
        if (!inited) initControls();
        paused = false;
        seconds = 0;
        if (controller.gethearts() == HeartsMaximum) heartmax = true;
        else heartmax = false;
        if (controller.getExir() == 0) exirzero = true;
        else exirzero = false;
        setExirUI();
        setHeartUI();
        setLevelUI();
        setTimer();
        setProgressUI();
        setAvatarUI();

        if (controller.getLevelUpShown() < controller.getLevel()) {
            LevelUpDialogClass cdd = new LevelUpDialogClass(SecondActivity.this);
            cdd.show();
            try {
                Save();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if(controller.getHeartId()>=controller.getLastHeartidSaved()+3)
        {
            Animation connectingAnimation = AnimationUtils.loadAnimation(SecondActivity.this, R.anim.syncedbuttonavatar);
            imgsynced.startAnimation(connectingAnimation);
            imgsynced.setVisibility(View.VISIBLE);
        }
        resumeMusic();
        super.onStart();
    }

    String dt1, dt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        final RelativeLayout btnlearn = (RelativeLayout) findViewById(R.id.btnlearn);
        final RelativeLayout btnexam = (RelativeLayout) findViewById(R.id.btntest);
        final RelativeLayout btnmatch = (RelativeLayout) findViewById(R.id.btnmatch);
        controller = new Controller(SecondActivity.this, true);
        txtexir1 = (TextView) findViewById(R.id.txtexir1);
        if (!inited) initControls();
        btnusersetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnusersetting.setBackgroundResource(R.drawable.curve_buttoninsideuserb);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnusersetting.setBackgroundResource(R.drawable.curve_buttoninsideusera);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        btnusersetting.setBackgroundResource(R.drawable.curve_buttoninsideusera);
                        UserMenuDialogClass cdd = new UserMenuDialogClass(SecondActivity.this);
                        cdd.show();

                        return true;
                }
                return false;
            }
        });
        btnmessages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnmessages.setBackgroundResource(R.drawable.curve_buttoninsideuserb);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnmessages.setBackgroundResource(R.drawable.curve_buttoninsideusera);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        btnmessages.setBackgroundResource(R.drawable.curve_buttoninsideusera);
                        MessagesDialogClass cdd = new MessagesDialogClass(SecondActivity.this);
                        cdd.show();
                        return true;
                }
                return false;
            }
        });
        btnlearn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnlearn.setBackgroundResource(R.drawable.button2b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnlearn.setBackgroundResource(R.drawable.button2a);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
                        // Date date = new Date();
                        //  SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                        //dt1 = sdf.format(date);

                        mpbutton.seekTo(0);mpbutton.start();
                        btnlearn.setBackgroundResource(R.drawable.button2a);
                        if (controller.getExir() == 0 || (controller.gethearts() + controller.getheartsBought()) == 0) {
                            NotEnoughDialogClass cdd = new NotEnoughDialogClass(SecondActivity.this);
                            cdd.show();
                        } else {
                            RewardsDialogClass cdd = new RewardsDialogClass(SecondActivity.this, LearnActivity.class, true);
                            cdd.show();

                        }
                        return false;
                }
                return true;
            }
        });

        btnexam.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnexam.setBackgroundResource(R.drawable.button1b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnexam.setBackgroundResource(R.drawable.button1a);
                        return false;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        btnexam.setBackgroundResource(R.drawable.button1a);
                        RewardsDialogClass cdd = new RewardsDialogClass(SecondActivity.this, TestActivity.class, false);
                        cdd.show();
                        return false;
                }
                return true;
            }
        });
        btnmatch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btnmatch.setBackgroundResource(R.drawable.button1b);
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        btnmatch.setBackgroundResource(R.drawable.button1a);
                        return false;
                    case MotionEvent.ACTION_UP:
                        mpbutton.seekTo(0);mpbutton.start();
                        btnmatch.setBackgroundResource(R.drawable.button1a);
                        Intent intent = new Intent(SecondActivity.this, MatchingActivity.class);
                        startActivity(intent);
                        return false;
                }
                return true;
            }
        });
        setPlayer();
        testDictionary();
        setMessagesUI();
    }

    private void testDictionary() {
        Button btnsearch = (Button) findViewById(R.id.btnsearch);
        final EditText txtword = (EditText) findViewById(R.id.txtword);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpbutton.seekTo(0);mpbutton.start();
                String word = txtword.getText().toString();
                try {
                    controller.Dictionary(word);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void Dictionary_Online(final String word) throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("[\"") + 2, response.indexOf("\",\""));
                            Toast.makeText(SecondActivity.this, resp.toString(), Toast.LENGTH_SHORT).show();
                            String s = response;

                        } catch (Exception e) {
                            int a = 1;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "translate");
                params.put("phrase", word);
                return params;
            }
        };
        queue.add(postRequest);

    }

    void setPlayer() {
        mPlayer = MediaPlayer.create(SecondActivity.this, R.raw.corporate);
        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume((float) (controller.getVolumeMain()/200.0), (float) (controller.getVolumeMain()/200.0));
        }
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    int length = 0;

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopMusic();
        mpbutton.stop();
        mpbutton.release();
        super.onDestroy();
    }

    public void resumeMusic() {
        if (mPlayer.isPlaying() == false) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    void GetChats() throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().equals("get_chats: result=ok")) {
                                if (params[1].toLowerCase().contains("count")) {
                                    String _count = params[1].toLowerCase().replace(" count=", "");
                                    int count = Integer.parseInt(_count);
                                    if (count > 0) {
                                        if (params[2].toLowerCase().contains("items") && params[2].contains("]")) {
                                            String _param2 = params[2].toLowerCase().replace(" items: [", "").replace("]", "").replace("\"", "");
                                            String[] data = _param2.split("#");
                                            List<String> slist;
                                            slist = new ArrayList<String>();
                                            for (int i = 0; i < data.length; i++)
                                                slist.add(data[i]);
                                            ListAdapter customAdapter = new ListAdapter(SecondActivity.this, R.layout.chat_item, slist);
                                            lstchats.setAdapter(customAdapter);
                                            scrollMyListViewToBottom(customAdapter);
                                        }
                                    } else if (count == 0) {
                                        Toast.makeText(SecondActivity.this, "No chats", Toast.LENGTH_SHORT).show();
                                    }
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
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "getChats,");
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }
    void GetUsers() throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().equals("get_users: result=ok")) {
                                if (params[1].toLowerCase().contains("count")) {
                                    String _count = params[1].toLowerCase().replace(" count=", "");
                                    int count = Integer.parseInt(_count);
                                    if (count > 0) {
                                        if (params[2].toLowerCase().contains("items") && params[2].contains("]")) {
                                            String _param2 = params[2].toLowerCase().replace(" items: [", "").replace("]", "").replace("\"", "");
                                            String[] data = _param2.split("#");
                                            List<String> slist;
                                            slist = new ArrayList<String>();
                                            for (int i = 0; i < data.length; i++)
                                                slist.add(data[i]);
                                            final ListAdapterUsers customAdapter = new ListAdapterUsers(SecondActivity.this,slist);
                                            lstusers.setAdapter(customAdapter);
                                            lstusers.requestLayout();
                                        }
                                    } else if (count == 0) {
                                        Toast.makeText(SecondActivity.this, "No users", Toast.LENGTH_SHORT).show();
                                    }
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
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String min=txtminlevel.getText().toString();if(min.length()==0)min="0";
                String max=txtmaxlevel.getText().toString();if(max.length()==0)max="1000";
                String count=txtcountuser.getText().toString();if(count.length()==0)count="60";
                params.put("command", "getUsers,"+min+"~"+max+"~"+count);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }
    int bookcontentlayout=0;
    void GetWeeks(int height) {
        final RelativeLayout lytweeks = (RelativeLayout)findViewById(R.id.lytweeks);
        final RelativeLayout lytcontent= (RelativeLayout)findViewById(R.id.lytcontent);
        RelativeLayout lytcontentfa= (RelativeLayout)findViewById(R.id.lytcontentfa);
        final ListView lstweeks = (ListView)findViewById(R.id.lstweeks);
        List<Integer> slist;
        slist = new ArrayList<Integer>();
        int count=46;
        if(controller.wordItems!=null) if(controller.wordItems.length>0) count=controller.wordItems.length/20;
        for (int i = 0; i < count; i++)
            slist.add(i+1);
        ListAdapterWeeks customAdapter = new ListAdapterWeeks(SecondActivity.this,slist);
        lstweeks.setAdapter(customAdapter);
        lytweeks.getLayoutParams().width=lytbody.getMeasuredWidth();
        lytweeks.requestLayout();
        final TextView txtconthtml=findViewById(R.id.txtdayhtml);
        final TextView txtconthtmlfa=findViewById(R.id.txtdayhtmlfa);
        txtconthtml.setOnTouchListener(new OnSwipeTouchListener(SecondActivity.this) {
             public void onSwipeRight() {
                 txtconthtml.getLayoutParams().width=txtconthtml.getMeasuredWidth();
                 mpbutton.seekTo(0);mpbutton.start();
                 ResizeWidthAnimation anim = new ResizeWidthAnimation(lytweeks,lytbody.getMeasuredWidth());
                 anim.setDuration(500);
                 lytweeks.startAnimation(anim);
                 lytweeks.requestLayout();
                 lytweeks.requestLayout();
                 bookcontentlayout=0;
            }
            public void onSwipeLeft() {
                txtconthtml.getLayoutParams().width=txtconthtml.getMeasuredWidth();
                mpbutton.seekTo(0);mpbutton.start();
                ResizeWidthAnimation anim = new ResizeWidthAnimation(lytcontent,0);
                anim.setDuration(500);
                lytcontent.startAnimation(anim);
                lytcontent.requestLayout();
                lytcontent.requestLayout();
                bookcontentlayout=2;
            }
        });
        txtconthtmlfa.setOnTouchListener(new OnSwipeTouchListener(SecondActivity.this) {
            public void onSwipeRight() {
                txtconthtmlfa.getLayoutParams().width=txtconthtmlfa.getMeasuredWidth();
                mpbutton.seekTo(0);mpbutton.start();
                ResizeWidthAnimation anim = new ResizeWidthAnimation(lytcontent,lytbody.getMeasuredWidth());
                anim.setDuration(500);
                lytcontent.startAnimation(anim);
                lytcontent.requestLayout();
                lytcontent.requestLayout();
                bookcontentlayout=1;
            }
        });
    }

    private void scrollMyListViewToBottom(final ListAdapter myListAdapter) {
        lstchats.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lstchats.setSelection(myListAdapter.getCount() - 1);
            }
        });
    }

    public class ResizeWidthAnimation extends Animation {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        public ResizeWidthAnimation(View view, int width) {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newWidth = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);

            mView.getLayoutParams().width = newWidth;
            mView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener implements GestureDetector.OnGestureListener {

            private static final int SWIPE_THRESHOLD = 0;
            private static final int SWIPE_VELOCITY_THRESHOLD = 0;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)*2) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }
    }
    void SendChat(final String message) throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            txtsend.setText("");
                            GetChats();
                        } catch (Exception e) {
                            int a = 1;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "insertchat," + controller.getUser() + "~" + message);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    void GetBuys() throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().equals("get_buys: result=ok")) {
                                if (params[1].toLowerCase().contains("count")) {
                                    String _count = params[1].toLowerCase().replace(" count=", "");
                                    int count = Integer.parseInt(_count);
                                    if (count > 0) {
                                        if (params[2].toLowerCase().contains("items") && params[2].contains("]")) {
                                            String _param2 = params[2].toLowerCase().replace(" items: [", "").replace("]", "").replace("\"", "");
                                            String[] data = _param2.split("#");
                                            for (int i = 0; i < data.length; i++) {
                                                String[] buy = data[i].split("~");
                                                if (buy.length == 3) {
                                                    String tarakonesh = buy[0];
                                                    String ordername = buy[1];
                                                    String quantity = buy[2];
                                                    try {
                                                        if (ordername.equals("heart")) {
                                                            controller.addheartsBought(Integer.parseInt(quantity));
                                                            setHeartUI();
                                                            Toast.makeText(SecondActivity.this, quantity + " " + ordername + " added", Toast.LENGTH_SHORT).show();
                                                            ExecuteBuy(tarakonesh);
                                                        } else if (ordername.equals("potion")) {
                                                            controller.increaseExir(Integer.parseInt(quantity));
                                                            setExirUI();
                                                            Toast.makeText(SecondActivity.this, quantity + " " + ordername + " added", Toast.LENGTH_SHORT).show();
                                                            ExecuteBuy(tarakonesh);
                                                        }
                                                    } catch (Exception e) {

                                                    }
                                                }
                                            }

                                        }
                                    } else if (count == 0) {
                                        Toast.makeText(SecondActivity.this, "No chats", Toast.LENGTH_SHORT).show();
                                    }
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
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "getBuys," + controller.getUser());
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    void ExecuteBuy(final String tarakonesh) throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().contains("execute")) {
                                if (params[1].toLowerCase().contains("message")) {
                                    String message = params[1].toLowerCase().replace(" message=", "");
                                    Toast.makeText(SecondActivity.this, message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "execBuy," + tarakonesh);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    void getImage() throws UnsupportedEncodingException {
        if(controller.isAvatarSaved()) {imgavatar.setImageBitmap(controller.getAvatar());return;}
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri_getimage + controller.getUser(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.length() > 200) {
                                byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
                                double w = bitmap.getWidth() / 2.0;
                                double h = bitmap.getHeight() / 2.0;
                                double radius = width / 2.0;
                                Bitmap bit = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                                try {
                                    for (int i = 0; i < width; i++) {
                                        for (int j = 0; j < width; j++) {
                                            if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * radius)) {
                                                bit.setPixel(i, j, Color.argb(0, 0, 0, 0));
                                            } else if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * 0.81 * radius)) {
                                                bit.setPixel(i, j, Color.argb(255, 0xf7, 0xaa, 0x3e));
                                            } else if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * 0.73 * radius)) {
                                                bit.setPixel(i, j, Color.argb(255, 0xff, 0xff, 0xff));
                                            } else bit.setPixel(i, j, bitmap.getPixel(i, j));
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SecondActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                controller.setAvatar(getStringImage(bit));
                                imgavatar.setImageBitmap(bit);

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

    void getAvatar(final int number) throws UnsupportedEncodingException {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        prgavatar.setVisibility(View.VISIBLE);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri_getavatar + "avatar" + number,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response.length() > 200) {
                                byte[] decodedString = Base64.decode(response, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                int width = Math.min(bitmap.getWidth(), bitmap.getHeight());
                                double w = bitmap.getWidth() / 2.0;
                                double h = bitmap.getHeight() / 2.0;
                                double radius = width / 2.0;
                                Bitmap bit = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                                try {
                                    for (int i = 0; i < width; i++) {
                                        for (int j = 0; j < width; j++) {
                                            if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * radius)) {
                                            } else if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * 0.81 * radius)) {
                                                bit.setPixel(i, j, Color.argb(50, 80, 40, 90));
                                            } else if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius * 0.73 * radius)) {
                                                bit.setPixel(i, j, Color.argb(255, 0xff, 0xff, 0xff));
                                            } else bit.setPixel(i, j, bitmap.getPixel(i, j));
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SecondActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                imgavatarbuying.setImageBitmap(bit);
                                txtavatarname.setText("avatar" + number);
                                avatarindex = number;
                                imgacceptavatar.setVisibility(View.VISIBLE);
                                Animation connectingAnimation = AnimationUtils.loadAnimation(SecondActivity.this, R.anim.acceptbuttonavatar);
                                imgacceptavatar.startAnimation(connectingAnimation);
                            }
                        } catch (Exception e) {
                            int a = 1;
                        }
                        prgavatar.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        prgavatar.setVisibility(View.INVISIBLE);
                        // error
                        //Toast.makeText(SecondActivity.this,"Error\n"+"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(postRequest);

    }

    void acceptAvatar() throws UnsupportedEncodingException {
        controller.AvatarChanged();
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // response
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().contains("avatar")) {
                                if (params[1].toLowerCase().contains("message")) {
                                    String message = params[1].toLowerCase().replace(" message=", "");
                                    Toast.makeText(SecondActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            imgacceptavatar.setVisibility(View.INVISIBLE);
                            imgacceptavatar.clearAnimation();
                            getImage();
                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(SecondActivity.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "acceptavatar," + controller.getUser() + "~avatar" + avatarindex);
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    public void setVolumes()
    {
        try{
            mpbutton.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
            mPlayer.setVolume((float)(controller.getVolumeMain()/200.0),(float)(controller.getVolumeMain()/200.0));
        }catch (Exception e)
        {

        }
    }

    public void Save() throws UnsupportedEncodingException {
        controller = new Controller(SecondActivity.this,true);
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        String resp = "";
                        try {
                            resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                        } catch (Exception e) {
                        }
                        try {
                            String[] params = resp.split(",");
                            if (params[0].toLowerCase().contains("save: result")) {
                                if (params.length > 1) {
                                    if (params[1].toLowerCase().contains("message")) {
                                        String msg = params[1].replace(" Message=", "");
                                        Toast.makeText(SecondActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        controller.setLastHeartIdSaved(controller.getHeartId());
                                        imgsynced.clearAnimation();
                                        imgsynced.setVisibility(View.INVISIBLE);
                                    }
                                }
                                else Toast.makeText(SecondActivity.this,resp,Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(SecondActivity.this,resp,Toast.LENGTH_SHORT).show();
                        }catch (Exception e){}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(SecondActivity.this,"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "save," + controller.getUser() + "~" + controller.getLevel() + "~" + controller.getProgress() + "~" + (controller.gethearts() + controller.getheartsBought()) + "~" + controller.getHeartId() + "~" + controller.getExir() + "~" + controller.getPercent10() + "~" + controller.getNumberOfPlayings()+"~"+controller.Version+"|"+controller.getDBVersion());
                params.put("data", controller.getSaveString());
                return params;
            }
        };
        queue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
        final TextView txtconthtml=findViewById(R.id.txtdayhtml);
        final TextView txtconthtmlfa=findViewById(R.id.txtdayhtmlfa);
        final RelativeLayout lytweeks = (RelativeLayout)findViewById(R.id.lytweeks);
        final RelativeLayout lytcontent= (RelativeLayout)findViewById(R.id.lytcontent);
        RelativeLayout lytcontentfa= (RelativeLayout)findViewById(R.id.lytcontentfa);
        if(bookcontentlayout==2)
        {
            try {
                txtconthtmlfa.getLayoutParams().width = txtconthtmlfa.getMeasuredWidth();
                mpbutton.seekTo(0);
                mpbutton.start();
                ResizeWidthAnimation anim = new ResizeWidthAnimation(lytcontent, lytbody.getMeasuredWidth());
                anim.setDuration(500);
                lytcontent.startAnimation(anim);
                lytcontent.requestLayout();
                lytcontent.requestLayout();
                bookcontentlayout = 1;
            }catch (Exception e){}
        }
        else if(bookcontentlayout==1)
        {
            try {
                txtconthtml.getLayoutParams().width=txtconthtml.getMeasuredWidth();
                mpbutton.seekTo(0);mpbutton.start();
                ResizeWidthAnimation anim = new ResizeWidthAnimation(lytweeks,lytbody.getMeasuredWidth());
                anim.setDuration(500);
                lytweeks.startAnimation(anim);
                lytweeks.requestLayout();
                lytweeks.requestLayout();
                bookcontentlayout=0;
            }catch (Exception e){}
        }
        else  super.onBackPressed();
    }
}
/*    void  GetAvatar(final int number)  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        ImageRequest imageRequest = new ImageRequest(urp+"avatar"+number,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                int width =Math.min(bitmap.getWidth(),bitmap.getHeight());
                                double w= bitmap.getWidth()/2.0;
                                double h = bitmap.getHeight()/2.0;
                                double radius = width/2.0;
                                Bitmap bit = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
                                try {
                                    for (int i = 0; i < width; i++) {
                                        for (int j = 0; j < width; j++) {
                                            if (Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius*radius)) {
                                                bit.setPixel(i, j, Color.argb(0, 0, 0, 0));
                                            }
                                            else if(Math.pow(i - w, 2) + Math.pow(j - h, 2) > (radius*0.81*radius))
                                            {
                                                bit.setPixel(i, j, Color.argb(50, 80, 40, 90));
                                            }
                                            else bit.setPixel(i,j,bitmap.getPixel(i,j));
                                        }
                                    }
                                }catch (Exception e){
                                    Toast.makeText(SecondActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                                imgavatar.setImageBitmap(bit);
                                if(number<1){
                                    try {
                                        GetAvatar(number+1);
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                imgavatar.setImageResource(R.drawable.usericon);
                            }
                        });
        queue.add(imageRequest);
    }
    */