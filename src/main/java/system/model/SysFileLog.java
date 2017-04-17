package system.model;


import base.model.BaseModel;
import com.wxb.ext.plugin.tablebind.TableBind;

/**
 * Created by liuyj on 2015/5/7.
 */
@TableBind(tableName = "sys_file_log",pkName = "id")
public class SysFileLog extends BaseModel<SysFileLog> {
    public static final SysFileLog dao = new SysFileLog();
}
