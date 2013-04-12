package com.develop.data.base.iml;

import com.develop.data.base.ConnectionBase;
import com.develop.data.base.ConnectionInfo;
import com.develop.data.base.ConnectionProvider;
import com.develop.data.base.ConnectionProviderFactory;

/**
 * �����࣬����ConnectionProvider,�����������ģ�����proxool,����������ڻ�û��ѡ���ԣ��Ժ��ع�Ӧ�ü��ϣ����ݲ�ͬ���������ɲ�ͬ�Ķ���
 * @author starlee
 *
 */
public class ConnectionProviderFactoryIml implements ConnectionProviderFactory
{
	public ConnectionProvider createConnectionProvider(ConnectionInfo info)
	{
		ConnectionBase base=new ConnectionBase();
		base.configure(info);
		return base;
	}
}