package base.tools;


import base.config.ProjectConfig;
import com.jfinal.upload.UploadFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import system.model.SysFile;

import java.io.File;
import java.util.Date;

/**
 * Desc: show
 * User: 罗铭豪野
 * Time: 2015-01-23 16:43
 */
public class FileUtils {
    /**
     * 获取文件后缀
     * @param path
     * @return
     */
    public static String getFileSuffix(String path){
        if(StringUtils.isBlank(path))
            return StringUtils.EMPTY;
        int lastNum = StringUtils.lastIndexOf(path,".");
        if(lastNum==-1)
            return path;
        return StringUtils.substring(path,lastNum+1);
    }

    public static int getFileType(String fileName){
        if(StringUtils.isBlank(fileName)){
            return 0;
        }
        String suffixStr = ProjectConfig.properties.getProperty("up.suffix.image");
        if(StringUtils.isNotBlank(suffixStr)){
            String suffix = getFileSuffix(fileName);
            boolean isImage = StringUtils.contains(suffixStr,suffix);
            if(isImage){
                return 1;
            }
        }
        return 0;
    }

    public static String getAllFileName(File file){
        if(file==null){
            return StringUtils.EMPTY;
        }
        String name = file.getName();
        String path = getPath();
        StringBuffer sb = new StringBuffer(path);
        String newName = getFileName(name);
        sb.append(newName);
        return sb.toString();
    }
    public static String getPath(){
        String path = ProjectConfig.properties.getProperty("up.path");
        String datePath = DateFormatUtils.format(new Date(),"/yyyy/MM/dd/");
        return path.concat(datePath);
    }
    public static String getSavePath(){
        String path = ProjectConfig.properties.getProperty("up.savepath");
        String datePath = DateFormatUtils.format(new Date(),"/yyyy/MM/dd/");
        return path.concat(datePath);
    }
    public static String getFileName(String name){
        String suffix = getFileSuffix(name);
        String key = System.currentTimeMillis()+""+((int)((Math.random()*9+1)*100000))+"".concat(".").concat(suffix);
        return  key;
    }
    public static File renameFile(UploadFile uploadFile){
        File file = uploadFile.getFile();
        String all = getAllFileName(file);
        File newFile = new File(all);
        file.renameTo(newFile);
        return newFile;
    }

    /**
     * 保存文件系统日志
     * @param file
     * @param path
     * @return
     */
    public static SysFile saveFileInfo(File file, String path){
        String name = file.getName();
        SysFile fileModel = new SysFile();
        fileModel.set("name",name);
        fileModel.set("size",file.length());
        String suffix = FileUtils.getFileSuffix(name);
        fileModel.set("suffix",suffix);
        fileModel.set("type",getFileType(suffix));
        fileModel.set("path",path);
        fileModel.set("status",0);
        fileModel.set("insert_date",new Date());
        fileModel.saveOrUpdate();
        return fileModel;
    }
    public static void main(String... args){
        File file = new File("/resource/upload/abc.jpg");
        System.out.println(getAllFileName(file));
    }
}
