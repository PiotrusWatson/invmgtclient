package gla.cs.joose.coursework.invmgtclient.model;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class InvmgtClient {
	private static Client client;
	private static WebTarget baseTarget;
	private static WebTarget itemsTarget;
	private static WebTarget itemTarget;
	
	/**
	 * Constructor to initialise a REST client and web targets (resource URIs)
	 * DO NOT MODIFY
	 */
	public InvmgtClient(){
		client = ClientBuilder.newClient();
		
		baseTarget = client.target("http://localhost:8080/invmgtapi/webapi/invapi");
		itemsTarget = baseTarget.path("items");
		itemTarget = baseTarget.path("items").path("{itemid}");
	}
	
	/**
	 * This method makes a REST API call to invmgtapi service for an update to an item in the inventory.
	 * 
	 * @param updateitemid
	 * @param newBarcode
	 * @param newItemName
	 * @param newItemType_s
	 * @param newQty
	 * @param newSupplier
	 * @param newDesc
	 * @return returns the updated item if updateRequest is successful or the error status code if unsuccessful
	 */
	public Object updateRequest(long updateitemid,  long newBarcode, 
							 String newItemName, String newItemType_s,
							 int newQty, String newSupplier, String newDesc){
		Item updated = new Item(newBarcode,newItemName,ItemType.getItemType(newItemType_s), newQty, newSupplier, newDesc);
		
		
		Builder builder = itemTarget.resolveTemplate("itemid", updateitemid).request();
		Response putResponse = builder.put(Entity.json(updated));
		if (putResponse.getStatus() != 200){
			System.out.println("HTTP error code : " + putResponse.getStatus());
			return putResponse.getStatus();
		}
		//Task 1

		return updated;

	}
	
	/**
	 * This method makes a REST API call to invmgtapi service to delete an item from the inventory.
	 * 
	 * @param itemid
	 * @return returns a status code indicating the outcome of deleteRequest
	 */
	public int deleteRequest(long itemid){
		//Task 2
		Builder builder = itemTarget.resolveTemplate("itemid", itemid)
									 .request();
		
		Response getResponse = builder.delete();
		
		/*// check response status code
        if (getResponse.getStatus() != 200) {
            throw new RuntimeException("HTTP error code : "+ getResponse.getStatus());
        }
        
        // display deleteresponse
        String feedback = getResponse.readEntity(String.class);
        System.out.println("Server output... ");
        System.out.println(feedback + "\n");*/
		return getResponse.getStatus();
	}
	
	/**
	 * This method makes a REST API call to invmgtapi service to retrieve items that matches a 
	 * specific search pattern from the inventory.
	 * 
	 * @param searchbydesc
	 * @param pattern
	 * @param limit
	 * @return returns a list of items that matches the searchRequest parameters
	 */
	public Item[] searchRequest(String searchbydesc, String pattern, int limit){
		Builder builder = itemsTarget.resolveTemplate("desc", searchbydesc)
				.resolveTemplate("pattern", pattern)
				.resolveTemplate("limit", limit)
				.request();
		
		Response getResponse = builder.get();
		 
		if (getResponse.getStatus() != 200)
			throw new RuntimeException("HTTP error code : " + getResponse.getStatus() );
		
		// display getresponse
		String feedback = getResponse.readEntity(String.class);
		System.out.println("Server output... ");
		System.out.println(feedback + "\n");
		
		return getResponse.readEntity(Item[].class);
		//Task 3

	}
	
	/**
	 * This method makes a REST API call to invmgtapi service to add an item to the inventory
	 *  
	 * @param barcode
	 * @param itemName
	 * @param itemType_s
	 * @param qty
	 * @param supplier
	 * @param desc
	 * @return - returns a REST response status code indicating the outcome of addItemRequest
	 */
	public int addItemRequest(long barcode, String itemName, String itemType_s, int qty, String supplier, String desc){
				
		//Task 4
		
		Builder builder = itemsTarget.request();
	
		Item newItem = new Item(barcode, itemName, ItemType.getItemType( itemType_s ), qty, supplier, desc);
		
		Response getResponse = builder.post(Entity.json(newItem));
		
		// check response status code
        if (getResponse.getStatus() != 201) {
            throw new RuntimeException("HTTP error code : "+ getResponse.getStatus());
        }
        
        // display addItem response
        String feedback = getResponse.readEntity(String.class);
        System.out.println("Server output... ");
        System.out.println(feedback + "\n");
		return getResponse.getStatus();
	
	}
}
