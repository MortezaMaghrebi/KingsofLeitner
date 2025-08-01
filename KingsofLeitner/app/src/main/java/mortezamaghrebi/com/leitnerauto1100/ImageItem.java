package mortezamaghrebi.com.leitnerauto1100;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageItem {
    public int id;
    public String word;
    public String base64image;
    public Bitmap image;
    public ImageItem(int id,String word,String base64image)
    {
        this.id=id;
        this.word=word;
        this.base64image=base64image;
    }
    public ImageItem(int id,String word,Bitmap base64image)
    {
        this.id=id;
        this.word=word;
        this.image=image;
    }
    public ImageItem()
    {

    }


}

