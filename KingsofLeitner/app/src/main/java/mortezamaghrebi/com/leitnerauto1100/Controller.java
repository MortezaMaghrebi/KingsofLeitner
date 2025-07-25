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
        try {
            myDB = new DBAdapter( context);
            myDB.createDatabaseIfNeeded();
            myDB.open();
//            DBAdapter dbAdapter = new DBAdapter(MainActivity.this);
//            dbAdapter.createDatabaseIfNeeded();
//            con.setDBVersion(1);
//            con.setUser("Guest");
//            con.setPassword("1234");
//            Cursor cursor = dbAdapter.getAllWords();
//            con.getWordItems(cursor);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
