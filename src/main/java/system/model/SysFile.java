package system.model;

import base.model.BaseModel;
import com.wxb.ext.plugin.tablebind.TableBind;

@TableBind(tableName = "sys_file",pkName = "id")
public class SysFile extends BaseModel<SysFile> {
    public static final SysFile dao = new SysFile();
}
