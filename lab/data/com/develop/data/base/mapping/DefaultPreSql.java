package com.develop.data.base.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.develop.data.TestA;

/**
 * 默认的实现方法<br/>
 * 针对如下的样子：注意加'\''的指的是数据库中的int类型 1)select field1,field2 from table where<br/>
 * field3='#{posi1}' and field4=#{posi2} -> select field1,field2 from table<br/>
 * where field3=? and field4=? <br/>
 * 2)insert into table<br/>
 * values(#{posi1},#{posi2},#{posi3})->insert into table values(?,?,?)<br/>
 * 本身实现了一个通用的解释器
 * @author starlee
 * 
 */
public class DefaultPreSql extends PreSqlAbstract implements SQLInterprete
{
	private static final char BLANK = 0x20;
	private static final char SINGLE_QUOTES = 0x27;
	private static final char OPEN_CURLY = 0x7b;
	private static final char CLOSE_CURLY = 0x7d;
	private static final char EQUAL = 0x3d;
	private static final char SHARP = 0x23;
	private static final char AT = 0x40;
	private SQLInterprete interprete;//解释器
	private TableMap table;
	private Logger log = Logger.getLogger(this.getClass());
	private Object[] paramValue;
	private String parameterType;
	private Method method;
	private boolean flagObject = false;// 标记值是从对象中取还是（针对参数只是一个对象）

	public DefaultPreSql()
	{
		this.table=getTableMap();
	}
	
	public void parseSQL(String sql, Object[] paramValue, String parameterType,
			Method method)
	{
		try
		{
			this.paramValue = paramValue;
			this.parameterType = parameterType;
			/*if (parameterType != null&&this.paramValue.length==1)//如果指定了参数类型(那就告诉为一个参数)
			{
				Class paramenterClass = Class.forName(parameterType);
				Class realClass = method.getParameterTypes()[0];
				if (paramenterClass.isAssignableFrom(realClass))// 参数刚好是
				{
					if(log.isDebugEnabled())
					log.debug("处理的是对象" + paramenterClass.getName());
					flagObject = true;
				} else
				{
					if(log.isDebugEnabled())
					log.debug("参数类型为:" + paramenterClass.getName() + "配置文件类型:"
							+ realClass.getName());
					throw new RuntimeException("参数类型与指定的类型不一致");
				}
			}*/
		} catch (Exception e)
		{
			// int,string,double这些就是直接过去了
		}
		// match(formatRawSql(sql));
		setFullSql(parseRawSql(sql));//第一步格式化原始语句
		if(interprete==null)//使用默认的解释器,第二步解释字段
		{
			if(log.isInfoEnabled())
				log.info("使用默认的解释器:"+this.getClass());
			setSql(parse(this));
		}
		else
		{
			if(log.isInfoEnabled())
			{
				log.info("解释器为:"+interprete.getClass());
			}
			setSql(interprete.parse(this));
		}
		setSql(setValues(getPureSQL(),paramValue));//具体变量值
	}

