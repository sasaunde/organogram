package capgemini.orgchart;

import javax.naming.directory.SearchResult;

/**
 * Person POJO
 * 
 * @author sasaunde
 *
 */
public class Person {

	private String name;
	
	private String phone;
	
	private String imageLocation;

	public Person(SearchResult searchResult) {
		
		
		name = searchResult.getAttributes().get("displayName").toString();
		phone = searchResult.getAttributes().get("msRTCSIP-Line").toString();
		imageLocation = searchResult.getAttributes().get("userPrincipalName").toString();
		
		// These are in the format key: value so remove the key
		name = name.substring(name.indexOf(':')+1, name.length()).trim();
		phone = phone.substring(phone.indexOf(':')+1, phone.length()).trim();
		imageLocation = "/image?id=" + imageLocation.substring(imageLocation.indexOf(':')+1, imageLocation.length()).trim();
	}
	
	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	} 
}
