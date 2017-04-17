package common.service;

import base.utils.ContextUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.log.Log;
import com.wxb.sdk.qiniu.QiNiuApi;
import common.model.SysFileLog;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;
import system.model.SysFile;

import java.io.File;

/**
 * 七牛上传辅助类
 */
public class QiNiuService {
    private static Log logger = Log.getLog(QiNiuService.class);
    private static String domain = StringUtils.EMPTY;

    public static String url(String filename){
        try {
            if (StringUtils.isBlank(domain)) {
                domain = ContextUtils.getProp("up.domain");
            }
            return "http://"+domain+"/"+new URLCodec("UTF-8").encode(filename).replace("+", "%20");
        }catch (Exception e){
            logger.error("QiNiuService@url error",e);
            return StringUtils.EMPTY;
        }
    }
    public void run(File file,SysFile sysFile){
        Thread upThread = new Thread(new UploadProcess(file,sysFile));
        upThread.start();
        logger.info("QiNiu file-Thread[" + upThread.getId() + "] begin on");
    }

    public static boolean upload(File file,SysFile sysFile){
        SysFileLog log = new SysFileLog();
        log.set("file_id", sysFile.getStr("id"));
        log.setDate("start");
        try{
            JSONObject jo = QiNiuApi.upload(file);
            if("SUCCESS".equals(jo.getString("result_code"))) {
                String key = jo.getString("key");
                String hash = jo.getString("hash");
                sysFile.set("key", key);
                sysFile.set("hash", hash);
                sysFile.set("status", 1);
                log.set("status", 1);
                logger.info("[upload]上传结果返回-->key:" + key + "|hash:" + hash);
            }else{
                logger.info("[upload]上传失败");
                sysFile.set("status", -1);
                log.set("status",  0);
            }
            log.setDate("end");
            sysFile.saveOrUpdate();
            return log.saveOrUpdate();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("uploadProcess@QiNiuUpload save model error", e);
        }

        return false;
    }


    class UploadProcess implements Runnable{
        private File file;
        private SysFile sysFile;
        public UploadProcess(File file,SysFile sysFile){
            this.file = file;
            this.sysFile = sysFile;
        }

        @Override
        public void run() {
            upload(file,sysFile);
        }
    }
}
