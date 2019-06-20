package server;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/inventory")
public class InventoryResource 
{
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
	public List<Item> getAllItems() throws Exception
	{
		return InventoryDAO.instance.getAllItems();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
	@Path("/category/{Category}")
	public List<Item> getAllItemsByCategory(@PathParam("Category") String category) throws Exception
	{
		return InventoryDAO.instance.getAllItemsByCategory(category);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
	@Path("/pricerange/{min}/{max}")
	public List<Item> getAllItemsByPriceRange(@PathParam("min") double min, @PathParam("max") double max) throws Exception
	{
		return InventoryDAO.instance.getAllItemsByPriceRange(min, max);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
	@Path("/lastadded/{limit}")
	public List<Item> getLastAddedItems(@PathParam("limit") int limit) throws Exception
	{
		return InventoryDAO.instance.getLastAddedItems(limit);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void addItem(@FormParam("name") String name,
			@FormParam("price") double price,
			@FormParam("category") String category,
			@Context HttpServletResponse servletResponse) throws Exception
	{
		System.out.println("Inside POST = " + name);
		
		Item item = new Item();
		item.setName(name);
		item.setPrice(price);
		item.setCategory(category);
		
		InventoryDAO.instance.addItem(item);
		
		servletResponse.sendRedirect("../addItem.html");
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/updateItem/{name}")
	public void updateItem(@PathParam("name") String name,
			@FormParam("price") double price,
			@FormParam("category") String category,
			@Context HttpServletResponse servletResponse) throws Exception
	{
		System.out.println("PUT name = " + name);
		
		Item Item = new Item();
		Item.setName(name);
		Item.setPrice(price);
		Item.setCategory(category);
		
		InventoryDAO.instance.updateItem(Item);
		
		//servletResponse.sendRedirect("../updateItem.html");
	}
	
	@DELETE
	@Produces(MediaType.TEXT_HTML)
	@Path("{ItemName}")
	public void deleteItem(@PathParam("ItemName") String name) throws Exception
	{
		System.out.println("Deleting = " + name);
		
		InventoryDAO.instance.deleteItem(name);
	}
}