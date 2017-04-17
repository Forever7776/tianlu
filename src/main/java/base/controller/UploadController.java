package base.controller;

import base.utils.FileUtils;
import com.jfinal.aop.Clear;
import com.jfinal.log.Log;
import com.jfinal.upload.UploadFile;
import com.wxb.ext.route.ControllerBind;
import com.wxb.sdk.wx.api.ApiConfigKit;
import com.wxb.sdk.wx.api.ApiResult;
import com.wxb.sdk.wx.api.MaterialApi;
import common.service.QiNiuService;
import org.apache.commons.lang3.StringUtils;
import system.model.SysFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 *上传文件基类
 */
@ControllerBind(controllerKey = "/up",viewPath = "/WEB-INF/view/")
public class UploadController extends BaseController {
    private Log logger = Log.getLog(this.getClass());
    @Clear
    public void listUpload(){
        try {
            List<String> urlList = new LinkedList<String>();
            String path = FileUtils.getPath();
            List<UploadFile> fileList = getFiles();
            SysFile sysFile;
            File tempFile;
            for (UploadFile file : fileList) {
                tempFile= FileUtils.renameFile(file);
                sysFile = FileUtils.saveFileInfo(tempFile,path);
                if(sysFile.getInt("id")!=null) {
                    QiNiuService service = new QiNiuService();
                    service.run(tempFile, sysFile);
                    urlList.add(service.url(tempFile.getName()));
                }
            }
            setAttr("file", urlList);
            setAttr("success",true);
        }catch (Exception e){
            logger.error("UploadController@selfUpload error",e);
            setAttr("success",false);
        }
        renderJson();
    }
    @Clear
    public void selfUpload(){
        try {
            String key = this.getPara("filekey", "file");//获得自定义文件键值，默认file
            String path = FileUtils.getPath();//获得路径
            UploadFile file = this.getFile(key, path);//获得文件对象 进行了设置保存路径
            File tempFile = FileUtils.renameFile(file);//改变文件名称
            SysFile result = FileUtils.saveFileInfo(tempFile,path);//保存文件日志对象
            if(StringUtils.isNoneBlank(result.getStr("id"))) {
                String fileName = tempFile.getName();
                setAttr("key",fileName);
                QiNiuService service = new QiNiuService();
                service.run(tempFile, result);
                setAttr("id", result.getPk());
                setAttr("url", service.url(fileName));
                setAttr("success",true);
            }else{
                setAttr("success",false);
                setAttr("msg","保存文件信息失败~");
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("UploadController@selfUpload error",e);
            setAttr("success",false);
        }
        renderJson();
    }
    @Clear
    public void kindup(){
        try{
            String key = "imgFile";//获得自定义文件键值，默认file
            String path = FileUtils.getPath();//获得路径
            UploadFile file = getFile(key, path);//获得文件对象 进行了设置保存路径
            File tempFile = FileUtils.renameFile(file);//改变文件名称
            SysFile result = FileUtils.saveFileInfo(tempFile,path);//保存文件日志对象
            if(StringUtils.isNoneBlank(result.getStr("id"))) {
                QiNiuService.upload(tempFile, result);
                String fileName = tempFile.getName();
                setAttr("key", fileName);
                setAttr("error",0);
                setAttr("url", QiNiuService.url(fileName));
            }else{
                setAttr("error",1);
                setAttr("msg","上传程序错误。");
            }
        }catch (Exception e){
            logger.error("UploadController@kindup error", e);
            setAttr("error",1);
            setAttr("msg","上传程序错误。");
        }
        renderJson();
    }
    @Clear
    public void wxup(){
        try{
            String key = this.getPara("filekey", "file");//获得自定义文件键值，默认file
            String path = FileUtils.getPath();//获得路径
            UploadFile file = this.getFile(key, path);//获得文件对象 进行了设置保存路径
            File tempFile = FileUtils.renameFile(file);//改变文件名称
            SysFile result = FileUtils.saveFileInfo(tempFile,path);//保存文件日志对象
            ApiResult apiResult = MaterialApi.createFileMaterial(tempFile, "image");
            logger.info("[wxupload]微信上传文件开启-->["+apiResult.getJson()+"]");
            if(!apiResult.isSucceed()){
                setAttr("success",false);
                setAttr("msg","上传到微信服务器失败~");
                renderJson();
                return;
            }
            String media_id = apiResult.getStr("media_id");
            result.set("media_id",media_id);
            result.saveOrUpdate();
            if(StringUtils.isNoneBlank(result.getStr("id"))) {
                String fileName = tempFile.getName();
                setAttr("key",fileName);
                QiNiuService.upload(tempFile,result);
                setAttr("media_id", media_id);
                setAttr("id", result.getStr("id"));
                setAttr("url", QiNiuService.url(tempFile.getName()));
                setAttr("success", true);
            }else{
                setAttr("success",false);
                setAttr("msg","保存文件信息失败~");
            }
        }catch (Exception e){
            logger.error("UploadController@wxup error",e);
            setAttr("success",false);
        }
        renderJson();
    }

}
