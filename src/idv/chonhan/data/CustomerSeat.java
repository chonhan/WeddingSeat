package idv.chonhan.data;

public class CustomerSeat {

	private Integer id = 0;

	private String name;

	/** Table Number from 1 to 29 */
	private Integer seat = 0;

	/** 0 for vegetarian and 1 for meat */
	private Integer isVeg = 0;

	/** 1 for Adult and 0 for Child */
	private Integer isChild = 0;

	/** 0 for absence, 1 for already here and 2 for will not come */
	private Integer status = 0;

	public CustomerSeat() {
		this.name = "No Name";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIsVeg() {
		return isVeg;
	}

	public void setIsVeg(Integer isVeg) {
		this.isVeg = isVeg;
	}

	public Integer getIsChild() {
		return isChild;
	}

	public void setIsChild(Integer isChild) {
		this.isChild = isChild;
	}

}
