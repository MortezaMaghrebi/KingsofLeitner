package mortezamaghrebi.com.leitnerauto1100;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Controller {
    final String MY_PREFS_NAME = "PREFS_1100";
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    DBAdapter myDB;
    int[] targets = {4,8,10,12,14,16,18,20,22,24,25,26,27,28,29,30,31,32,33,34,35}; //if number of correct answers arrive targets*20 level increases; each level releases 20 words;
    int[] heartOfBox={1,1,4,8,14,24,38,56,80,110,146,189,240,299,360};
    String encode = "پ,A~ر,B~ ,C~خ,D~و,E~،,F~س,G~ی,H~ن,I~ا,J~ذ,K~ب,L~ه,M~ک,N~د,O~ف,P~م,Q~ش,R~ج,S~ت,T~غ,U~ق,V~ز,W~آ,X~ل,Y~ژ,Z~گ,s~ض,b~ح,t~ط,d~چ,r~ث,f~ع,g~ص,h~ظ,i~ئ,j~-,k~p,l~e,m~n,n~a,o~c,p~ء,q";
    public wordItem[] wordItems;
    final String url = "http://kingsofleitner.ir/words1100/webservice.php";
    public final String infourl = "http://kingsofleitner.ir/words1100/info.php";
    final int DecreasExirEachMin=60; //use functions please: getDecreaseExirTime()
    final int HeartsMaximum=12;
    final int HeartIncreasTime=540; //seconds
    final int InitialExir=60;
    final int NumberOfQuestions=14,RewardBad=4,RewardSoSo=6,RewardExcelent=12;
    ProgressDialog progressDialog;
    ProgressDialog progressDialog2;
    public int Version=3;

    public Controller(Context context,Boolean getwords) {
        this.context = context;
        editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        openDB(context);
        if(getwords) {
            Cursor cursor = myDB.getAllWords();
            getWordItems(cursor);
            cursor.close();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Getting words online...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog2 = new ProgressDialog(context);
        progressDialog2.setMessage("Getting Saved Progress...");
        progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }
    private static final String TAG = "DatabaseBackup";
    public boolean backupDatabaseToDocuments(Context context) {
        try {
            File dbFile = context.getDatabasePath("DB1100");
            if (!dbFile.exists()) {
                Log.e(TAG, "Database file not found!");
                return false;
            }

            File docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!docsDir.exists()) docsDir.mkdirs();

            File backupFile = new File(docsDir, "DB1100_backup.db");

            copyFile(dbFile, backupFile);
            Toast.makeText(context,"Backup saved to Documents folder.",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Database backup saved in Documents: " + backupFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error backing up database", e);
            return false;
        }
    }

    private  void copyFile(File src, File dst) throws Exception {
        try (FileChannel inChannel = new FileInputStream(src).getChannel();
             FileChannel outChannel = new FileOutputStream(dst).getChannel()) {
            outChannel.transferFrom(inChannel, 0, inChannel.size());
        }
    }
    public int getExactPercent()
    {
        int wrong=0;
        int correct=0;
        for(int k=0;k<wordItems.length;k++)
        {
            String r = wordItems[k].review;
            for(int i=0;i<r.length();i++)
            {
                String c= String.valueOf(r.charAt(i));
                if(c.equals("w")){wrong++;}
                else if(c.equals("f")) {wrong++;}
                else if(c.equals("e")) {wrong++;}
                else correct++;
            }
        }
        return (correct*100/(correct+wrong));
    }
    public void getWordItems(Cursor cursor) {
        wordItems = new wordItem[cursor.getCount()];
        int k = 0;
        if (cursor.moveToFirst()) {
            do {
                wordItems[k] = new wordItem();
                wordItems[k].id = cursor.getInt(DBAdapter.COL_id);
                wordItems[k].word = cursor.getString(DBAdapter.COL_word);
                wordItems[k].example = cursor.getString(DBAdapter.COL_example);
                wordItems[k].day = cursor.getInt(DBAdapter.COL_day);
                wordItems[k].persian= decode(cursor.getString(DBAdapter.COL_persian));
                wordItems[k].examplefa= decode(cursor.getString(DBAdapter.COL_examplefa));
                wordItems[k].definition = cursor.getString(DBAdapter.COL_definition);
                wordItems[k].review= cursor.getString(DBAdapter.COL_review);
                wordItems[k].started = cursor.getInt(DBAdapter.COL_started);
                wordItems[k].finished = cursor.getInt(DBAdapter.COL_finished);
                wordItems[k].lastheart = cursor.getInt(DBAdapter.COL_lastheart);
                wordItems[k].pronounce = cursor.getString(DBAdapter.COL_pronounce);
                k++;
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public String getSaveString()
    {
        String savestr="[User:"+ getUser()+",Count:"+wordItems.length+"]WORDS(word,review,lastheart,started,finished){";
        for(int i=0;i<wordItems.length;i++)
        {
            String sep="";
            if(i>0) sep="$$";
            savestr+=sep+wordItems[i].word+"**"+wordItems[i].review+"**"+wordItems[i].lastheart+"**"+wordItems[i].started+"**"+wordItems[i].finished;
        }
        savestr+="}";
        return savestr;
    }

    void openDB(Context context)
    {
            myDB = DBAdapter.getInstance(context);
            myDB.open();
    }
    void closeDB()
    {
        try {
            myDB.close();
        }catch (Exception e){}
    }
    int getDBVersion()
    {
        return prefs.getInt("dbversion",0);
    }
    void setDBVersion(int version)
    {
        editor.putInt("dbversion",version);
        editor.commit();
    }
    int getSwipeShown()
    {
        return prefs.getInt("swipeshown",0);
    }
    void setSwipeShown()
    {
        editor.putInt("swipeshown",Math.min(prefs.getInt("swipeshown",0)+1,100));
        editor.commit();
    }
    /////
    int getTimeLearn()
    {
        return prefs.getInt("timelearn",30);
    }
    void setTimeLearn(int time)
    {
        editor.putInt("timelearn",time);
        editor.commit();
    }
    int getTimeTest()
    {
        return prefs.getInt("timetest",45);
    }
    void setTimeTest(int time)
    {
        editor.putInt("timetest",time);
        editor.commit();
    }
    /////
    /////
    int getVolumeGame()
    {
        return prefs.getInt("voulmegame",25);
    }
    void setVolumeGame(int volume)
    {
        editor.putInt("voulmegame",volume);
        editor.commit();
    }
    /////
    int getVolumeButtons()
    {
        return prefs.getInt("voulmebuttons",100);
    }
    void setVolumeButtons(int volume)
    {
        editor.putInt("voulmebuttons",volume);
        editor.commit();
    }/////
    int getVolumeMain()
    {
        return prefs.getInt("voulmemain",30);
    }
    void setVolumeMain(int volume)
    {
        editor.putInt("voulmemain",volume);
        editor.commit();
    }
    /////
    int getTapShown()
    {
        return prefs.getInt("tapshown",0);
    }
    void setTapShown()
    {
        editor.putInt("tapshown",Math.min(prefs.getInt("tapshown",0)+1,100));
        editor.commit();
    }
    /////
    Boolean getInfoShown()
    {
        return prefs.getBoolean("infoshown",false);
    }
    void setInfoShown()
    {
        editor.putBoolean("infoshown",true);
        editor.commit();
    }
    /////
    Boolean getIsLogin()
    {
        return prefs.getBoolean("islogin",false);
    }
    void setIsLogin(Boolean isLogin)
    {
        editor.putBoolean("islogin",isLogin);
        editor.commit();
    }
    ///
    Boolean getIsPronounce()
    {
        return prefs.getBoolean("ispronounce",true);
    }
    void setIsPronounce(Boolean isPronounce)
    {
        editor.putBoolean("ispronounce",isPronounce);
        editor.commit();
    }
    ///
    Boolean getIsUserWordsDownloaded()
    {
        return prefs.getBoolean("userwordsgotten",false);
    }
    void setIsUserWordsDownloaded(Boolean downloaded)
    {
        editor.putBoolean("userwordsgotten",downloaded);
        editor.commit();
    }
    ///
    int getProgWidth()
    {
        Random rnd = new Random();
        return prefs.getInt("progwidth",0);
    }
    void setProgWidth(int width)
    {
        editor.putInt("progwidth",width);
        editor.commit();
    }
    public int getLastHeartidSaved()
    {
        return prefs.getInt("lastsavedid",0);
    }
    public void setLastHeartIdSaved(int heartId)
    {
        editor.putInt("lastsavedid",heartId);
        editor.commit();
    }
    /////
    ///
    String getMessages()
    {
        return prefs.getString("messages","");
    }
    int getMessagesCount()
    {
        return prefs.getInt("messagescount",0);
    }
    int getMessagesShownCount()
    {
        return prefs.getInt("messagesshowncount",0);
    }
    void setMessagesShownCount(int num)
    {
        editor.putInt("messagesshowncount",num);
        editor.commit();
    }
    void setMessages(String messages)
    {
        editor.putString("messages",messages);
        editor.putInt("messagescount",(messages.length()>0?messages.split("#").length:0));
        editor.commit();
    }
    /////

    ///
    Bitmap getAvatar()
    {
        String imstr= prefs.getString("avatar","");
        if(imstr.length()==0) return BitmapFactory.decodeResource(context.getResources(),
            R.drawable.usericon);
        byte[] decodedString = Base64.decode(imstr, Base64.DEFAULT);
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
                        bit.setPixel(i, j, Color.argb(255, 0xf7, 0xaa, 0x3e));
                    } else bit.setPixel(i, j, bitmap.getPixel(i, j));
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }return bit;
    }
    void setAvatar(String avatar)
    {
         avatar = "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";
        editor.putString("avatar",avatar);
        editor.putBoolean("avatarsaved",true);
        editor.commit();
    }
    public Boolean isAvatarSaved()
    {
        return prefs.getBoolean("avatarsaved",false);
    }
    public void AvatarChanged()
    {
        editor.putBoolean("avatarsaved",false);
        editor.commit();
    }

    private Bitmap StringToWordImage(String imagestr)
    {
        try {
            byte[] decodedString = Base64.decode(imagestr, Base64.DEFAULT);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);//.copy(Bitmap.Config.ARGB_8888,true);
            Bitmap bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), Bitmap.Config.ARGB_8888);
            for(int i=0;i<bitmap.getWidth();i++) for (int j=0;j<bitmap.getHeight();j++)bitmap.setPixel(i,j,bitmap1.getPixel(i,j));
            int radius = bitmap.getWidth()/5;
            int width=bitmap.getWidth();int height=bitmap.getHeight();
            int hmr=height-radius;int wmr=width-radius;
            for(int i=0;i<=radius;i++) {
                for (int j = 0; j <= radius; j++)
                    if (((i - radius) * (i - radius) + (j - radius) * (j - radius)) > radius * radius){
                        if ((i <= radius) && (j <= radius)){
                            bitmap.setPixel(i, j, Color.argb(0, 0, 0, 0));
                        }
                    }
            }
            for(int i=wmr;i<bitmap.getWidth();i++) {
                for (int j = 0; j <= radius; j++)
                    if (Math.pow(i-wmr,2) + Math.pow(radius-j,2)  > radius * radius)
                        if (i>= wmr && j <= radius)
                            bitmap.setPixel(i, j, Color.argb(0, 0, 0, 0));
            }
            for(int i=0;i<=radius;i++) {
                for (int j = hmr; j <bitmap.getHeight(); j++)
                    if (((i - radius) * (i - radius) + (j-hmr) * (j-hmr)) > radius * radius)
                        if (i <= radius && j>=hmr)
                            bitmap.setPixel(i, j, Color.argb(0, 0, 0, 0));
            }
            for(int i=wmr;i<width;i++) {
                for (int j = hmr; j <height; j++)
                    if (Math.pow(wmr-i,2) + Math.pow(hmr-j,2) > radius * radius)
                        if (i>=wmr && j>=hmr)
                            bitmap.setPixel(i, j, Color.argb(0, 0, 0, 0));
            }
            return bitmap;
        }catch (Exception e){return null;}
    }
    /////
    Bitmap getWordImage(String word)
    {
        Cursor cursor=myDB.getWordImage(word);
        String imstr="";
        if(cursor.getCount()>0){ if (cursor.moveToFirst()) imstr = cursor.getString(DBAdapter.COL_image);}
        else  return null;
        return StringToWordImage(imstr);
    }
    Bitmap setWordImage(String word,String image)
    {
       myDB.insertWordImage(word,image);
       return StringToWordImage(image);
    }
    //
    private int getLevelTarget(int level) //if number of correct answers arrive targets level increases; each level releases 20 words;
    {
        int target =80;
        if(level>targets.length) return (35*20);
        else return targets[level-1]*20;
    }
    /////
    public Boolean getBuyEnabled()
    {
        return prefs.getBoolean("potionsactivated",false);
    }
    public void setBuyEnabled(Boolean enabled)
    {
        editor.putBoolean("potionsactivated",enabled);
        editor.commit();
    }
    /////
    /////
    public int getHeartId()
    {
        return prefs.getInt("heartid",1);
    }
    void inceaseHeartId()
    {
        editor.putInt("heartid",(getHeartId()+1));
        editor.commit();
    }
    /////
    public int getNumberOfPlayings()
    {
        return prefs.getInt("numofplays",0);
    }
    public void setNumberOfPlayings(int number)
    {
        editor.putInt("numofplays",number);
        editor.commit();
    }
    public void addPlayResult(double percent)
    {
        int n=getNumberOfPlayings();
        double perc=(double)prefs.getFloat("percent",(float)0);
        double mpercent = (perc*n+percent*10)/(n+1);
        editor.putInt("numofplays",(n+1));
        editor.putFloat("percent",(float)mpercent);
        editor.commit();
    }
    public double getPercent()
    {
        return (prefs.getFloat("percent",(float)0)/10.0);
    }
    public double getPercent10()
    {
        return (prefs.getFloat("percent",(float)0));
    }
    public void setPercent(double percent)
    {
        editor.putFloat("percent",(float)(percent*10));
        editor.commit();
    }
    ///
    public void initialHeartAndExir(int hearts,int heartid,int potions,int level, int progress)
    {
        editor.putInt("heartid",heartid);
        editor.putInt("level",level);
        editor.putInt("hearts",0);
        editor.putInt("heartsbought",0);
        editor.putInt("exir",potions);
        editor.putInt("progress",progress);
        editor.commit();
        addheartsBought(hearts);
        setLevelUpShown(level);
    }
    public void initialHeartAndExirSettings(Boolean online,int heartincreastime,int decreaspotiontime,int heartsmax)
    {
        editor.putInt("heartinctime",heartincreastime);
        editor.putInt("heartsmax",heartsmax);
        editor.putInt("decexirtime",decreaspotiontime);
        editor.putBoolean("settingsonline",online);
        editor.commit();
    }
    /////
    int getHeartsMaximum()
    {
        return prefs.getInt("heartsmax",HeartsMaximum);
    }
    int getHeartIncreaseTime()
    {
        return prefs.getInt("heartinctime",HeartIncreasTime);
    }
    int gethearts()
    {
        return prefs.getInt("hearts",getHeartsMaximum());
    }
    int getheartsBought()
    {
        return prefs.getInt("heartsbought",0);
    }
    void addheart(int number)
    {
        String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(date);
        int h=(gethearts()+number);
        if (h>getHeartsMaximum())h=getHeartsMaximum();
        editor.putInt("hearts",h);
        editor.putString("addhearttime",stringDate);
        editor.commit();
    }
    void addheartsBought(int number)
    {
        int h=(gethearts()+number);
        int h1=0;
        if (h>getHeartsMaximum()) {
            h1 = h - getHeartsMaximum();
            editor.putInt("hearts", getHeartsMaximum());
        }
        else editor.putInt("hearts", h);
        editor.putInt("heartsbought",getheartsBought()+h1);
        editor.commit();
    }

    void decreaseheart() {
        int h = gethearts();
        int h1 = getheartsBought();
        if (h > 0) {
            h--;
            editor.putInt("hearts", h);
            if (h == (getHeartsMaximum() - 1)) {
                String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                String stringDate = sdf.format(date);
                editor.putString("addhearttime", stringDate);
            }
        } else if (h1 > 0) {
            h1--;
            editor.putInt("heartsbought", h1);
        }
        editor.commit();
    }

    Date getHeartAddedTime()
    {
        String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
        String str= prefs.getString("addhearttime","");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        Date date;
        if(str.equals("")){
            date = new Date();
            String stringDate = sdf.format(date);
            editor.putString("addhearttime",stringDate);
            editor.commit();
            return date;
        }
        try {
            date = sdf.parse(str);
            return date;
        } catch(Exception e){
            date = new Date();
            String stringDate = sdf.format(date);
            editor.putString("addhearttime",stringDate);
            editor.commit();
            return date;
        }
    }
    /////
    int getDecreasExirTime() //equals 80
    {
        return prefs.getInt("decexirtime",DecreasExirEachMin);
    }
    int getExir()
    {
        return  prefs.getInt("exir",InitialExir);
    }
    int getDecreasingExir()
    {
        String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
        String str= prefs.getString("exirtime","");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        Date date = new Date();
        if(str.equals("")) date =new Date();
        try {
            date = sdf.parse(str);
        } catch(Exception e){
            date = new Date(); //Exception handling
        }
        long mills = (new Date()).getTime() - date.getTime();
        int mins = (int) (mills/(1000 * 60));
        return (int)(mins/getDecreasExirTime());
    }
    Date getExirChangedTime()
    {
        String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
        String str= prefs.getString("exirtime","");
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        Date date;
        if(str.equals("")){
            date = new Date();
            String stringDate = sdf.format(date);
            editor.putString("exirtime",stringDate);
            editor.commit();
            return date;
        }
        try {
            date = sdf.parse(str);
            return date;
        } catch(Exception e){
            date = new Date();
            String stringDate = sdf.format(date);
            editor.putString("exirtime",stringDate);
            editor.commit();
            return date;
        }

    }
    void increaseExir(int number)
    {
        String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(date);
        editor.putString("exirtime",stringDate);
        editor.putInt("exir",(getExir()+number));
        editor.commit();
    }
    int decreaseExir(int number)
    {
        int getex=getExir();
        if(getex>12 && number>(getex/2))number=(getex/2);
        int e = (getex-number);
        if(e<0)e=0;
        String DATE_FORMAT_NOW = "yyyy-MM-dd kk:mm:ss";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        String stringDate = sdf.format(date);
        editor.putString("exirtime",stringDate);
        editor.putInt("exir",e);
        editor.commit();
        return (getex-e);
    }

    /////
    int getLevel()
    {
        return prefs.getInt("level",1);
    }
    void increaseLevel(int number)
    {
        editor.putInt("level",(getLevel()+number));
        editor.commit();
        increaseExir(1);
        addheartsBought(1);
    }
    int getLevelUpShown()
    {
        return prefs.getInt("levelup",1);
    }
    void setLevelUpShown(int level)
    {
        editor.putInt("levelup",level);
        editor.commit();
    }
    /////
    private int getMaxWordIdReleased()
    {
        int level=getLevel();
        int max=level*20;
        if (max>wordItems.length) max=wordItems.length;
        return max;
    }
    public int getPreviousWordIdReleased()
    {
        int level=getLevel();
        if(level>1)level--;
        int max=level*20;
        if (max>wordItems.length-1) max=wordItems.length-1;
        return max;
    }
    /////
    public int getProgress()
    {
        return prefs.getInt("progress",0);
    }
    Boolean increaseProgress(int number) //correct answers;
    {
        int progress=getProgress();
        int target = getLevelTarget(getLevel());
        if(progress+number>=target)
        {
            increaseLevel(1);
            editor.putInt("progress",(progress+number-target));
            editor.commit();
            return true; //****Level Increased!!!!!****
        }
        else {
            editor.putInt("progress", (getProgress() + number));
            editor.commit();
            return false; //level did not change
        }
    }
    String getProgressString() //correct answers;
    {
        int progress=getProgress();
        int target = getLevelTarget(getLevel());
        return ""+progress+"/"+target;
    }
    int getProgressWidthByRef(int refWidth)
    {
        int progress=getProgress();
        int target = getLevelTarget(getLevel());
        return progress*refWidth/target;
    }

    /////
    String getUser()
    {
        Random rnd = new Random();
        return prefs.getString("user","guest"+ String.valueOf(rnd.nextInt(1000)*rnd.nextInt(1000)));
    }
    void setUser(String user)
    {
        editor.putString("user",user);
        editor.commit();
    }
    String getPassword()
    {
        return prefs.getString("password","");
    }
    void setPassword(String password)
    {
        editor.putString("password",password);
        editor.commit();
    }
    /////
    int[] GetQuestionsIndex (int numOfQuestions)
    {
        int[] questions = new int[numOfQuestions];
        List<Integer> list;
        list = new ArrayList<Integer>();
        int k=0;
        int max=getMaxWordIdReleased();
        int heartid=getHeartId();
        for(int i=0;i<max;i++)
        {
            int box = wordItems[i].box();
            int dheart = heartid-wordItems[i].lastheart;
            if(box>=15) continue;
            else {
                if (dheart >= heartOfBox[box]) {
                    if(!list.contains(i)) list.add(i);
                    k++;
                }
                if (k == numOfQuestions-1) break;
            }
        }
        if(list.size()<numOfQuestions)
        {
            Random rnd=new Random();
            while (list.size()<numOfQuestions){
                int []r= new int[6];
                r[0]=rnd.nextInt(wordItems.length);
                r[1]=rnd.nextInt(Math.min(max*2,wordItems.length));
                r[1]=rnd.nextInt(Math.min(max*2,wordItems.length));
                r[1]=rnd.nextInt(Math.min(max*2,wordItems.length));
                r[2]=rnd.nextInt(Math.min(max*3,wordItems.length));
                r[2]=rnd.nextInt(Math.min(max*3,wordItems.length));
                int rr=r[rnd.nextInt(6)];
                int box = wordItems[rr].box();
                if(box<15) if(!list.contains(rr))list.add(rr);
            }
        }
        List<Integer> qlist;
        qlist = new ArrayList<Integer>();
        Random rnd=new Random();
        while (qlist.size()<numOfQuestions){
            int r= rnd.nextInt(list.size());
            if(!qlist.contains(list.get(r)))qlist.add(list.get(r));
        }
        for(int i=0;i<numOfQuestions;i++) questions[i]=qlist.get(i);
        return questions;
    }

    private String getword(int id)
    {
        return String.valueOf(wordItems.length);
    }

    public void checkVersionAndUpdate()  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.contains("VERSION")) {
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            try {
                                int version = Integer.parseInt(resp);
                                if(version>getDBVersion())
                                {
                                    progressDialog.show();
                                    updateDatabase(version);
                                }
                                else Toast.makeText(context,"You have the latest version of word database",Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e){
                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                        else  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"Could not connect to server",Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "getdbversion");
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    public  void  updateDatabase(final int version)  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.contains("WORDS")) {
                            String params = response.substring(response.indexOf("WORDS")+6,response.indexOf("{")-1);
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            if(updateFromString(resp,params,false)>0) setDBVersion(version);
                        }
                        else  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        Log.d("Response", response);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"Could not connect to server",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "get1100update");
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    public  void  getSavedWordsOnline()  throws UnsupportedEncodingException
    {
        progressDialog2.show();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        setIsUserWordsDownloaded(true);
                        if(response.contains("WORDS")) {
                            String params = response.substring(response.indexOf("WORDS")+6,response.indexOf("{")-1);
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            updateFromString(resp,params,true);
                            Toast.makeText(context,"Saved progress loaded",Toast.LENGTH_SHORT).show();
                        }
                        else  Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        Log.d("Response", response);
                        progressDialog2.dismiss();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(context,"Could not connect to server",Toast.LENGTH_LONG).show();
                        progressDialog2.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "getSavedWords"+","+getUser());
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }


    int updateFromString(String file,String params,Boolean isSave) //isSave(true)->loadSavedWordsOnlind  (false)->getUpdateWordsOnline
    {
        String []rows = file.replace("$$","~").split("~");
        String []param = params.split(",");
        String all="";
        int count=0,error=0;
        for(int i=0;i<rows.length;i++)
        {
            String row = rows[i];
            row=row.replace("**","~");
            String[] cells = row.split("~");
            String[] cellvalues = new String[param.length];
            for(int j=0;j<cells.length;j++)
            {
                cellvalues[j] = cells[j];
            }
            String line ="";
            wordItem word=new wordItem();
            for (int j=0;j<cells.length;j++)
            {
                word.setparam(param[j],cellvalues[j]);
            }
            if(isSave) {
                try {
                    if (myDB.UpdateSaveItem(word))
                        count++;
                    else error++;
                }catch (Exception e){}
            }else {
                try {
                    if (myDB.UpdateOrInsert(word))
                        count++;
                    else error ++;
                }catch (Exception e){}

            }
        }
        //Toast.makeText(context,"Your word database updated successfully. Updated: "+count+" , Errors: "+error,Toast.LENGTH_LONG).show();
        Cursor cursor = myDB.getAllWords();
        getWordItems(cursor);
        cursor.close();
        return count;
    }
    public  void  loadGetLoading(final MainActivity ma)  throws UnsupportedEncodingException
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(response.contains("getLoading")) {
                            String resp = response.substring(response.indexOf("{") + 1, response.indexOf("}"));
                            setMessages(resp);
                        }
                        ma.ismessagesloaded=true;
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        ma.ismessagesloaded=true;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("command", "getLoading"+","+getUser()+"~"+getPassword());
                //params.put("domain", "http://itsalif.info");
                return params;
            }
        };
        queue.add(postRequest);

    }

    String decode (String persian)
    {
        String[] c = encode.split("~");
        String[] c1 = new String[c.length];
        String[] c2 = new String[c1.length];
        for(int j=0;j<c1.length; j++)
        {
            c1[j] = c[j].split(",")[0];
            c2[j] = c[j].split(",")[1];
        }
        for(int j=0;j<c2.length;j++)
        {
            persian=persian.replace(c2[j], c1[j]);
        }
        return persian;
    }

}
