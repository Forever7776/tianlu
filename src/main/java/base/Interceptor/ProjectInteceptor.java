package base.Interceptor;

import base.utils.ContextUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;

import javax.servlet.http.HttpServletRequest;

/**
 * 拦截器
 */

public class ProjectInteceptor implements Interceptor {
    private  final Log logger = Log.getLog(this.getClass());
    public void intercept(Invocation ai) {
        Controller cl = ai.getController();
        HttpServletRequest request = cl.getRequest();
        boolean isAjax = ContextUtils.isAjax(request);
        ai.invoke();
        if(!isAjax){
            cl.setAttr("context",new ContextUtils());
            cl.setAttr("imgurl", ContextUtils.img());
            cl.setAttr("domain", ContextUtils.getHttpDomain());
        }
    }

}
