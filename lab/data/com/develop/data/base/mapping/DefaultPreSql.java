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
 * Ĭ�ϵ�ʵ�ַ���<br/>
 * ������µ����ӣ�ע���'\''��ָ�������ݿ��е�int���� 1)select field1,field2 from table where<br/>
 * field3='#{posi1}' and field4=#{posi2} -> select field1,field2 from table<br/>
 * where field3=? and field4=? <br/>
 * 2)insert into table<br/>
 * values(#{posi1},#{posi2},#{posi3})->insert into table values(?,?,?)<br/>
 * ����ʵ����һ��ͨ�õĽ�����
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
	private SQLInterprete interprete;//������
	private TableMap table;
	private Logger log = Logger.getLogger(this.getClass());
	private Object[] paramValue;
	private String parameterType;
	private Method method;
	private boolean flagObject = false;// ���ֵ�ǴӶ�����ȡ���ǣ���Բ���ֻ��һ������

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
			/*if (parameterType != null&&this.paramValue.length==1)//���ָ���˲�������(�Ǿ͸���Ϊһ������)
			{
				Class paramenterClass = Class.forName(parameterType);
				Class realClass = method.getParameterTypes()[0];
				if (paramenterClass.isAssignableFrom(realClass))// �����պ���
				{
					if(log.isDebugEnabled())
					log.debug("������Ƕ���" + paramenterClass.getName());
					flagObject = true;
				} else
				{
					if(log.isDebugEnabled())
					log.debug("��������Ϊ:" + paramenterClass.getName() + "�����ļ�����:"
							+ realClass.getName());
					throw new RuntimeException("����������ָ�������Ͳ�һ��");
				}
			}*/
		} catch (Exception e)
		{
			// int,string,double��Щ����ֱ�ӹ�ȥ��
		}
		// match(formatRawSql(sql));
		setFullSql(parseRawSql(sql));//��һ����ʽ��ԭʼ���
		if(interprete==null)//ʹ��Ĭ�ϵĽ�����,�ڶ��������ֶ�
		{
			if(log.isInfoEnabled())
				log.info("ʹ��Ĭ�ϵĽ�����:"+this.getClass());
			setSql(parse(this));
		}
		else
		{
			if(log.isInfoEnabled())
			{
				log.info("������Ϊ:"+interprete.getClass());
			}
			setSql(interprete.parse(this));
		}
		setSql(setValues(getPureSQL(),paramValue));//�������ֵ
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
							throw new RuntimeException(key + "û��ӳ���κ��ֶ�");
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
								throw new RuntimeException("����û��" + field);
							sql = sql.replace(field + "@" + key, tfield);
						}
					}
				}
			}
		}
		return sql;
	}

	/**
	 * ���û���SQL���и�ʽ��,�����и�ʽ��飬ȥ������ո�,�����ո�</br> update @Ds_basic set
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
			if (temp == BLANK)// ���ո�
			{

				int m = n + 1;
				for (; bid.charAt(m) == BLANK && m < bid.length(); m++)// �����ո�
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
	 * Ϊ������ֵ
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
					para.setValue(value);// ֵ
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			// System.out.println("flag:" + full);
			//TODO ������α���sql ע�룿����para.getValue().toString(),���д���
			if(para.getValue()==null)
				throw new RuntimeException(para.getName()+"Ϊ��;");
			String value=para.getValue().toString().replaceAll("\'", "\"").replaceAll("--", "����");//������ȫ����ɡ�
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
			if (chars[i] == SINGLE_QUOTES)// �������������
			{
				parseSql.append("\'");
				int over = dealOpenSingleQuotes(chars, i, parseSql);
				i = over;
				parseSql.append("\'");
				continue;
			}
			if (chars[i] == SHARP)// �������#(�����@�ᴦ�����������������AT����)
			{
				BeanParam param = new BeanParam();
				param.setType(param.INT);
				int over = dealSharp(chars, i, param);
				parseSql.append(param.getNewName());
				i = over;// �ѽ���λ�ø���
				continue;// �����ӽ���λ�ÿ�ʼ
			}
			if (chars[i] == AT)// �������@
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
				throw new RuntimeException("û�з��ֽ�������'");
			if (chars[i] == SINGLE_QUOTES)
			{
				flag = i;
				break;
			}
			if (chars[i] == SHARP)// �����#���ţ����ʾ�������
			{
				BeanParam param = new BeanParam();
				param.setType(param.STRING);
				int over = dealSharp(chars, i, param);
				content.append(param.getNewName());
				i = over;// �ѽ���λ�ø���
				if (i == chars.length - 1)// ����������͵�����ˣ��Ϳ϶�������
				{
					throw new RuntimeException("û�з��ֽ�������'");
				}
				continue;// �����ӽ���λ�ÿ�ʼ
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
		if (chars[begin + 1] != OPEN_CURLY)// ����������{����
		{
			throw new RuntimeException("#����λ��" + (begin + 1) + "�Ƿ��ַ�"
					+ chars[begin + 1] + "������Ϊ:{");
		}
		for (int i = begin + 2; i < chars.length; i++)
		{
			if (i == chars.length - 1 && chars[i] != CLOSE_CURLY)
				throw new RuntimeException("û�з��ֽ�������}");
			if (chars[i] == CLOSE_CURLY)
			{
				over = i;
				break;
			}
			para.append(chars[i]);
		}
		param.setName(para.toString().trim());// ����ո�
		int order = getVariableOrder();
		param.setOrder(order + 1);
		extractAt(param);
		setVariableOrder(order);
		getParams().add(param);// �������
		return over;// ���ؽ�����λ��
	}

	/**
	 * ���������������Ƿ���@
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
			param.setName(nameStr.trim());// ����
		} else
		{
			if (strs.length > 2)
			{
				throw new RuntimeException("�����ж��@�����Ƿ�û����ȷ�ر�{}");
			}
			param.setNewName("__" + strs[0] + "_temp_" + strs[1] + "__");
			param.setName(strs[0]);// ����
			param.setOrder(Integer.parseInt(strs[strs.length - 1]));// ��ָ����λ�ô����Զ����ɵ�λ��
		}
		return 0;
	}

	/**
	 * property@class
	 * 
	 * @param chars
	 * @param begin
	 * @return ����0��ʾ������������1��ʾ������
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
				pro.add("__");// �������⴦��
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
	 * ����{}ƥ��(������)
	 * 
	 * @return
	 */
	@Deprecated
	public void match(String sql)
	{
		Stack<Character> stack = new Stack<Character>();
		char[] chars = sql.toCharArray();
		StringBuilder builder = new StringBuilder();// ���������ռλ����ʽ��sql,PreparedStatement
		StringBuilder builderSql = new StringBuilder();// ��������ɾ�������ֵ��sql,Statement
		StringBuilder name = null;// ��¼${name}���������
		boolean remain = true;// ����ǲ���Ҫת���õģ�ʱ�õı�־λ
		int count = 0;// ������һ���м���ռλ��
		boolean remainConstant = false;// ������Щ�����''
		for (int i = 0; i < chars.length; i++)
		{

			if (chars[i] == '\'' && stack.isEmpty())// ��Ϊ�ַ�ʱ��ָ��������sqlֵ����''�������ģ�
			{

				stack.push(chars[i]);
				stack.push('#');// ƥ���Ƿ������ҪתΪ?,������ջ��push��#����,��Ϊ��ʱ����д���Ķ�����������Ҫת���ı���'lixing',�������������ŵĻ���������$�����ǳ��治��Ҫת��
				builderSql.append(chars[i]);// ����Ҫ���浽builderSql��
				continue;// ������Ҫ������buidler��
			}
			if (chars[i] == '#')
			{
				if (!stack.isEmpty())
				{
					Character charHead = stack.peek();
					if (charHead == '#')// �ж��Ƿ���''
					{
						stack.pop();// ���ǰ����һ��#���򵯳�����Ӧ�ı�ʾ''����ȷʵҪתΪ?�ġ���Ӧ��49��
					}
				}

				char next = chars[i + 1];
				if (next != '{')
					throw new RuntimeException("��λ��" + i
							+ "('#')��δ����'{',�������sql���");
				else
				{
					stack.push(next);// push��һ��Ԫ��'{'
					builder.append("?");// ��ԭ����Ԫ��ת����'?'
					name = new StringBuilder();// ��ʼ��name����Ϊ����Ҫ����{name}������
					i++;// �������ӵ�'{'
					remain = false;
					continue;// ����
				}
			}

			else if (!stack.isEmpty())
			// ��Ϊ�գ���ʼƥ��
			{
				char temp = stack.peek();// ����һ��Ԫ��
				if (temp == '#')// ����һ����û�е���#������������ŵĶ����������
				{
					stack.pop();
					remainConstant = true;
					builder.append('\'');
				}
				if (temp == '{' && chars[i] == '}')
				{
					count++;
					stack.pop();// ��������һ��Ԫ�أ������ƥ��'{}'�ɹ�
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
						param.setName(nameStr.trim());// ����
						param.setOrder(count);
					} else
					{
						param.setNewName("__" + strs[0] + "_temp_" + strs[1]
								+ "__");
						param.setName(strs[0]);// ����
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
				} else if (i == chars.length - 1 && temp == '{')// ƥ�䲻�ɹ�
				{
					throw new RuntimeException("'{}'������,�������sql");
				} else if (i == chars.length - 1 && temp == '\'')
				{
					throw new RuntimeException("'\'\''������,�������sql");
				}
			}
			if (remain)
			{
				builder.append(chars[i]);
				builderSql.append(chars[i]);
			}
		}

		String sqlToRunStatement = interpretSQL(builderSql).toString();
		String sqlToRun = interpretSQL(builder).toString();// ���ʺŵ���ʽ
		log.info("ִ��sql:" + sqlToRun);
		log.info("���в���(��־λ):" + count);// ��¼���ж��ٸ�����
		log.info(sqlToRunStatement);
		setSql(sqlToRun);
		setFullSql(sqlToRunStatement);
		return;
	}

	@Deprecated
	public StringBuilder interpretSQL(StringBuilder raw)
	{
		Map<String, String> store = new HashMap<String, String>();
		int position = 0;// �趨��ѯλ��
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

				String table = raw.substring(start + 1, end).trim();// ȡ��������
				if (!store.containsKey(table))
				{
						String tableName = this.table.get(table.toString()).getTableName().trim();
						store.put(table, tableName + "_temp_ ");// ��ͬ�ľͲ�Ҫ�ٴ洢,��Ϊ��ȡ����
					
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
					} while (before >= 0);// ȡ����
					if (property.length() == 0)// û������
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
					throw new RuntimeException("�Ƿ���MQL��ʽ,������@��ͷ");
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
		return parseClsToTable();//�ڶ���ӳ��
	}
}

