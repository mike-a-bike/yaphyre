package test.yaphyre.geometry;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses( {VectorTest.class, MatrixTest.class, TransformationTest.class, RayTest.class})
public class AllGeometryTests {

}
