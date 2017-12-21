package nongbang.hg.nongbang.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CropBb {
	Statement stmt;
	Connection conn;
	public CropBb(){
		try{
			//调用Class.forName()方法加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("成功加载MySQL驱动！");
			String url="jdbc:mysql://39.108.70.152:3306/zwdata";    //JDBC的URL
			conn = DriverManager.getConnection("jdbc:mysql://39.108.70.152:3306/zwdata?user=root&password=082312&useUnicode=true&amp;characterEncoding=UTF-8");
			stmt = conn.createStatement(); //创建Statement对象
			System.out.println("成功连接到数据库！");

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getCropInformation(String id) {
		String list=null;
		String sql = "select * from zw  where id = "+Integer.parseInt(id);    //要执行的SQL
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			if (rs==null) {
				return null;
			}
			while (rs.next()){
				list=rs.getInt(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4)+"|"+rs.getString(5)+"|"+rs.getString(6)+"|"+rs.getString(7)+"|"+rs.getString(8)+"|"+rs.getString(9)+"|"+rs.getString(10)+"|"+rs.getString(11)+"|"+rs.getString(12)+"|"+rs.getString(13)+"|"+rs.getString(14)+"|"+rs.getString(15)+"|"+rs.getString(16);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//创建数据对象

		return list;
	}

	public String findCropInformation(String name) {
		String list=null;
		String sql = "select * from zw  where name like  '%"+name+"%'";    //要执行的SQL
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			if (rs==null) {
				return null;
			}
			while (rs.next()){
				list=rs.getInt(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4)+"|"+rs.getString(5)+"|"+rs.getString(6)+"|"+rs.getString(7)+"|"+rs.getString(8)+"|"+rs.getString(9)+"|"+rs.getString(10)+"|"+rs.getString(11)+"|"+rs.getString(12)+"|"+rs.getString(13)+"|"+rs.getString(14)+"|"+rs.getString(15)+"|"+rs.getString(16);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//创建数据对象


		return list;
	}

	public void findCropInformationByType(String type) {
		String list=null;
		String sql = "select * from zw  where leixing = '"+type+"'";    //要执行的SQL
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			if (rs==null) {
				return;
			}
		//	StaticValues.findList.clear();
			while (rs.next()){
				list=rs.getInt(1)+"|"+rs.getString(2)+"|"+rs.getString(3)+"|"+rs.getString(4)+"|"+rs.getString(5)+"|"+rs.getString(6)+"|"+rs.getString(7)+"|"+rs.getString(8)+"|"+rs.getString(9)+"|"+rs.getString(10)+"|"+rs.getString(11)+"|"+rs.getString(12)+"|"+rs.getString(13)+"|"+rs.getString(14)+"|"+rs.getString(15)+"|"+rs.getString(16);
		//		StaticValues.findList.add(list);
				list="";
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//创建数据对象

	}
}
