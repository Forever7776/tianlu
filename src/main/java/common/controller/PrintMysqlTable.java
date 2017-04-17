package common.controller;
import base.config.ProjectConfig;
import base.controller.BaseController;
import com.jfinal.aop.Clear;
import com.jfinal.plugin.activerecord.Db;
import com.wxb.ext.route.ControllerBind;
import common.service.MysqlService;
import java.util.List;

/**
 * Created by kz on 2017/01/16.
 */
@ControllerBind(controllerKey = "/mysql",viewPath = "/WEB-INF/view/common/mysql/")

public class PrintMysqlTable extends  BaseController{
    MysqlService service = new MysqlService();

    public void index(){
        render("mysql.ftl");
    }

    //选择性导出mysql数据库
    public void exportWXMember(){
        try {
            String schema=this.getPara("mysql");
            String templatePath =this.getRequest().getServletContext().getRealPath(ProjectConfig.properties.getProperty("poi.template"));//得到文件夹实际路径
            List<String> listName=Db.query("select table_name from information_schema.tables where table_schema=?",schema);
            if(listName.size()>0){
                service.membersExport(templatePath,schema,listName,this.getResponse());
            }else{
                return;
            }
            renderNull();
        }catch (Exception e){
            this.renderErrorPage("导出失败：系统异常！");
        }
    }
}
