package Runner;


	import io.cucumber.testng.AbstractTestNGCucumberTests;
	import io.cucumber.testng.CucumberOptions;

	@CucumberOptions (
	        features = "./src/test/java/Feature/T.feature",
	        glue = {"StepDefination"}
	)
	public class Run extends AbstractTestNGCucumberTests {
}
