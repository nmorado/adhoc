package dbfit;

public class TeradataTest extends DatabaseTest {
    public TeradataTest() {
        super(dbfit.api.DbEnvironmentFactory.newEnvironmentInstance("Teradata"));
        System.out.println("TeradataTest: TeradataEnvironment()");
    }
}

