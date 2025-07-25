package mortezamaghrebi.com.leitnerauto1100;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.ContactsContract;

import android.os.Bundle;
import android.text.BoringLayout;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MatchingActivity extends AppCompatActivity {
    Controller controller;
    int[] questionsIndex;
    int[] answersIndex;
    int[] useranswers;
    TextView[]words = new TextView[9];
    TextView[]meanings = new TextView[10];
    ImageView[]imgwords=new ImageView[9];
    ImageView[]imgmeanings=new ImageView[10];
    ImageView imglines,imgback,imgexirplus1;
    TextView txtresult;
    RelativeLayout lytcontent,btncheckout;
    int [] wordsid ={R.id.txtword1,R.id.txtword2,R.id.txtword3,R.id.txtword4,R.id.txtword5,R.id.txtword6,R.id.txtword7,R.id.txtword8,R.id.txtword9} ;
    int [] meaningsid = {R.id.txtmeaning1,R.id.txtmeaning2,R.id.txtmeaning3,R.id.txtmeaning4,R.id.txtmeaning5,R.id.txtmeaning6,R.id.txtmeaning7,R.id.txtmeaning8,R.id.txtmeaning9,R.id.txtmeaning10};
    int [] imgwordsid ={R.id.imgword1,R.id.imgword2,R.id.imgword3,R.id.imgword4,R.id.imgword5,R.id.imgword6,R.id.imgword7,R.id.imgword8,R.id.imgword9} ;
    int [] imgmeaningsid = {R.id.imgmeaning1,R.id.imgmeaning2,R.id.imgmeaning3,R.id.imgmeaning4,R.id.imgmeaning5,R.id.imgmeaning6,R.id.imgmeaning7,R.id.imgmeaning8,R.id.imgmeaning9,R.id.imgmeaning10};
    int NumberOfQuestions=10;
    MediaPlayer mp_select1;
    MediaPlayer mp_select2;
    MediaPlayer mp_result1;
    MediaPlayer mp_result2;
    MediaPlayer mp_finish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        getquestions();
        initcontrols();
        setPlayer();
        musiccreated=true;
        resumeMusic();
    }
    int selectedword=-1;
    int selectedmeaning=-1;
    void initcontrols()
    {
        mp_select1 = MediaPlayer.create(MatchingActivity.this, R.raw.select1);
        mp_select2 = MediaPlayer.create(MatchingActivity.this, R.raw.select2);
        mp_result1 = MediaPlayer.create(MatchingActivity.this, R.raw.win);
        mp_result2= MediaPlayer.create(MatchingActivity.this, R.raw.lose);
        mp_finish= MediaPlayer.create(MatchingActivity.this, R.raw.emtiaz);
        mp_select1.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
        mp_select2.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
        mp_result1.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
        mp_result2.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
        mp_finish.setVolume((float)(controller.getVolumeButtons()/100.0),(float)(controller.getVolumeButtons()/100.0));
        txtresult = (TextView) findViewById(R.id.txtresult);
        lytcontent = (RelativeLayout)findViewById(R.id.lytcontent);
        btncheckout = (RelativeLayout)findViewById(R.id.btncheckout);
        imglines=(ImageView)findViewById(R.id.imglines);
        imgback=(ImageView)findViewById(R.id.imgback);
        imgexirplus1=(ImageView)findViewById(R.id.imgexirplus1);
        imgexirplus1.setVisibility(View.INVISIBLE);
        txtresult.setVisibility(View.INVISIBLE);
        words = new TextView[9];
        meanings = new TextView[10];
        imgwords=new ImageView[9];
        imgmeanings=new ImageView[10];
        View.OnClickListener imgwordclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rewardgot) {
                    int index = Integer.parseInt(view.getTag().toString());
                    if (selectedmeaning >= 0) {
                        mp_select2.seekTo(0);
                        mp_select2.start();
                        matchitems(index, selectedmeaning);
                        imgwords[index].setImageResource(R.drawable.cirlcechoice4);
                        if(resultshown) checkItOut();
                    } else {
                        mp_select1.seekTo(0);
                        mp_select1.start();
                        if (selectedword >= 0) if (useranswers[selectedword] < 0)
                            imgwords[selectedword].setImageResource(R.drawable.cirlcechoice1);
                        selectedword = index;
                        imgwords[selectedword].setImageResource(R.drawable.cirlcechoice4);
                    }
                }
            }
        };
        View.OnClickListener imgmeaningclick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rewardgot) {
                    int index = Integer.parseInt(view.getTag().toString());
                    if (selectedword >= 0) {
                        mp_select2.seekTo(0);
                        mp_select2.start();
                        matchitems(selectedword, index);
                        imgmeanings[index].setImageResource(R.drawable.cirlcechoice4);
                        if(resultshown) checkItOut();
                    } else {
                        mp_select1.seekTo(0);
                        mp_select1.start();
                        Boolean clean = true;
                        for (int i = 0; i < useranswers.length; i++)
                            if (useranswers[i] == selectedmeaning) clean = false;
                        if (selectedmeaning >= 0) if (clean)
                            imgmeanings[selectedmeaning].setImageResource(R.drawable.cirlcechoice1);
                        selectedmeaning = index;
                        imgmeanings[selectedmeaning].setImageResource(R.drawable.cirlcechoice4);
                    }
                }
            }
        };
        btncheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showresult();
            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchingActivity.this.finish();
            }
        });
        for(int i=0;i<NumberOfQuestions;i++)
        {
            if(i<NumberOfQuestions-1) {
                imgwords[i] = (ImageView) findViewById(imgwordsid[i]);
                words[i] = (TextView) findViewById(wordsid[i]);
                words[i].setTag(""+i); imgwords[i].setTag(""+i);
                words[i].setText(controller.wordItems[questionsIndex[i]].word);
                imgwords[i].setOnClickListener(imgwordclick);
                words[i].setOnClickListener(imgwordclick);
            }
            meanings[i]=(TextView)findViewById(meaningsid[i]);
            imgmeanings[i]=(ImageView) findViewById(imgmeaningsid[i]);
            meanings[i].setTag(""+i); imgmeanings[i].setTag(""+i);
            String[] p= controller.wordItems[answersIndex[i]].persian.split("،");
            String pers=p[0];
            if(p.length>1)if(pers.length()+p[1].length()<23) pers+="، "+p[1];
            meanings[i].setText(pers);
            imgmeanings[i].setOnClickListener(imgmeaningclick);
            meanings[i].setOnClickListener(imgmeaningclick);
        }
    }

    void checkItOut()
    {
        for(int u=0;u<imgmeanings.length;u++) imgmeanings[u].setImageResource(R.drawable.cirlcechoice1);
        int corrects=0;
        for(int i=0;i<useranswers.length;i++)
        {
            if(useranswers[i]==-1) {
                imgwords[i].setImageResource(R.drawable.cirlcechoice1);
            }else if(questionsIndex[i]==answersIndex[useranswers[i]])
            {
                corrects++;
                imgwords[i].setImageResource(R.drawable.cirlcechoice2);
                imgmeanings[useranswers[i]].setImageResource(R.drawable.cirlcechoice2);
            }
            else{
                imgwords[i].setImageResource(R.drawable.cirlcechoice3);
                imgmeanings[useranswers[i]].setImageResource(R.drawable.cirlcechoice3);
            }
        }
        if(corrects==NumberOfQuestions-1) payreward();
    }
    Boolean checkCompleted()
    {
        int corrects=0;
        for(int i=0;i<useranswers.length;i++)
        {
            if(useranswers[i]==-1) {
            }else if(questionsIndex[i]==answersIndex[useranswers[i]])
            {
                corrects++;
            }
            else{
            }
        }
        if(corrects==NumberOfQuestions-1) return true;
        else return false;
    }

    Bitmap bitback;
    void matchitems(int w,int m) {
        for(int i=0;i<useranswers.length;i++) if(useranswers[i]==m)useranswers[i]=-1;
        useranswers[w]=m;
        Bitmap b;
        if(bitback==null) {
            bitback = Bitmap.createBitmap(lytcontent.getMeasuredWidth(), lytcontent.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
           // for (int i = 0; i < bitback.getWidth(); i++) {
           //     for (int j = 0; j < bitback.getHeight(); j++) {
            //            bitback.setPixel(i, j, Color.argb(0, 0, 0, 0));
            //            if(i==0||j==0||i==bitback.getWidth()-1||j==bitback.getHeight()-1)bitback.setPixel(i, j, Color.argb(255, 0, 0, 0));
            //    }
           // }
        }
        b=Bitmap.createBitmap(bitback);
        try {
            for(int u=0;u<imgmeanings.length;u++) imgmeanings[u].setImageResource(R.drawable.cirlcechoice1);
            for(int i=0;i<useranswers.length;i++) {
                if(useranswers[i]>=0) {
                    imgwords[i].setImageResource(R.drawable.cirlcechoice4);
                    imgmeanings[useranswers[i]].setImageResource(R.drawable.cirlcechoice4);
                    int mindex = useranswers[i];
                    int x1 = imgwords[i].getLeft() +imgwords[i].getMeasuredWidth()/2 , y1 = imgwords[i].getTop() +imgwords[i].getMeasuredHeight()/2;
                    int x2 = imgmeanings[mindex].getLeft()+imgmeanings[mindex].getMeasuredWidth()/2 , y2 = imgmeanings[mindex].getTop()+imgmeanings[mindex].getMeasuredHeight()/2;
                    double dx=0.5;
                    if(Math.abs(y2-y1)>10*Math.abs(x2-x1)) dx=0.1;
                    if(x2>x1) {
                        for (double x = x1; x<x2; x += dx) {
                            double y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                            try {
                                int xx = (int) x, yy = (int) y;
                                for (double ix = x - 2.5; ix < x + 2.5; ix++) {
                                    for (double iy = y - 2.5; iy < y + 2.5; iy++) {
                                        b.setPixel((int) ix, (int) iy, Color.rgb(0, 0, 0));
                                    }
                                }
                            } catch (Exception e) {
                                int a;
                            }
                        }
                    }else
                    {
                        for (double x = x1; x>x2; x -= dx) {
                            double y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                            try {
                                int xx = (int) x, yy = (int) y;
                                for (double ix = x - 2.5; ix < x + 2.5; ix++) {
                                    for (double iy = y - 2.5; iy < y + 2.5; iy++) {
                                        b.setPixel((int) ix, (int) iy, Color.rgb(0, 0, 0));
                                    }
                                }
                            } catch (Exception e) {
                                int a;
                            }
                        }
                    }
                }
                else {
                    imgwords[i].setImageResource(R.drawable.cirlcechoice1);
                }
            }
            imglines.setImageBitmap(b);
        } catch (Exception e) {
            Toast.makeText(MatchingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        selectedmeaning=selectedword=-1;

    }


    void getquestions()
    {
        controller = new Controller(MatchingActivity.this,true);
        if(controller.wordItems.length==0)
        {
            controller.addheart(1);
            AlertDialog alertDialog = new AlertDialog.Builder(MatchingActivity.this).create();
            alertDialog.setMessage("You didn't get words,if download didn't start automatically, please connect to internet and try again.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            questionsIndex = controller.GetQuestionsIndex(NumberOfQuestions);
            List<Integer> list,answrs;
            list = new ArrayList<Integer>();
            answrs = new ArrayList<Integer>();
            for(int i=0;i<NumberOfQuestions;i++)list.add(i);
            Random r= new Random();
            while(list.size()>0)
            {
                int k=r.nextInt(list.size());
                answrs.add(list.get(k));
                list.remove(k);
            }
            answersIndex = new int[NumberOfQuestions];
            for(int i=0;i<answersIndex.length;i++) answersIndex[i]=questionsIndex[answrs.get(i)];
        }
        useranswers = new int[NumberOfQuestions-1];
        for(int i=0;i<useranswers.length;i++) useranswers[i]=-1;
    }

    Boolean resultshown=false;
    Boolean rewardgot=false;
    void showresult()
    {
        if(resultshown){ checkItOut();}
        else {
            controller.inceaseHeartId();
            for (int u = 0; u < imgmeanings.length; u++)
                imgmeanings[u].setImageResource(R.drawable.cirlcechoice1);
            int corrects = 0;
            for (int i = 0; i < useranswers.length; i++) {
                String s = "";
                if (useranswers[i] == -1) {
                    imgwords[i].setImageResource(R.drawable.cirlcechoice1);
                    s = "e";
                } else if (questionsIndex[i] == answersIndex[useranswers[i]]) {
                    imgwords[i].setImageResource(R.drawable.cirlcechoice2);
                    imgmeanings[useranswers[i]].setImageResource(R.drawable.cirlcechoice2);
                    s = "m";
                    corrects++;
                } else {
                    controller.wordItems[questionsIndex[i]].review += "f";
                    imgwords[i].setImageResource(R.drawable.cirlcechoice3);
                    imgmeanings[useranswers[i]].setImageResource(R.drawable.cirlcechoice3);
                    s = "w";
                }
                controller.wordItems[questionsIndex[i]].review += s;
                controller.wordItems[questionsIndex[i]].lastheart = controller.getHeartId();
                wordItem wi = controller.wordItems[questionsIndex[i]];
                controller.myDB.UpdateWordReview(wi.id, wi.review, wi.lastheart);
            }
            controller.increaseProgress(corrects);
            controller.addPlayResult(corrects*100.0/(NumberOfQuestions-1));
            mPlayer.setVolume((float) (controller.getVolumeGame()/200.0), (float) (controller.getVolumeGame()/200.0));
            if(corrects==NumberOfQuestions-1){
                payreward();
            }
            else if (corrects >= (NumberOfQuestions * 6 / 10)) {
                mp_result1.seekTo(0);
                mp_result1.start();
            } else {
                mp_result2.seekTo(0);
                mp_result2.start();
            }
            resultshown = true;
            if(corrects<NumberOfQuestions-1) {
                txtresult.setText("" + corrects + "/" + (NumberOfQuestions - 1));
                txtresult.setVisibility(View.VISIBLE);
                Animation connectingAnimation = AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.exirplus);
                txtresult.startAnimation(connectingAnimation);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtresult.setVisibility(View.INVISIBLE);
                }
            },1500);
        }
    }

    void payreward(){
        if(!rewardgot){
            mp_finish.seekTo(0);
            mp_finish.start();
            controller.increaseExir(1);
            imgexirplus1.setVisibility(View.VISIBLE);
            Animation connectingAnimation = AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.exirplus);
            imgexirplus1.startAnimation(connectingAnimation);
            rewardgot=true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgexirplus1.setVisibility(View.INVISIBLE);
                }
            },1500);
        }
    }

    void setPlayer()
    {
        mPlayer = MediaPlayer.create(MatchingActivity.this,R.raw.royalti_matching);
        if(mPlayer!= null)
        {
            mPlayer.setLooping(true);
            mPlayer.setVolume((float)(controller.getVolumeGame()/100.0),(float)(controller.getVolumeGame()/100.0));
        }
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra){

                onError(mPlayer, what, extra);
                return true;
            }
        });
    }
    MediaPlayer mPlayer;int length=0;Boolean paused=false;
    public void pauseMusic()
    {
        if(mPlayer.isPlaying())
        {
            mPlayer.pause();
            length=mPlayer.getCurrentPosition();

        }
    }
    public void resumeMusic()
    {
        if(mPlayer.isPlaying()==false)
        {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }
    public void stopMusic()
    {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }
    @Override
    protected void onPause() {
        paused=true;
        pauseMusic();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        try
        {
            mp_select1.stop();
            mp_select2.stop();
            mp_result1.stop();
            mp_result2.stop();
            mp_finish.stop();
            mp_finish.release();
            mp_select1.release();
            mp_select2.release();
            mp_result1.release();
            mp_result2.release();
        }catch (Exception e){}
        stopMusic();
        super.onDestroy();
    }
    Boolean musiccreated=false;
    @Override
    protected void onStart() {
        if(musiccreated) resumeMusic();
        super.onStart();
    }
}
