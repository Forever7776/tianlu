package system.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import common.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import system.model.SysUser;

/**
 * Created by liuyj on 2015/5/7.
 */
public class AdminInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation ai) {
        //判定用户是否登录
        Controller c = ai.getController();
        String ajax = c.getRequest().getHeader("X-Requested-With");
        boolean isAjax = StringUtils.isNotBlank(ajax) && StringUtils.equals(ajax,"XMLHttpRequest");
        SysUser sysUser = c.getSessionAttr(Constants.LOGIN_USER);
        if(sysUser==null){
            if(isAjax){
                c.setAttr("msg","登陆超时，请重新登陆后操作！");
                c.setAttr("success",false);
                c.renderJson();
            }else {
                c.redirect("/admin");
            }
        }else {
            ai.invoke();
        }
    }
}
