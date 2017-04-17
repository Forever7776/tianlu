package system.job;

import com.jfinal.log.Log;
import com.wxb.sdk.qiniu.QiNiuApi;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QiNiuRefresh implements Job {
    private Log logger = Log.getLog(this.getClass());
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("[job]-->七牛服务TOKEN刷新<--");
        try {
            QiNiuApi.reload();
        }catch (Exception e){
            logger.error("QiNiuRefresh@refresh error",e);
        }
    }
}
