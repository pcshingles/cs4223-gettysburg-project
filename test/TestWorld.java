import student.gettysburg.engine.GettysburgFactory;

/**
 * Test world class - singleton instance of game engine
 * @author Paul
 *
 */
public class TestWorld {

	private GettysburgFactory factory;
	
	public GettysburgFactory getFactory() {
		if(factory == null){
			this.factory = new GettysburgFactory();
		}
		return this.factory;
	}

}
