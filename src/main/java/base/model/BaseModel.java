package base.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.wxb.ext.plugin.sqlinxml.SqlKit;
import com.wxb.ext.plugin.tablebind.TableBind;
import com.wxb.utils.Txt;
import com.wxb.utils.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BaseModel定义了一组常用的方法
 * 以get开头获取单个Model
 * 以list开头获取List<Model>
 */
public class BaseModel<M extends Model<M>> extends Model<M> {

	private static final long serialVersionUID = 8924183967602127690L;

	/***
	 * 用来当 缓存名字 也用来 生成 简单sql
	 */
	public String tableName;
	public String pkName;
	private String querySql;

	/***
	 * 反射获取 注解获得 tablename，pkName
	 */
	public BaseModel(){
		if (StringUtils.isBlank(tableName)){
			TableBind table = this.getClass().getAnnotation(TableBind.class);
			if (table != null){
				tableName = table.tableName();
				pkName = table.pkName();
				querySql="select * from "+tableName;
			}
		}
	}
	/**
	 * 通过ID查找一条数据
	 * @param pk	ID值
	 * @return Model结果
	 */
	public M getByPk(String pk){
		set(pkName,pk);
		return findById(pk);
	}
	
	/**
	 * 通过指定列名和值查找一条数据
	 * @param attr	列名
	 * @param obj	列值
	 * @return Model结果
	 */
	public M getByAttr(String attr,Object obj){
		List<M> list=listByAttrs(null,new String[]{attr},new Object[]{obj});
		return list.size()>0?list.get(0):null;
	}
	/**
	 * 通过指定一组列名和对应值查找一条数据(当结果为多条的时候，返回第一条)
	 * @param attrs 列名数组
	 * @param paras	列值数组
	 * @return Model结果
	 */
	public M getByAttrs(String[] attrs,Object... paras){
		List<M> list=listByAttrs(null,attrs,paras);
		return list.size()>0?list.get(0):null;
	}
	/**
	 * 获取所有
	 * @return
	 */
	public List<M> listAll(){
		return listByAttrs(null,null,new Object[]{});
	}
	public List<M> listOrderBy(String orderBy){
		return listByAttrs(orderBy,null,new Object[]{});
	}
	/**
	 * 通过指定列名和对应值查找一组数据
	 * @param attr	列名
	 * @param para	列值
	 * @return list结果
	 */
	public List<M> listByAttr(String attr,Object para){
		return listByAttrs(null,new String[]{attr}, new Object[]{para});
	}
	/**
	 * 通过指定列名和对应值查找一组数据并按指定列排序
	 * @param orderBy	指定排序的列
	 * @param attr		列明
	 * @param para		列值
	 * @return
	 */
	public List<M> listByAttr(String orderBy,String attr,Object para){
		return listByAttrs(orderBy,new String[]{attr}, new Object[]{para});
	}
	/**
	 * 通过指定一组列名和对应值查找一组数据
	 * @param attrs 列名数组
	 * @param paras	列值数组
	 * @return list结果
	 */
	public List<M> listByAttrs(String[] attrs,Object... paras){
		return listByAttrs(null,attrs,paras);
	}
	/**
	 * 通过指定一组列名和对应值查找一组数据并按指定列排序
	 * @param orderBy	指定排序的列
	 * @param attrs		列名数组
	 * @param paras		列值数组
	 * @return
	 */
	public List<M> listByAttrs(String orderBy,String[] attrs,Object... paras){
		StringBuilder sb=new StringBuilder(512);
		sb.append(querySql);
		if(attrs==null||paras==null||attrs.length<1){
			if(StringUtils.isNotBlank(orderBy)){
				sb.append(" order by ").append(orderBy);
			}
			return find(sb.toString());
		}
		sb.append(" where 1=1 ");
		for(String property:attrs){
			sb.append("and ").append(property).append("=? ");
		}
		if(StringUtils.isNotBlank(orderBy)){
			sb.append(" order by ").append(orderBy);
		}
		return find(sb.toString(), paras);
	}

    /**
     * 通过指定一组列名和对应值查找一组数据并按指定列排序
     * @param orderBy	指定排序的列
     * @param attrs		列名数组
     * @likeWays likeWays 匹配方式数组 = or like
     * @param paras		列值数组
     * @return
     */
    public List<M> listByAttrsWithLikeWay(String orderBy,String[] attrs,String[] likeWays,String andOr,Object... paras){
        StringBuilder sb=new StringBuilder(512);
        sb.append(querySql);
        if(attrs==null||paras==null||attrs.length<1){
            if(StringUtils.isNotBlank(orderBy)){
                sb.append(" order by ").append(orderBy);
            }
            return find(sb.toString());
        }
        sb.append(" where ");
        int i=0;
        for(String property:attrs){
            if(i!=0){
                sb.append(" "+andOr+" ");
            }
            sb.append(property).append(" "+likeWays[i]+"").append("? ");
            i++;
        }
        if(StringUtils.isNotBlank(orderBy)){
            sb.append(" order by ").append(orderBy);
        }
        return find(sb.toString(), paras);
    }

