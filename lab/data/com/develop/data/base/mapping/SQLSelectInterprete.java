package com.develop.data.base.mapping;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author StarLee
 * 
 */
public class SQLSelectInterprete implements SQLInterprete
{

	@Override
	public String parse(PreSql preSql)
	{
		String sql = preSql.getFullSql();
		Map<String, List<String>> fields = preSql.getFields();
		if (!fields.isEmpty())
		{
			Set<String> keys = fields.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext())
			{
				String key = it.next();
				List<String> ls = fields.get(key);
				Iterator<String> itt = ls.iterator();
				for (; itt.hasNext();)
				{
					String field = itt.next();
					if (field.equals("__"))// 表名
					{
						sql = sql.replace(field + "@" + key, preSql
								.getTableMap().get(key).getTableName());
					} else
					{
						if (field.equals("*"))
						{
							Map<String, String> fieldsAll = preSql
									.getTableMap().get(key).getFields();
							Set<Entry<String, String>> lsPair = fieldsAll
									.entrySet();
							Iterator<Entry<String, String>> itPair = lsPair
									.iterator();
							StringBuilder all = new StringBuilder();
							for (; itPair.hasNext();)
							{
								Entry<String, String> entry = itPair.next();
								all.append(entry.getValue());
								all.append(" as ");
								String as = "__" + entry.getKey() + "__";
								all.append(as);
								BeanAsName beanAs = new BeanAsName();
								beanAs.setAsName(as);
								beanAs.setCls(preSql.getTableMap().get(key)
										.getClsName());
								beanAs.setClsProperty(entry.getKey());
								preSql.getAsMapping().put(as, beanAs);
								if (!itPair.hasNext())
									break;
								all.append(",");
							}
							sql = sql
									.replace(field + "@" + key, all.toString());
						} else//关于这一项，其实可以做得更智能，即如果后面有as则采用用户指定的as,所以不建议使用了@，又使用as
						{
							String tfield = preSql.getTableMap().get(key)
									.getFields().get(field);
							if (tfield == null)
								throw new RuntimeException("表"+preSql.getTableMap().get(key).getTableName()+"中没有" + field);
							String replace = field + "@" + key;
							StringBuilder buidler = new StringBuilder();
							buidler.append(tfield);
							int indexForbidReplace = sql.toLowerCase().indexOf(
									"where");
							int currentToReplace = sql.indexOf(replace);
							if (indexForbidReplace==-1||currentToReplace < indexForbidReplace)
							{
								buidler.append(" as ");
								String as = "__" + field + "__";
								buidler.append(as);
								BeanAsName beanAs = new BeanAsName();
								beanAs.setAsName(as);
								beanAs.setCls(preSql.getTableMap().get(key)
										.getClsName());
								beanAs.setClsProperty(field);
								preSql.getAsMapping().put(as, beanAs);
							}
							sql = sql.replace(field + "@" + key,
									buidler.toString());
						}
					}
				}
			}
		}
		return sql;
	}

}
