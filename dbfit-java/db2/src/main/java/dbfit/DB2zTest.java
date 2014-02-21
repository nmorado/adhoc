package dbfit;

public class DB2zTest  extends DatabaseTest {
    public DB2zTest(){
        super(dbfit.api.DbEnvironmentFactory.newEnvironmentInstance("DB2z"));
    }
}

