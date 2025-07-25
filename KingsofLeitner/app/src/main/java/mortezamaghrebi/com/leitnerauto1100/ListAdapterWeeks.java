package mortezamaghrebi.com.leitnerauto1100;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

public class ListAdapterWeeks extends ArrayAdapter<Integer> {

    private int resourceLayout;
    private SecondActivity secondactivity;
    private Context context;
    Controller controller;
    RelativeLayout lytweeks,lytcontent,lytcontentfa;
    Boolean[] daysopen;
    MediaPlayer mpbutton;// = MediaPlayer.create(this, R.raw.select2);
    ListView listview;
    int width;
    int count;
    public ListAdapterWeeks(SecondActivity activity, List<Integer> items) {
        super(activity, R.layout.week_item, items);
        this.context = activity;
        this.secondactivity=activity;
        this.resourceLayout = R.layout.week_item;
        //controller = new Controller(context, true);
        this.lytcontent = secondactivity.findViewById(R.id.lytcontent);
        this.lytweeks = secondactivity.findViewById(R.id.lytweeks);
        this.lytcontentfa = secondactivity.findViewById(R.id.lytcontentfa);
        daysopen = new Boolean[items.size()];
        for(int i=0;i<daysopen.length;i++)daysopen[i]=false;
        mpbutton = MediaPlayer.create(context, R.raw.clicksound);
        this.listview=secondactivity.findViewById(R.id.lstweeks);
        this.count=items.size();
    }
    ImageView imgswipe;
    View.OnClickListener daysclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mpbutton.seekTo(0);
            mpbutton.start();
            if(controller==null)controller = new Controller(context,true);
            String pos = view.getTag().toString().split(",")[0];
            String daystr = view.getTag().toString().split(",")[1];
            int week = Integer.parseInt(pos);
            int day = Integer.parseInt(daystr);
            width=Math.max(width,lytweeks.getMeasuredWidth());
            TextView txtdayweek = lytcontent.findViewById(R.id.txtdayweek);
            TextView txtdayhtml = lytcontent.findViewById(R.id.txtdayhtml);
            TextView txtdayweekfa = lytcontentfa.findViewById(R.id.txtdayweekfa);
            TextView txtdayhtmlfa = lytcontentfa.findViewById(R.id.txtdayhtmlfa);
            imgswipe = lytcontent.findViewById(R.id.imgswipeanim);
            RelativeLayout lytlstlist = secondactivity.findViewById(R.id.lytlistweek);
            lytlstlist.getLayoutParams().width=lytlstlist.getMeasuredWidth();
            lytlstlist.requestLayout();
            txtdayweek.setText("WEEK " + (week + 1) + " DAY " + day);
            txtdayweekfa.setText("هفته " + (week + 1) + " روز " + day);
            String cont = "";
            String contfa="";
            for (int i = 0; i < 5; i++) {
                int item=week * 20 + (day - 1) * 5+i;
                wordItem wi=controller.wordItems[item];
                cont += "<h3>"+wi.word+" / "+wi.pronounce+"</h3>";
                cont+= "<p>"+ wi.definition +"</p>";
                cont+="<i>"+wi.example.replace("_______","<strong>"+wi.word+"</strong>")+"</i>";
                cont+="<br>";
                contfa += "<h3>" +wi.persian+"</h3>";
                contfa+="<p>"+wi.examplefa+"</p>";
                contfa+="<br>";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                txtdayhtml.setText(Html.fromHtml(cont, Html.FROM_HTML_MODE_COMPACT));
                txtdayhtmlfa.setText(Html.fromHtml(contfa, Html.FROM_HTML_MODE_COMPACT));
            } else {
                txtdayhtml.setText(Html.fromHtml(cont));
                txtdayhtmlfa.setText(Html.fromHtml(contfa));
            }
            ResizeWidthAnimation anim = new ResizeWidthAnimation(lytweeks,0);
            anim.setDuration(500);
            lytweeks.startAnimation(anim);
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(controller.getSwipeShown()%5==0) {
                        Animation connectingAnimation = AnimationUtils.loadAnimation(secondactivity, R.anim.swipe);
                        imgswipe.setVisibility(View.VISIBLE);
                        imgswipe.startAnimation(connectingAnimation);
                        imgswipe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                imgswipe.setVisibility(View.INVISIBLE);
                            }
                        });
                        (new Handler()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imgswipe.setVisibility(View.INVISIBLE);
                            }
                        }, 2000);
                    }
                    controller.setSwipeShown();
                }
            },700);
            secondactivity.bookcontentlayout=1;
        }
    };
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            v = vi.inflate(resourceLayout, null);
        }

        TextView txtweeknum = (TextView) v.findViewById(R.id.txtweeknumber);
        TextView txtweek = (TextView) v.findViewById(R.id.txtweek);
        final RelativeLayout lytweek = (RelativeLayout) v.findViewById(R.id.lytweek);
        final LinearLayout lytdays = (LinearLayout) v.findViewById(R.id.lytdays);
        LinearLayout lytday1 = (LinearLayout) v.findViewById(R.id.lytday1);
        LinearLayout lytday2 = (LinearLayout) v.findViewById(R.id.lytday2);
        LinearLayout lytday3 = (LinearLayout) v.findViewById(R.id.lytday3);
        LinearLayout lytday4 = (LinearLayout) v.findViewById(R.id.lytday4);
        lytday1.setTag(""+position+",1");lytday1.setOnClickListener(daysclick);
        lytday2.setTag(""+position+",2");lytday2.setOnClickListener(daysclick);
        lytday3.setTag(""+position+",3");lytday3.setOnClickListener(daysclick);
        lytday4.setTag(""+position+",4");lytday4.setOnClickListener(daysclick);
        txtweeknum.setText(""+(position+1));
        txtweek.setText("Week "+(position+1));
        if(daysopen[position])
        {
            lytdays.getLayoutParams().height=dpToPx(170);
            lytdays.requestLayout();
        }
        else {
            lytdays.getLayoutParams().height=0;
            lytdays.requestLayout();
        }
        lytweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                if(daysopen[position])
                {
                    lytdays.getLayoutParams().height=0;
                    lytdays.requestLayout();
                    daysopen[position]=false;
                }
                else {
                    lytdays.getLayoutParams().height=dpToPx(170);
                    lytdays.requestLayout();
                    daysopen[position]=true;
                }
                for(int i=0;i<daysopen.length;i++) if(i!=position) daysopen[i]=false;
                if(position==count-1)
                {
                    listview.post(new Runnable() {
                        @Override
                        public void run() {
                            // Select the last row so it will scroll into view...
                            listview.setSelection(count-1);
                        }
                    });
                }
            }
        });

        return v;
    }

    public int dpToPx(int dp) {
            return (int)(dp * secondactivity.getResources().getDisplayMetrics().density);
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

}