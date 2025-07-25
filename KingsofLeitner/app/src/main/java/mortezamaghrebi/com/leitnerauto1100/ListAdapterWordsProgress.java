package mortezamaghrebi.com.leitnerauto1100;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapterWordsProgress extends ArrayAdapter<Integer> {

    private int resourceLayout;
    private Context mContext;
    Controller controller;
    int maxwidth;
    MediaPlayer mpbutton;
    public ListAdapterWordsProgress(Activity context, Controller controller, List<Integer> items,int screenwidth) {
        super(context, R.layout.wordprogress_item, items);
        this.mContext = (Context) context;
        this.controller = controller;
        this.maxwidth = screenwidth * 140 / 384;
        mpbutton = MediaPlayer.create(context, R.raw.clicksound);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.wordprogress_item, null);
        }
            TextView word = (TextView) v.findViewById(R.id.txtword);
            TextView week = (TextView) v.findViewById(R.id.txtweek);
            TextView shown = (TextView) v.findViewById(R.id.txtshown);
            RelativeLayout p1 = (RelativeLayout) v.findViewById(R.id.lytwordprogres1);
            RelativeLayout p2 = (RelativeLayout) v.findViewById(R.id.lytwordprogres2);
            RelativeLayout p3 = (RelativeLayout) v.findViewById(R.id.lytwordprogres3);
            RelativeLayout item = (RelativeLayout) v.findViewById(R.id.lytworditem);
            word.setText(controller.wordItems[position].word);
            int day=controller.wordItems[position].day;
            int dayis=((day-1)%4)+1;
            int weekis=(int)((day-1)/4)+1;
            week.setText("Week\n"+weekis+":"+dayis);
            shown.setText(""+controller.wordItems[position].review.length()+" times shown");
            int greenwitdh=maxwidth*controller.wordItems[position].box()/15;
            int redwidth =greenwitdh*controller.wordItems[position].wrongpercent()/1000;
            p1.getLayoutParams().width =maxwidth;
            p2.getLayoutParams().width =greenwitdh;
            p3.getLayoutParams().width =redwidth;
            p1.requestLayout();p2.requestLayout();p3.requestLayout();
            item.setTag(""+position);
            item.setOnClickListener(click);
        return v;
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
    View.OnClickListener click=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos=Integer.parseInt(view.getTag().toString());
            mpbutton.seekTo(0);mpbutton.start();
            WordDialogClass cdd = new WordDialogClass((Activity) mContext,controller.wordItems[pos],controller);
            cdd.show();
        }
    };

}