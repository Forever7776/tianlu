package tianlu.admin.controller;
import base.controller.BaseController;
import com.jfinal.aop.Before;
import com.wxb.ext.route.ControllerBind;
import common.constants.Constants;
import system.interceptor.AdminInterceptor;

@Before(AdminInterceptor.class)
@ControllerBind(controllerKey = "/admin/hone",viewPath = "/WEB-INF/view/tianlu/admin")
public class HomePageController extends BaseController {
    //主页图片
    public void index(){
        this.render("index.ftl");
    }

    //主页公告
    public void gongGao(){
        this.render("gonggao.ftl");
    }

    //公司概述
    public void companyIntroduction(){
        this.removeSessionAttr(Constants.LOGIN_USER);
        this.redirect("/admin");
    }
}
