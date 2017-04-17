package tianlu.admin.controller;

import base.controller.BaseController;
import base.tools.MD5;
import com.jfinal.log.Log;
import com.wxb.ext.route.ControllerBind;
import common.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import system.model.SysUser;

@ControllerBind(controllerKey = "/admin/login",viewPath = "/WEB-INF/view/tianlu/admin")
public class AdminController extends BaseController{
    private Log logger = Log.getLog(this.getClass());
    public void index(){
        render("login.ftl");
    }

    //登录
    public void toLogin(){
        String username = this.getPara("name");
        String password = this.getPara("pwd");
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(password)){
            String MD5Password = MD5.GetMD5Code(password);
            SysUser sysUser = SysUser.dao.findFirst("select * from sys_user where username = ?",username);
            if(sysUser!=null){
                if(MD5Password.equals(sysUser.getStr("password"))){
                    if(sysUser.getInt("status")==1){
                        this.setSessionAttr(Constants.LOGIN_USER,sysUser);
                        renderJsonSuccess();
                    }else{
                        this.renderJsonError("登录失败，该用户无效！");
                    }
                }else{
                    this.renderJsonError("密码错误！");
                }
            }else{
                this.renderJsonError("用户名不存在！");
            }
        }else{
            renderJsonError("系统错误");
        }
    }
}
