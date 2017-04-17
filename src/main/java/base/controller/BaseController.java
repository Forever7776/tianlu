package base.controller;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseController extends Controller {
    protected static final String INSERTDATESTR = "insert_date";
    protected static final String LASTDATESTR = "last_date";

    protected void renderJsonSuccess(JSONObject jo){
        renderJson(true, jo);
    }
    protected void renderJsonSuccess(String msg){
        renderJson(true, msg);
    }
    protected void renderJsonSuccess(){
        renderJson(true);
    }
    protected void renderJsonError(String msg){
        renderJson(false, msg);
    }
    protected void renderJsonError(){
        renderJson(false);
    }

    protected void renderJson(boolean rs){
        setAttr("success",rs);
        setAttr("msg", rs ? "SUCCESS" : "FAIL");
        renderJson();
    }
    private void renderJson(boolean rs,String msg){
        setAttr("success",rs);
        setAttr("msg",msg);
        renderJson();
    }
    private void renderJson(boolean rs,JSONObject jo) {
        if(jo==null) {
            jo = new JSONObject();
            jo.put("msg",rs?"SUCCESS":"FAIL");
        }else{
            jo.putAll(jo);
        }
        jo.put("success", rs);
        renderJson(jo.toJSONString());
    }
    public void renderSuccessPage(String url){
        renderSuccessPage(url,StringUtils.EMPTY);
    }
    public void renderSuccessPage(String url,String msg){
        msg = StringUtils.isBlank(msg)?"操作成功":msg;
        renderPage(true,msg,url);
    }
    public void renderErrorPage(){
        renderErrorPage(StringUtils.EMPTY);
    }
    public void renderErrorPage(String msg){
        msg = StringUtils.isBlank(msg)?"系统错误":msg;
        renderPage(false,msg,StringUtils.EMPTY);
    }
    private void renderPage(boolean result,String msg,String url){
        setAttr("success",result);
        setAttr("msg",msg);
        setAttr("call_url",url);
        render("/WEB-INF/view/error/msg.ftl");
    }

    /**
     * 保持参数 和返回页面参数
     * @return
     */
    protected void keepPageParam(){
        this.keepPara();
        Map<String,String[]> param = new HashMap<>();
        param.putAll(getParaMap());
        param.remove("page");
        param.remove("rows");
        StringBuffer sb = new StringBuffer();
        Iterator<String> it =  param.keySet().iterator();
        boolean isFirst = false;
        while(it.hasNext()){
            if(isFirst){
                isFirst = false;
            }else{
                sb.append("&");
            }
            String str = it.next();
            String[] arr = param.get(str);
            String value;
            if(arr.length>1){
                value = StringUtils.join(arr,",");
            }else{
                value = arr[0];
            }
            sb.append(str).append("=").append(value);
        }
        setAttr("urlpara",sb.toString());
    }

    protected void renderFtl(String view){
        render(view+".ftl");
    }

    protected Integer toMoney(String name){
        Double cost_db = NumberUtils.toDouble(getPara(name),0D) *100;
        return cost_db.intValue();
    }

}
