package base.utils;

import base.config.ProjectConfig;
import org.apache.commons.lang3.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class ContextUtils {
    private static String qiniu;
    private static String domain;
    public static String getHttpDomain(){
        if(StringUtils.isBlank(domain)) {
            StringBuffer sb = new StringBuffer("http://");
            sb.append(getProp("domain"));
            sb.append("/");
            domain = sb.toString();
        }
        return domain;
    }
    public static String img(){
        if(StringUtils.isBlank(qiniu)) {
            StringBuffer sb = new StringBuffer("http://");
            sb.append(getProp("up.domain"));
            sb.append("/");
            qiniu = sb.toString();
        }
        return qiniu;
    }
    public static String img(String key){
        if(StringUtils.isBlank(qiniu)) {
            qiniu = img();
        }
        return qiniu.concat(key);
    }
    public static boolean isAjax(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(header);
    }

    /**
     * 获取配置文件的值
     * @param key
     * @return
     */
    public static String getProp(String key){
        return ProjectConfig.properties.getProperty(key);
    }

    public static String ver(String file){
        StringBuffer sb = new StringBuffer(file);
        String path =getProp("res.path");
        File files = new File(path.concat(file));
        if(files.exists() && files.isFile()){
            sb.append("?").append("v=").append(files.lastModified());
        }
        return sb.toString();
    }

    public static String getUrl(HttpServletRequest request){
        StringBuffer sb = request.getRequestURL();
        sb.append("?").append(request.getQueryString());
        return sb.toString();
    }

    /**
     * 判断是否微信内置浏览器
     * @param request
     * @return
     */
    public static boolean isWeixin(HttpServletRequest request){
        String ua = request.getHeader("user-agent").toLowerCase();
        return StringUtils.indexOf(ua,"micromessenger")>-1;
    }
}
