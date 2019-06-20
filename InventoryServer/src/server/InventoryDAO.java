package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public enum InventoryDAO 
{
	instance;
	
	public List<Item> getAllItems() throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id, name, price, category from Inventory");
		
		ArrayList<Item> list = new ArrayList<Item>();
		while (rs.next())
		{
			int id = rs.getInt("id") + 1;
			String name = rs.getString("name");
			double price = rs.getDouble("price");
			String category = rs.getString("category");
			Item Item = new Item(id, name, price, category);
			list.add(Item);
		}
		
		stmt.close();
		conn.close();
		
		return list;
	}
	
	public List<Item> getAllItemsByCategory(String cat) throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id, name, price, category from Inventory where category = '" + cat + "'");
		
		ArrayList<Item> list = new ArrayList<Item>();
		while (rs.next())
		{
			int id = rs.getInt("id") + 1;
			String name = rs.getString("name");
			double price = rs.getDouble("price");
			String category = rs.getString("category");
			Item Item = new Item(id, name, price, category);
			list.add(Item);
		}
		
		stmt.close();
		conn.close();
		
		return list;
	}
	
	public List<Item> getAllItemsByPriceRange(double min, double max) throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id, name, price, category from Inventory where price between " + min + " and " + max);
		
		ArrayList<Item> list = new ArrayList<Item>();
		while (rs.next())
		{
			int id = rs.getInt("id") + 1;
			String name = rs.getString("name");
			double price = rs.getDouble("price");
			String category = rs.getString("category");
			Item Item = new Item(id, name, price, category);
			list.add(Item);
		}
		
		stmt.close();
		conn.close();
		
		return list;
	}
	
	public List<Item> getLastAddedItems(int limit) throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id, name, price, category from Inventory order by id desc");
		
		ArrayList<Item> list = new ArrayList<Item>();
		while (rs.next() && limit > 0)
		{
			int id = rs.getInt("id") + 1;
			String name = rs.getString("name");
			double price = rs.getDouble("price");
			String category = rs.getString("category");
			Item Item = new Item(id, name, price, category);
			list.add(Item);
			limit--;
		}
		
		stmt.close();
		conn.close();
		
		return list;
	}
	
	public void addItem (Item Item) throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("insert into Inventory (name, price, category) values ('" + Item.getName() + "', '" + Item.getPrice() + "', '" + Item.getCategory() + "')");
		
		stmt.close();
		conn.close();
	}
	
	public void updateItem (Item Item) throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		
		if(Item.getCategory().isEmpty())
			stmt.executeUpdate("update Inventory set price='" + Item.getPrice() + "' where name='" + Item.getName() + "'");
		if(Item.getPrice() == 0)
			stmt.executeUpdate("update Inventory set category='" + Item.getCategory() + "' where name='" + Item.getName() + "'");
		
		stmt.close();
		conn.close();
	}
	
	public void deleteItem (String name) throws Exception
	{
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/oneDB", "SA", "Passw0rd");
		Statement stmt = conn.createStatement();
		
		stmt.executeUpdate("delete from Inventory where name='" + name + "'");
		
		stmt.close();
		conn.close();
	}	
}
