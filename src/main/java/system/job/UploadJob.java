package system.job;

import com.jfinal.log.Log;
import common.service.QiNiuService;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import system.model.SysFile;

import java.io.File;
import java.util.List;

public class UploadJob implements Job {
    private static Log logger = Log.getLog(UploadJob.class);
    private static int js = 0;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("文件异常检查程序开始启动--------------");
        ++js;
        logger.info("自服务器启动已运行[" + js + "]次");
        try{
            List<SysFile> fileList = SysFile.dao.listWhere("where status=?",-1);
            if(CollectionUtils.isNotEmpty(fileList)){
                logger.info("[upload]无需要处理的异常文件");
                return;
            }
            logger.info("[upload]定时器需要处理["+fileList.size()+"]个异常文件");
            for(SysFile sysFile :fileList){
                String filePath = sysFile.getStr("path")+sysFile.getStr("name");
                File file = new File(filePath);
                if(file.exists()){
                    logger.info("["+filePath+"]文件存在，开始执行上传七牛任务[start]");
                    QiNiuService.upload(file,sysFile);
                }else{
                    logger.info("["+filePath+"]文件不存在，销毁数据处理");
                    sysFile.set("status",-2);
                    sysFile.update();
                }

            }
        }catch (Exception e){
            logger.error("UploadJob@error",e);
        }
        logger.info("--------------文件异常检查程序结束");
    }
}