	private String parseClsToTable()
	{
		String sql = getFullSql();
		Map<String, List<String>> fields = getFields();
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
					if (field.equals("__"))
					{
						sql = sql.replace(field + "@" + key, table.get(key)
								.getTableName());
					} else
					{
						if (table.get(key).getFields() == null)
						{
							throw new RuntimeException(key + "没有映射任何字段");
						}
						if (field.equals("*"))
						{
							 Map<String,String> fieldsAll=table.get(key).getFields();
							 Iterator<String> ait=fieldsAll.values().iterator();
							 StringBuilder all=new StringBuilder();
							 for(;ait.hasNext();)
							 {
								 all.append(ait.next());
								 if(!ait.hasNext())
									 break;
								 all.append(",");
							 }
							 sql = sql.replace(field + "@" + key, all.toString());
						} else
						{
							
							String tfield = table.get(key).getFields()
									.get(field);
							if (tfield == null)
								throw new RuntimeException("表中没有" + field);
							sql = sql.replace(field + "@" + key, tfield);
						}
					}
				}
			}
		}
		return sql;
	}

	/**
	 * 将用户的SQL进行格式化,并进行格式检查，去掉多余空格,或加入空格</br> update @Ds_basic set
	 * college_name=' # { collegeName } ' where id=#{id@1} and
	 * name='#{name@1}'-></br> update @Ds_basic set college_name =
	 * '#{collegeName}' where id = #{id@1} and name = '#{name@1}'
	 * 
	 * @return
	 */
	@Deprecated
	public String formatRawSql(String rawSQL)
	{
		StringBuilder bid = new StringBuilder(rawSQL.trim());
		StringBuilder format = new StringBuilder();
		for (int n = 0; n < bid.length(); n++)
		{
			char temp = bid.charAt(n);
			if (temp == BLANK)// 即空格
			{

				int m = n + 1;
				for (; bid.charAt(m) == BLANK && m < bid.length(); m++)// 连续空格
				{
					continue;
				}
				if (special(format.charAt(format.length() - 1)))
				{
					if (bid.charAt(m) != SINGLE_QUOTES
							&& format.charAt(format.length() - 1) == CLOSE_CURLY)
						format.append(temp);
					if (format.charAt(format.length() - 2) == CLOSE_CURLY)
						format.append(temp);
				} else
					format.append(temp);
				n = m;
				format.append(bid.charAt(n));
				continue;
			}
			if (temp == EQUAL)
			{
				format.append(" ");
				format.append(temp);
				if (bid.charAt(n + 1) != BLANK)
					format.append(" ");
				continue;
			}
			format.append(temp);
		}
		//System.out.println(rawSQL);
		//System.out.println(format.toString());
		return format.toString();
	}

	public boolean special(char ch)
	{
		return ch == SINGLE_QUOTES || ch == SHARP || ch == OPEN_CURLY
				|| ch == CLOSE_CURLY;
	}

	/**
	 * 为变量赋值
	 * 
	 * @param obj
	 */
	public String setValues(String full,Object[] obj)
	{
		
		for (BeanParam para : getParams())
		{
			Object objj = obj[para.getOrder() - 1];

			if (isPrimitive(objj.getClass()))
				para.setValue(objj);
			else
			{
				try
				{
					Object value = PropertyUtils.getProperty(objj,
							para.getName());
					para.setValue(value);// 值
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			// System.out.println("flag:" + full);
			//TODO 对于如何避免sql 注入？？对para.getValue().toString(),进行处理
			if(para.getValue()==null)
				throw new RuntimeException(para.getName()+"为空;");
			String value=para.getValue().toString().replaceAll("\'", "\"").replaceAll("--", "——");//单引号全部变成“
			full = full.replace(para.getNewName(), value);
		}
		return full;
	}

	public boolean isPrimitive(Class cls)
	{
		return cls.isAssignableFrom(Integer.class)
				|| cls.isAssignableFrom(Double.class)
				|| cls.isAssignableFrom(Boolean.class)
				|| cls.isAssignableFrom(Float.class)
				|| cls.isAssignableFrom(String.class);
	}

	public String parseRawSql(String sql)
	{
		StringBuilder parseSql = new StringBuilder();

		char[] chars = sql.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			if (chars[i] == SINGLE_QUOTES)// 如果遇到单引号
			{
				parseSql.append("\'");
				int over = dealOpenSingleQuotes(chars, i, parseSql);
				i = over;
				parseSql.append("\'");
				continue;
			}
			if (chars[i] == SHARP)// 如果遇到#(里面的@会处理，而不是留给下面的AT处理)
			{
				BeanParam param = new BeanParam();
				param.setType(param.INT);
				int over = dealSharp(chars, i, param);
				parseSql.append(param.getNewName());
				i = over;// 把结束位置给出
				continue;// 继续从结束位置开始
			}
			if (chars[i] == AT)// 如果遇到@
			{
				int flag = dealAt(chars, i);
				if (flag == 0)
					parseSql.append("__");
			}
			parseSql.append(chars[i]);
		}
		// System.out.println("new:"+parseSql.toString());
		log.info(parseSql.toString());
		return parseSql.toString();
	}

	private int dealOpenSingleQuotes(char[] chars, int begin,
			StringBuilder builder)
	{
		int flag = begin;
		StringBuilder content = new StringBuilder();
		for (int i = begin + 1; i < chars.length; i++)
		{
			if (i == chars.length - 1 && chars[i] != SINGLE_QUOTES)
				throw new RuntimeException("没有发现结束符：'");
			if (chars[i] == SINGLE_QUOTES)
			{
				flag = i;
				break;
			}
			if (chars[i] == SHARP)// 如果是#符号，则表示处理变量
			{
				BeanParam param = new BeanParam();
				param.setType(param.STRING);
				int over = dealSharp(chars, i, param);
				content.append(param.getNewName());
				i = over;// 把结束位置给出
				if (i == chars.length - 1)// 如果它出来就到最后了，就肯定出错了
				{
					throw new RuntimeException("没有发现结束符：'");
				}
				continue;// 继续从结束位置开始
			}
			content.append(chars[i]);
		}
		builder.append(content);
		return flag;
	}

	/**
	 * 
	 * @param chars
	 * @param begin
	 * @param param
	 * @return
	 */
	private int dealSharp(char[] chars, int begin, BeanParam param)
	{
		int over = begin;
		StringBuilder para = new StringBuilder();
		if (chars[begin + 1] != OPEN_CURLY)// 后面必须跟随{符号
		{
			throw new RuntimeException("#后面位置" + (begin + 1) + "非法字符"
					+ chars[begin + 1] + "，必须为:{");
		}
		for (int i = begin + 2; i < chars.length; i++)
		{
			if (i == chars.length - 1 && chars[i] != CLOSE_CURLY)
				throw new RuntimeException("没有发现结束符：}");
			if (chars[i] == CLOSE_CURLY)
			{
				over = i;
				break;
			}
			para.append(chars[i]);
		}
		param.setName(para.toString().trim());// 清除空格
		int order = getVariableOrder();
		param.setOrder(order + 1);
		extractAt(param);
		setVariableOrder(order);
		getParams().add(param);// 加入变量
		return over;// 返回结束的位置
	}

	/**
	 * 检查变量名字里面是否有@
	 * 
	 * @param param
	 * @return
	 */
	private int extractAt(BeanParam param)
	{
		String nameStr = param.getName();
		String[] strs = param.getName().split("@");
		if (strs.length == 1)
		{
			param.setNewName("__" + nameStr + "_temp_" + param.getOrder()
					+ "__");
			param.setName(nameStr.trim());// 名字
		} else
		{
			if (strs.length > 2)
			{
				throw new RuntimeException("变量中多个@，你是否没有正确关闭{}");
			}
			param.setNewName("__" + strs[0] + "_temp_" + strs[1] + "__");
			param.setName(strs[0]);// 名字
			param.setOrder(Integer.parseInt(strs[strs.length - 1]));// 用指定的位置代替自动生成的位置
		}
		return 0;
	}

	/**
	 * property@class
	 * 
	 * @param chars
	 * @param begin
	 * @return 返回0表示是类名，返回1表示是属性
	 */
	private int dealAt(char[] chars, int begin)
	{
		StringBuilder cls = new StringBuilder();
		StringBuilder property = new StringBuilder();
		for (int i = begin + 1; i < chars.length; i++)
		{
			char flag = chars[i];
			if (flag != 0x3e && flag != 0x3c && flag != 0x3d && flag != 0x20)
			{
				cls.append(flag);
			} else
			{
				break;
			}
		}
		Map<String, List<String>> clsMap = getFields();

		if (!clsMap.containsKey(cls.toString().trim()))
		{
			clsMap.put(cls.toString().trim(), new ArrayList<String>());
		}
		List<String> pro = clsMap.get(cls.toString().trim());
		for (int i = begin - 1; i > -1; i--)
		{
			char flag = chars[i];
			if (flag != 0x3e && flag != 0x3c && flag != 0x3d && flag != 0x20)
			{
				property.append(flag);
			} else
			{
				break;
			}
		}
		if (pro.contains(property.toString().trim()))
			return 1;
		else
		{
			if (property.toString().equals(""))
			{
				pro.add("__");// 对类特殊处理
				return 0;
			}
			pro.add(property.reverse().toString().trim());
			return 1;
		}
	}
	@Override
	public int getVariableOrder()
	{
		if(this.paramValue.length==1)
			return 0;
		return super.getVariableOrder();
	}

	@Override
	public void setVariableOrder(int variableOrder)
	{
		// TODO Auto-generated method stub
		if(this.paramValue.length==1)
			super.setVariableOrder(0);
		super.setVariableOrder(variableOrder);
	}
	
	

	/**
	 * 进行{}匹配(即变量)
	 * 
	 * @return
	 */
	@Deprecated
	public void match(String sql)
	{
		Stack<Character> stack = new Stack<Character>();
		char[] chars = sql.toCharArray();
		StringBuilder builder = new StringBuilder();// 这个是生成占位符形式的sql,PreparedStatement
		StringBuilder builderSql = new StringBuilder();// 这个是生成具有完整值的sql,Statement
		StringBuilder name = null;// 记录${name}里面的名字
		boolean remain = true;// 这个是不需要转换用的，时用的标志位
		int count = 0;// 计数，一共有几个占位符
		boolean remainConstant = false;// 对于那些常规的''
		for (int i = 0; i < chars.length; i++)
		{

			if (chars[i] == '\'' && stack.isEmpty())// 当为字符时（指的是数据sql值是用''括起来的）
			{

				stack.push(chars[i]);
				stack.push('#');// 匹配是否真的需要转为?,所以往栈中push‘#’号,因为有时候是写死的东西，并不是要转换的比如'lixing',如果不嫌弃多符号的话，可以用$代替是常规不需要转换
				builderSql.append(chars[i]);// 但需要保存到builderSql中
				continue;// 并不需要保存在buidler中
			}
			if (chars[i] == '#')
			{
				if (!stack.isEmpty())
				{
					Character charHead = stack.peek();
					if (charHead == '#')// 判断是否是''
					{
						stack.pop();// 如果前面有一个#了则弹出，对应的表示''中是确实要转为?的。对应第49行
					}
				}

				char next = chars[i + 1];
				if (next != '{')
					throw new RuntimeException("在位置" + i
							+ "('#')后未发现'{',请检查你的sql语句");
				else
				{
					stack.push(next);// push第一个元素'{'
					builder.append("?");// 对原来的元素转换成'?'
					name = new StringBuilder();// 初始化name，因为我们要保存{name}的名字
					i++;// 跳过紧接的'{'
					remain = false;
					continue;// 跳过
				}
			}

			else if (!stack.isEmpty())
			// 不为空，则开始匹配
			{
				char temp = stack.peek();// 最上一个元素
				if (temp == '#')// 到这一步还没有弹出#则表明，单引号的东西不被替代
				{
					stack.pop();
					remainConstant = true;
					builder.append('\'');
				}
				if (temp == '{' && chars[i] == '}')
				{
					count++;
					stack.pop();// 弹出最上一个元素，这个是匹配'{}'成功
					BeanParam param = new BeanParam();
					if (stack.isEmpty())
					{
						param.setType(BeanParam.INT);
					} else
					{
						param.setType(BeanParam.STRING);
					}

					String nameStr = name.toString();
					String[] strs = nameStr.split("@");
					if (strs.length == 1)
					{
						param.setNewName("__" + nameStr + "_temp_" + count
								+ "__");
						param.setName(nameStr.trim());// 名字
						param.setOrder(count);
					} else
					{
						param.setNewName("__" + strs[0] + "_temp_" + strs[1]
								+ "__");
						param.setName(strs[0]);// 名字
						param.setOrder(Integer.parseInt(strs[strs.length - 1]));
					}
					getParams().add(param);
					builderSql.append(param.getNewName());
				}
				if (stack.isEmpty())
				{
					remain = true;
					continue;
				}
				if (temp == '{' && chars[i] != '}')
				{
					name.append(chars[i]);
				}

				if (temp == '\'' && chars[i] == '\'')
				{
					stack.pop();
					if (remainConstant)
					{
						builder.append('\'');
						remainConstant = false;
					}
					builderSql.append('\'');
					remain = true;
					continue;
				} else if (i == chars.length - 1 && temp == '{')// 匹配不成功
				{
					throw new RuntimeException("'{}'不完整,请检查你的sql");
				} else if (i == chars.length - 1 && temp == '\'')
				{
					throw new RuntimeException("'\'\''不完整,请检查你的sql");
				}
			}
			if (remain)
			{
				builder.append(chars[i]);
				builderSql.append(chars[i]);
			}
		}

		String sqlToRunStatement = interpretSQL(builderSql).toString();
		String sqlToRun = interpretSQL(builder).toString();// 是问号的形式
		log.info("执行sql:" + sqlToRun);
		log.info("共有参数(标志位):" + count);// 记录共有多少个参数
		log.info(sqlToRunStatement);
		setSql(sqlToRun);
		setFullSql(sqlToRunStatement);
		return;
	}

	@Deprecated
	public StringBuilder interpretSQL(StringBuilder raw)
	{
		Map<String, String> store = new HashMap<String, String>();
		int position = 0;// 设定查询位置
		do
		{
			int start = raw.indexOf("@", position);
			if (start != -1)
			{
				int end = start;
				while (raw.charAt(end) != '=' && raw.charAt(end) != ' '
						&& raw.charAt(end) != '>' && raw.charAt(end) != '<'
						&& end < raw.length())
					end++;

				String table = raw.substring(start + 1, end).trim();// 取出对象名
				if (!store.containsKey(table))
				{
						String tableName = this.table.get(table.toString()).getTableName().trim();
						store.put(table, tableName + "_temp_ ");// 相同的就不要再存储,且为表取别名
					
				}
				if (start != 0)
				{
					int before = start - 1;
					StringBuilder property = new StringBuilder();
					char pchar = ' ';
					do
					{
						pchar = raw.charAt(before);
						if (pchar != '=' && pchar != ' ' && pchar != '<'
								&& pchar != '>')
						{
							before--;
							property.append(pchar);
						} else
						{
							break;
						}
					} while (before >= 0);// 取属性
					if (property.length() == 0)// 没有属性
					{
						raw.replace(start, end + 1, this.table.get(table)
								.getTableName() + " as " + store.get(table));
					} else
					{
						int pos = before + 1;
						if (before == 0)
							pos = 0;
						raw.replace(pos, end, store.get(table).trim() + "."
								+ property.reverse().toString());
					}
				}

				else
				//
				{
					throw new RuntimeException("非法的MQL形式,不能以@开头");
				}
				// position = end;
			} else
			{
				break;
			}
		} while (true);
		return raw;
	}

	public static void main(String[] args)
	{
		PreSql preSql = new DefaultPreSql();

		preSql.parseSQL(
				"insert into table values('#{posi1@3}','#{posi2@1}',#{posi3@2})",
				new Object[]
				{ 1, true, 2.3 }, null, null);
		System.out.println("1:"+preSql.getFullSql());
		System.out.println("1:"+preSql.getPureSQL());

		/*
		 * Object[] obj = new Object[] { 1, 2, 3 }; for (BeanParam para :
		 * preSql.getParams()) { //
		 * System.out.println(para.getName()+":"+para.getOrder
		 * ()+":"+para.getType()+":"+para.getValue());
		 * para.setValue(obj[para.getOrder() - 1]); full =
		 * full.replace(para.getNewName(), para.getValue().toString()); }
		 */
		// preSql.parseSQL("select * from Ds_basic", null);
		PreSql preSql2 = new DefaultPreSql();
		preSql2.parseSQL("select *@Ds_basic from @Ds_basic where ygbh='#{id}'",
				new Object[]
				{ 1 }, "string", null);
		System.out.println("2:"+preSql2.getFullSql());
		System.out.println("2:"+preSql2.getPureSQL());
		PreSql preSql3 = new DefaultPreSql();
		preSql3.setSQLInterprete(new SQLSelectInterprete());
		preSql3.parseSQL(
				"select *@Ds_basic from Ds_basic where ygbh='1141' and college_name@Ds_basic='#{dfdfdf}'",
				new Object[]
				{ 1 }, "string", null);
		System.out.println(preSql3.getFullSql());
		System.out.println(preSql3.getPureSQL());
		PreSql preSql4 = new DefaultPreSql();
		TestA a = new TestA();
		a.setCollegeName("cuc");
		a.setId(23);
		a.setName("lixing");
		// update @Ds_basic set Ds_basic.college_name='#{collegeName@1}' where
		// Ds_basic.id=#{id@1} and name='#{name@2}'
		preSql4.setSQLInterprete(new SQLSelectInterprete());
		preSql4.parseSQL(
				"@Ds_basic update @Ds_basic set college_name@Ds_basic<='#{collegeName}' where id@Ds_basic=<#{id@1} and name@Ds_basic='#{name@2}'",
				new Object[]
				{ a, "afdfd" }, null, null);
		System.out.println("4:"+preSql4.getFullSql());
		System.out.println("4:"+preSql4.getPureSQL());
		// DefaultPreSql sql = (DefaultPreSql) preSql3;
		// sql.formatRawSql("update @Ds_basic  set college_name=<' #  { collegeName } ' where id = #{id@1} and name='#{name@1}'");
		DefaultPreSql preSql5 = new DefaultPreSql();
		preSql5.parseRawSql("update @Ds_basic  set college_name@Ds_basic=<' #{ collegeName }' where id = #{id@1} and name='#{name@1}'");
		DefaultPreSql preSql6 = new DefaultPreSql();
		preSql6.parseRawSql("update Ds_basic  set college_name=<'#{ collegeName }' where id = #{id@1} and name='#{name@1}'");
	}

	@Override
	public void setSQLInterprete(SQLInterprete interprete)
	{
		this.interprete=interprete;
	}

	@Override
	public String parse(PreSql preSql)
	{
		return parseClsToTable();//第二步映射
	}
}

