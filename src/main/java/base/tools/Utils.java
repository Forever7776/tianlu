package base.tools;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * Created by luomhy on 2015/5/6.
 */
public class Utils {
    public static String uuid(){
        String s = UUID.randomUUID().toString();
        StringBuffer sb = new StringBuffer();
        //ȥ����-������
        return StringUtils.replace(s, "-", StringUtils.EMPTY);
    }
}
