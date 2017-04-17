package tianlu.platform.controller;

import base.controller.BaseController;
import com.wxb.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/",viewPath = "/WEB-INF/view/tianlu/platform")
public class IndexController extends BaseController {
    public void index(){
        this.render("index.ftl");
    }

    public void chain(){
        this.render("chain.ftl");
    }

    public void base(){
        this.render("base.ftl");
    }

    public void platform(){
        this.render("platform.ftl");
    }

    public void standard(){
        this.render("standard.ftl");
    }

    public void merchan(){
        this.render("merchan.ftl");
    }

    public void website(){
        this.render("website.ftl");
    }

    public void about(){
        this.render("about.ftl");
    }
}
