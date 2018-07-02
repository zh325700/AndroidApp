package be.kuleuven.softdev.hehuang.pokepelican.mCloud;
/**
 * Created by shuaigehan on 19/12/2016.
 */

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

/**
 * The type Cloudinary client.
 */
public class CloudinaryClient {
    /**
     * Resize string.
     *
     * @param picname the name of the picture
     * @return the url of the picture being recized.
     */
    public static String resize(String picname) {
        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
        //Manipulate
        Transformation t = new Transformation();
        t.width(300);
        t.height(250);

        return cloud.url().transformation(t).generate(picname + ".jpg");  //返回调整以后的pic url
    }

}