	/**
	 * 通过主键更新一条数据
	 * @param pk	主键
	 * @param attr	列名
	 * @param para	列值
	 * @return
	 */
	public boolean update(String pk,String attr,Object para){
		return update(pk,new String[]{attr},new Object[]{para});
	}
	/**
	 * 通过主键更新一条数据
	 * @param pk	主键
	 * @param attrs	列名数组
	 * @param paras	列值数组
	 * @return
	 */
	public boolean update(String pk,String[] attrs,Object... paras){
		if(pk==null||attrs==null||paras==null)
			return false;
		set(pkName,pk);
		for(int i=0;i<attrs.length&&i<paras.length;i++){
			set(attrs[i],paras[i]);
		}
		return update();
	}
	/**
	 * 通过where条件和值获取第一个
	 * @param where	sql条件
	 * @param params	值
	 * @return
	 */
	public M getByWhere(String where, Object... params){
		List<M> list = listWhere(where, params);
		if (list.size() > 0) return list.get(0);
		else return null;
	}
	/**
	 * 通过自定义条件获取
	 * @param where sql条件
	 * @return
	 */
	public List<M> listWhere(String where){
		return find(querySql + " " + where);
	}
	/**
	 * 通过自定义条件获取
	 * @param where sql条件
	 * @param params 条件值
	 * @return
	 */
	public List<M> listWhere(String where,Object... params){
		return find(querySql + " " + where,params);
	}
	/***
	 * 返回全部的数据 比较方便 但不灵活
	 * @return
	 */
	/*public List<M> listPage(int page, int size){
		if (page < 1) page = 0;
		return find(querySql + " limit " + (page - 1) * size + "," + size);
	}*/
	/**
	 * 保存或更新
	 * (当主键存在时更新数据，不存在时插入数据)
	 * (可自动生成主键)
	 * @return
	 */
	public boolean saveOrUpdate(){
		if (StringUtils.isBlank(getPk())) {
			set(pkName, UUIDGenerator.getUUID());
			return save();
		}
		if(getByPk(getPk())!=null){
			return update();
		}else{
			return save();
	}
}
	/***
	 * if empty remove the attr
	 * 
	 * @param attr
	 */
	public BaseModel<M> emptyRemove(String attr){
		if (get(attr) == null) remove(attr);
		return this;
	}

	public BaseModel<M> emptyZreo(String attr){
		if (get(attr) == null) set(attr, 0);
		return this;
	}

	/***
	 * pks 必需为连续的 1，2，3 这样子
	 * @param pks
	 */
	public boolean batchDelete(Integer pks){
		if (pks==null || pks==0) return false;
		return Db.update("delete from " + tableName + " where " + pkName + " in (" + pks + ")") > 0;
	}

	public List<M> findByCache(String sql){
		return super.findByCache(tableName, sql, sql);
	}

	public List<M> findByCache(String sql, Object... params){
		return super.findByCache(tableName, sql, sql, params);
	}

	public M findFirstByCache(String sql, Object... params){
		List<M> list=super.findByCache(tableName, sql, sql, params);
		return list.size()>0?list.get(0):null;
	}

	public long getCount(String sql){
		sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		return findFirst(" select count(*) as c from" + sql).getLong("c");
	}

	public long getCount(String sql, Object... params){
		sql = Txt.split(sql.toLowerCase(), "from")[1];
		if (sql.contains("order by")) sql = Txt.split(sql, "order by")[0];
		return findFirst(" select count(*) as c from" + sql, params).getLong("c");
	}

	public M putModel(String key, Object value)
	{

		if (value instanceof BaseModel)
		{
			this.put(key, ((BaseModel) value).getAttrs());
		}
		if (value instanceof Record)
		{
			this.put(key, ((Record) value).getColumns());
		}

		if (value instanceof List)
		{
			List models = new ArrayList();
			for (Object obj : (List) value)
			{
				if (obj instanceof BaseModel)
				{
					models.add(((BaseModel) obj).getAttrs());
				}
				if (obj instanceof Record)
				{
					models.add(((Record) obj).getColumns());
				}

			}
			this.put(key, models);
		}

		return (M) this;
	}

	public String getPk(){
		return get(pkName);
	}

	public static String sql(String key){
		return SqlKit.sql(key);
	}

	/**
	 * 判断是否是新增元素
	 * @return
	 */
	public boolean isNew(){
		return StringUtils.isBlank(getPk());
	}

	/**
	 * 设置日期属性
	 * @param key
	 * @return
	 */
	public M setDate(String key){
		return set(key, new Date());
	}

	public M setDate(String key,Date date){
		return set(key, date);
	}
	public M setAid(String aid){
		return set("account_id", aid);
	}
	public M setStatus(Integer status){
		return set("status",status);
	}
	public Integer getStatus(){
		return getInt("status");
	}

	public boolean updateAndLastDate(){
		set("last_date",new Date());
		return update();
	}

	public M setCheckBox(String key){
		return set(key,get(key)==null?0:1);

	}

}
