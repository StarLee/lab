

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.develop.data.base.JDBCCallback;
import com.develop.data.base.Session;
import com.develop.data.base.iml.DefaultQuery;

public class CallProcedure
{
	public static void main(String[] args) throws SQLException
	{
		DefaultQuery query = new DefaultQuery();
		query.execute(new JDBCCallback()
		{

			@Override
			public Object doExecute(Session arg0)
			{
				Connection conn = arg0.getConnectionProvider().getConnection();
				CallableStatement call;
				try
				{
//					call = conn.prepareCall("{call simple()}");
					call = conn.prepareCall("{?=call hello(?)}");
					call.registerOutParameter(1, Types.CHAR);
					call.setString(2, "lixing");
					call.execute();
					System.out.println(call.getString(1));
					//call.registerOutParameter(1, Types.INTEGER);
					//call.registerOutParameter(2, Types.INTEGER);
					//call.execute();
					//ResultSet rs=call.executeQuery();
					//while(rs.next())
					//System.out.println(rs.getString(1));
				} catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}
