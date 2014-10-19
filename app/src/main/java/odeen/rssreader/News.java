package odeen.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Женя on 17.10.2014.
 */
public class News {
    private UUID mId;
    private String mTitle;
    private String mURL;
    private Date mPubDate;
    private String mDescription;
    private String mImageLink;
    private Bitmap mImage;
    public News() {
        mId = UUID.randomUUID();
        mPubDate = new Date();
        mImageLink = "";//TODO: BAD!
    }
    public UUID getId() {
        return mId;
    }
    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String mURL) {
        this.mURL = mURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle.trim();
    }

    public Date getPubDate() {
        return mPubDate;
    }

    public void setPubDate(Date mPubDate) {
        this.mPubDate = mPubDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription.trim();
    }

    public String getImageLink() {
        return mImageLink;
    }

    public void setImageLink(String mImageLink) {
        this.mImageLink = mImageLink;
    }
    public Bitmap getImage() {
        return mImage;
    }
    public void decodeBitmap(String url) throws IOException {
        mImage = decodeSampledBitmap(url, 100, 100);
    }
    public static Bitmap decodeSampledBitmap(String url, int reqWidth, int reqHeight) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new URL(url).openStream(), null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeStream(new URL(url).openStream(), null, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
