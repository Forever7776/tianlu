package common.model;

import base.model.BaseModel;
import com.wxb.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "sys_file_log",pkName = "id")
public class SysFileLog extends BaseModel<SysFileLog> {
    public static final SysFileLog dao = new SysFileLog();
}
