package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import model.Item;

public class MainWindow 
{
	private static Scanner scanner = new Scanner(System.in);
	
	/** Start-Up Main Menu 
	 * input what you would like to do using numbers 1, 2, 3, 4, and 5 to exit
	 * 
	 * @param args
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException 
	{
		int input = 0;
		
		while (input != 5)
		{
			System.out.println("-- Welcome to the Poppulo Inventory System --\n");
			System.out.println("1. View Items in the Inventory");
			System.out.println("2. Add Items to Inventory");
			System.out.println("3. Update an Item in the Inventory");
			System.out.println("4. Delete an Item from the Inventory");
			System.out.println("5. Exit\n");
			
			System.out.print("What would you like to do (press 5 to exit): ");
			input = Integer.parseInt(scanner.nextLine());
			
			switch (input)
			{
			case 1:
				readItems();
				break;
			case 2:
				addItem();
				break;
			case 3:
				updateItem();
				break;
			case 4:
				deleteItem();
				break;
			case 5:
				break;
			default:
				System.out.println("Invalid Option! Please try again.");
				break;
			}
		}
	}
	
	/** This method gives the user options on how to view the Inventory.
	 *  The user picks an option and that creates a URI to send a GET request
	 *  
	 * @throws URISyntaxException
	 */
	private static void readItems() throws URISyntaxException
	{
		int option = 0;
		String input = null;
		URI uri;
		
		while (option != 4)
		{
			System.out.println("\n-- Inventory Item Viewer --");
			System.out.println("1. View all Items from a given Category");
			System.out.println("2. View Items of a certain Price Range");
			System.out.println("3. View the last number of Items added");
			System.out.println("4. Exit\n");
			
			System.out.print("What would you like to do (press 4 to exit): ");
			option = Integer.parseInt(scanner.nextLine());
			
			switch (option)
			{
			case 1:
				System.out.print("\nWhat Category of Items would you like to see: ");
				input = scanner.nextLine();
				uri = new URIBuilder().setScheme("http").setHost("localhost")
						.setPort(8080).setPath("/InventoryServer/rest/inventory/category/" + input).build();
				getItems(uri);
				break;
			case 2:
				System.out.println("\nWhat Price Range of Items would you like to see: ");
				System.out.print("Minimum: ");
				int min = Integer.parseInt(scanner.nextLine());
				System.out.print("Maximum: ");
				int max = Integer.parseInt(scanner.nextLine());
				uri = new URIBuilder().setScheme("http").setHost("localhost")
						.setPort(8080).setPath("/InventoryServer/rest/inventory/pricerange/"+min+"/"+max).build();
				getItems(uri);
				break;
			case 3:
				System.out.print("\nWhat Number of last added Items would you like to see: ");
				input = scanner.nextLine();
				uri = new URIBuilder().setScheme("http").setHost("localhost")
						.setPort(8080).setPath("/InventoryServer/rest/inventory/lastadded/" + input).build();
				getItems(uri);
				break;
			case 4:
				break;
			default:
				System.out.println("Invalid Option! Please try again.");
				break;
			}
		}	
	}
	
