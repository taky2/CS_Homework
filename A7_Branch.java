
/**
 * Branch is required for the tree data structures
 * 
 * Requires ZooGame.java
 * 
 * @author Dustin Fay
 */

public class A7_Branch
{

	Branch yes;
	Branch no;
	String data;

	Branch(String data, Branch yes, Branch no)
	{
		this.yes = yes;
		this.no = no;
		this.data = data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	public String getData()
	{
		return this.data;
	}

	// Yes is treated as left
	public void setYesReference(Branch yes)
	{
		this.yes = yes;
	}

	// No is treated as right
	public void setNoReference(Branch no)
	{
		this.no = no;
	}

	public Branch getYesReference()
	{
		return this.yes;
	}

	public Branch getNoReference()
	{
		return this.no;
	}

}
