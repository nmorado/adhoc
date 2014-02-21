package dbfit.environment;

import dbfit.api.DBEnvironment;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

public class DB2DbEnvironmentFactoryTest {

    @Ignore("DB2 driver not available during build")
    @Test
    public void newDbEnvironmentTest() throws Exception {
        DBEnvironment env = dbfit.api.DbEnvironmentFactory.newEnvironmentInstance("DB2");
        assertNotNull(env);
    }
}