	/** This method is called after the user chooses what they want to view in the Inventory
	 *  The method creates and executes a http GET request to the server.
	 *  A response is then received and parsed into a List using a XMLPullParser class
	 *  The list is then printed out onto the screen
	 *  
	 * @param uri
	 */
	private static void getItems(URI uri)
	{
		try
		{
			CloseableHttpResponse response = null;
			
			try
			{
				HttpGet httpGet = new HttpGet(uri);
				httpGet.setHeader("Accept", "application/xml");
				
				CloseableHttpClient httpClient = HttpClients.createDefault();
				response = httpClient.execute(httpGet);
				
				HttpEntity entity = response.getEntity();
				String text = getASCIIContentFromEntity(entity);
				
				List<Item> inventory = new ParseInventory().doParseInventory(text);
				System.out.printf("Name\t| Price\t| Category\n");
				System.out.println("-------------------------------------");
				for (Item item : inventory)
				{
					System.out.printf(item.getName() + "\t| ");
				    System.out.printf("%.2f", item.getPrice()); 
				    System.out.printf("\t| " + item.getCategory() + "\n");
				}
			}
			finally
			{
				response.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private static String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException 
	{
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		
		while (n > 0)
		{
			byte[] b = new byte[4096];
			n = in.read(b);
			
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		
		return out.toString();
	}
	
	/**
	 * This method adds a new Item to the Inventory using a http POST request.
	 * the user inputs the details of the new item on screen and this method creates and sends nameValuePairs using those details on to the server.
	 * 
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static void addItem() throws URISyntaxException, ClientProtocolException, IOException
	{
		String input = null;
		Item item = new Item();
		
		do
		{
			System.out.println("\n-- Add Item to the Inventory --");
			System.out.print("Name of Item: ");
			item.setName(scanner.nextLine());
			System.out.print("Price: ");
			item.setPrice(Double.parseDouble(scanner.nextLine()));
			System.out.print("Category: ");
			item.setCategory(scanner.nextLine());
			
			URI uri = new URIBuilder().setScheme("http").setHost("localhost")
					.setPort(8080).setPath("/InventoryServer/rest/inventory").build();
			
			HttpPost httpPost = new HttpPost(uri);
			httpPost.setHeader("Accept", "text/html");
			CloseableHttpClient client = HttpClients.createDefault();
			
			// POST
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("name", item.getName()));
			nameValuePairs.add(new BasicNameValuePair("price", Double.toString(item.getPrice())));
			nameValuePairs.add(new BasicNameValuePair("category", item.getCategory()));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			CloseableHttpResponse response = client.execute(httpPost);
			
			System.out.print("Would you like to add another item? (y/n): ");
			input = scanner.nextLine();
			
		} while (!input.equals("n"));
	}
	
	/**
	 * This method asks the user what they want to update, the price or the category of an item
	 * Input is then taken from the user of what item they want to update and what they are updating (price/category) and creates an item object
	 * The updater() method is then called
	 * 
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private static void updateItem() throws ClientProtocolException, URISyntaxException, IOException
	{
		int input = 0;
		Item item = new Item();
		
		do
		{
			System.out.println("-- Update Item in Inventory");
			System.out.println("1. Update Price");
			System.out.println("2. Update Category");
			System.out.println("3. Exit");
			
			System.out.print("What would you like to do (press 3 to exit): ");
			input = Integer.parseInt(scanner.nextLine());
			
			switch (input)
			{
			case 1:
				System.out.print("\nWhat Item do you want to update: ");
				item.setName(scanner.nextLine());
				System.out.print("New Price: ");
				item.setPrice(Double.parseDouble(scanner.nextLine()));
				item.setCategory(null);
				
				updater(item);
				break;
			case 2:
				System.out.print("\nWhat Item do you want to update: ");
				item.setName(scanner.nextLine());
				System.out.print("New Category: ");
				item.setCategory(scanner.nextLine());
				item.setPrice(0);
				
				updater(item);
				break;
			case 3:
				break;
			default:
				System.out.println("Invalid Option! Please try again.");
				break;
			}
		} while (input != 3);
	}
	
	/**
	 * This method creates a URI using the name of the item the user wants to update
	 * The method then sends a http POST request with the items updated details as nameValuePairs to change in the inventory
	 * 
	 * @param item
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static void updater(Item item) throws URISyntaxException, ClientProtocolException, IOException
	{
		URI uri = new URIBuilder().setScheme("http").setHost("localhost")
				.setPort(8080).setPath("/InventoryServer/rest/inventory/updateItem/"+item.getName()).build();
		
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setHeader("Accept", "text/html");
		CloseableHttpClient client = HttpClients.createDefault();
		
		// POST
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("name", item.getName()));
		nameValuePairs.add(new BasicNameValuePair("price", Double.toString(item.getPrice())));
		nameValuePairs.add(new BasicNameValuePair("category", item.getCategory()));
		
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		CloseableHttpResponse response = client.execute(httpPost);
	}
	
	/**
	 * This method asks the user for the name of the item they want to delete from the inventory
	 * The method then creates a URI using the input from the user (item name)
	 * A http DELETE request is executed to delete the item from the inventory
	 * 
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static void deleteItem() throws URISyntaxException, ClientProtocolException, IOException
	{
		String input = null;
		String name = null;
		
		do
		{
			System.out.println("\n-- Delete Item to the Inventory --");
			System.out.print("Name of Item: ");
			name = scanner.nextLine();
			
			URI uri = new URIBuilder().setScheme("http")
					.setHost("localhost")
					.setPort(8080)
					.setPath("/InventoryServer/rest/inventory/" + name)
					.build();
			
			HttpDelete httpDelete = new HttpDelete(uri);
			httpDelete.setHeader("Accept", "text/html");
			
			CloseableHttpClient client = HttpClients.createDefault();
			CloseableHttpResponse response = client.execute(httpDelete);
			
			System.out.print("Would you like to add another item? (y/n): ");
			input = scanner.nextLine();
			
		} while (!input.equals("n"));
	}
}
