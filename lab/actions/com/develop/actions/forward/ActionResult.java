package com.develop.actions.forward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.develop.actions.action.ActionInvocation;
import com.develop.actions.bean.ActionBean;
import com.develop.actions.result.AbstractResult;

public class ActionResult extends RedirectResult
{
	private String namespace;
	private String actionName;

	public String getNamespace()
	{
		return namespace;
	}

	public void setNamespace(String namespace)
	{
		this.namespace = namespace;
	}

	public String getActionName()
	{
		return actionName;
	}

	public void setActionName(String actionName)
	{
		this.actionName = actionName;
	}

	@Override
	public void execute(ActionInvocation actionInvocation)
	{
		ActionBean actionBean = (ActionBean) actionInvocation
				.getConfiguration().getAction(namespace, actionName);

		if (actionName.contains("?"))// 这个地方主要是是处理参数的
		{
			StringBuilder location = new StringBuilder();
			location.append(actionName.substring(0, actionName.lastIndexOf("?")));
			location.append(".do?");
			
			String queryParam = actionName.substring(
					actionName.lastIndexOf("?") + 1, actionName.length());
			String[] queryParams = queryParam.split("&");
			List<String> list = new ArrayList<String>();
			for (String query : queryParams)
			{
				StringBuilder builderPrefix = new StringBuilder();
				StringBuilder builder = new StringBuilder();
				Stack<Character> stack = new Stack<Character>();
				for (int i = 0; i < query.length(); i++)
				{
					if (query.charAt(i) == '{')
					{
						stack.push('{');
						continue;
					}
					if (query.charAt(i) == '$')
						continue;

					if (!stack.isEmpty())
					{
						if ((char) stack.peek() == '{'
								&& query.charAt(i) == '}')
						{
							stack.pop();
							builderPrefix.append((String) actionInvocation
									.getValueStack().findValue(
											builder.toString().trim()));
							list.add(builderPrefix.toString());
							break;
						}

						builder.append(query.charAt(i));
						continue;
					}
					builderPrefix.append(query.charAt(i));
				}
			}
			for (Iterator<String> it = list.iterator(); it.hasNext();)
			{
				
				location.append(it.next());
				if (it.hasNext())
					location.append("&");
			}
			actionName=location.toString();
		}
		else
		{
			actionName=actionName+".do";
		}
		if("/".equals(namespace))
			setLocation("/"+actionName);
		else {
			setLocation(namespace+"/"+actionName);
		}
		super.execute(actionInvocation);
	}

	public static void main(String[] args)
	{
		String actionName = "?id=id&name=${name}";
		Map map = new HashMap();
		map.put("id", "11");
		map.put("name", "lixing");
		if (actionName.contains("?"))// 这个地方主要是是处理参数的
		{

			String queryParam = actionName.substring(
					actionName.lastIndexOf("?") + 1, actionName.length());
			String[] queryParams = queryParam.split("&");
			List<String> list = new ArrayList<String>();
			for (String query : queryParams)
			{
				StringBuilder builderPrefix = new StringBuilder();
				StringBuilder builder = new StringBuilder();
				Stack<Character> stack = new Stack<Character>();

				for (int i = 0; i < query.length(); i++)
				{
					if (query.charAt(i) == '{')
					{
						stack.push('{');
						continue;
					}
					if (query.charAt(i) == '$')
						continue;

					if (!stack.isEmpty())
					{
						if ((char) stack.peek() == '{'
								&& query.charAt(i) == '}')
						{
							stack.pop();
							builderPrefix.append((String) map.get(builder
									.toString().trim()));
							list.add(builderPrefix.toString());
							break;
						}

						builder.append(query.charAt(i));
						continue;
					}
					builderPrefix.append(query.charAt(i));
				}
			}
			System.out.println(list);
		}
	}
}
