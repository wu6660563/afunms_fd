/**
 * @author Соѕь
 * @project afunms
 * @date 2011-10-18
 */

package com.afunms.temp.model;


public class Objbean {
	
	private String id;
	
	private String lineid;
   
	private String image;
	
	private String color;
	
	private String category;
	
	private String xmlname;
	
	private String type;

	private String relationMap;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public String getXmlname() {
		return xmlname;
	}

	public void setXmlname(String xmlname) {
		this.xmlname = xmlname;
	}

	public String getLineid() {
		return lineid;
	}

	public void setLineid(String lineid) {
		this.lineid = lineid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    /**
     * getRelationMap:
     * <p>
     *
     * @return  String
     *          -
     * @since   v1.01
     */
    public String getRelationMap() {
        return relationMap;
    }

    /**
     * setRelationMap:
     * <p>
     *
     * @param   relationMap
     *          -
     * @since   v1.01
     */
    public void setRelationMap(String relationMap) {
        this.relationMap = relationMap;
    }

}
