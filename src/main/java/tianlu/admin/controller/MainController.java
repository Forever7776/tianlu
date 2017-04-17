package tianlu.admin.controller;
import base.controller.BaseController;
import com.jfinal.aop.Before;
import com.wxb.ext.route.ControllerBind;
import common.constants.Constants;
import system.interceptor.AdminInterceptor;

@Before(AdminInterceptor.class)
@ControllerBind(controllerKey = "/admin/page",viewPath = "/WEB-INF/view/tianlu/admin")
public class MainController extends BaseController {
    public void index(){
        this.render("index.ftl");
    }

    public void home(){
        this.render("main.ftl");
    }

    public void logout(){
        this.removeSessionAttr(Constants.LOGIN_USER);
        this.redirect("/admin");
    }
}
