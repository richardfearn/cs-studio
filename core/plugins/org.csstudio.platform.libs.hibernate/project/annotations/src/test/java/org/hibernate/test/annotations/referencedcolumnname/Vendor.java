//$Id: Vendor.java 14736 2008-06-04 14:23:42Z hardy.ferentschik $
package org.hibernate.test.annotations.referencedcolumnname;

import javax.persistence.Id;
import javax.persistence.Entity;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Vendor {
	int id;

	@Id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

