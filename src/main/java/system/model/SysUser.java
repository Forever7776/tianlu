package system.model;

import base.model.BaseModel;
import com.wxb.ext.plugin.tablebind.TableBind;

/**
 * Created by liuyj on 2015/5/7.
 */
@TableBind(tableName = "sys_user",pkName = "id")
public class SysUser extends BaseModel<SysUser> {
    public static final SysUser dao = new SysUser();
}