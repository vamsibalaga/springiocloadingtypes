package com.example.demo;

import com.example.demo.configs.MySimConfig;
import com.example.demo.configs.WithConfigurationExample;
import com.example.demo.configs.WithoutConfigurationExample;
import com.example.demo.implementations.BoostMobile;
import com.example.demo.interfaces.ISim;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WithoutConfigurationExample.class)
public class DemoApplication {

	public static void main(String[] args) {

		System.out.println("Hello World  Demo Application using Java 17 in Local...");

		//without IOC
		System.out.println("******************************JAVA Initialization****************************************");
		ISim sim = new BoostMobile(); // Only constructor called
		sim.calling();
		sim.data();
		sim.hashCode();
		System.out.println("*****************************************************************************************");

		//IOC Spring only Initializes configuration/AutoConfiguration
		System.out.println("**************************************Primary ISim***************************************************");
		ApplicationContext context  = SpringApplication.run(DemoApplication.class, args);
		System.out.println("**************************************Primary ISim After Context ***************************************************");
		//Gives the same instance singleton by Spring container
		ISim simwoConfigIOC = context.getBean(ISim.class);
		//Gives the same instance singleton by Spring container
		ISim simwoConfigIOC1 = context.getBean(ISim.class);
		/*Both simwoConfigIOC
		*      simwoConfigIOC1
		*       have same Hashcode
		*/
		System.out.println("*****************************************************************************************");
		//Now how do I get the other Beans defined without configuration annotation
		//It gives the same singleton  instance
		System.out.println("===  @Bean ===");
		ISim simIOC = context.getBean(ISim.class); //(constructor + hash)
		simIOC.calling();
		simIOC.data();
		simIOC.hashCode();
		System.out.println("############################################################################################");
		//Difference comes when you call the same bean definition to do another actions without effecting the actual bean ISim

		// Get the woConfigSim bean
		ISim woConfigSim = context.getBean("woConfigSim", ISim.class);
		System.out.println("woConfigSim hashCode: " + woConfigSim.hashCode());

		// Get serviceAWithout and serviceBWithout
		String serviceA = context.getBean("serviceAWithout", String.class);
		String serviceB = context.getBean("serviceBWithout", String.class);

		// Verify - get woConfigSim again
		ISim woConfigSim2 = context.getBean("woConfigSim", ISim.class);
		System.out.println("woConfigSim2 hashCode: " + woConfigSim2.hashCode());
		// woConfigSim and woConfigSim2 will have SAME hashCode (singleton)
		// But serviceA and serviceB used DIFFERENT instances (no @Configuration CGLIB proxy)


		/*
		 Config Based and Annotation
		 Without Config Based Annotaion beans if both are implementing same Class/Bean
		 to pull it either use Import annotation or create a new context to bring that bean like
		 AnnotationConfigApplicationContext
		 #No qualifying bean of type 'com.example.demo.interfaces.ISim' available: expected single matching bean but found 2: configSim,woConfigSim#
		 then we need to add Qualifier and call them with qulaified name when trying get the bean
		 												or
		 Add primary and other should have Qualifier to call but bydefault primary instance is what spring brings from IOC
		 Then context.getBean(ISim.class) will return configSim by default.
		 */

		//getting the objects using IOC
		//Java Based
//		System.out.println("=== IOC ===");
//		ApplicationContext context1 =
//				new AnnotationConfigApplicationContext(MySimConfig.class);

//		ApplicationContext context = new AnnotationConfigApplicationContext(
//				MySimConfig.class,
//				WithoutConfigurationExample.class,
//				WithConfigurationExample.class
//		);

//		System.out.println("===  @Bean ===");
//		ISim simIOC = context.getBean(ISim.class); //(constructor + hash) Spring IOC call creates singleton
//		simIOC.calling();
//		simIOC.data();
//
//		ISim simIOC1 = context.getBean(ISim.class); // will not call initialize the class again
//		simIOC1.calling();
//		simIOC1.data();
		/*
		ApplicationContext context = new AnnotationConfigApplicationContext(WithoutConfigurationExample.class);
		System.out.println("=== WITHOUT @Configuration ===");
		ISim simwoConfigIOC = context.getBean(ISim.class);
		simwoConfigIOC.calling();
		simwoConfigIOC.data();
		simwoConfigIOC.hashCode();


		String serviceA = context.getBean("serviceAWithout", String.class);
		String serviceB = context.getBean("serviceBWithout", String.class);

		System.out.println(serviceA);
		System.out.println(serviceB);
		System.out.println("Different hash codes = Multiple instances created!");
		 */


		//ApplicationContext context = new AnnotationConfigApplicationContext(WithConfigurationExample.class);
		//ISim simwoConfigIOC = context.getBean(ISim.class);
		//System.out.println(simwoConfigIOC.hashCode());
		/*System.out.println("=== WITH @Configuration ===");
		ISim simwoConfigIOC = context.getBean(ISim.class);
		simwoConfigIOC.calling();
		simwoConfigIOC.data();
		simwoConfigIOC.hashCode();


		String serviceA = context.getBean("serviceAWith", String.class);
		String serviceB = context.getBean("serviceBWith", String.class);

		System.out.println(serviceA);
		System.out.println(serviceB);
		System.out.println("Different hash codes = Same instances created and shared using CGLIB by Spring Frameowrk making it singleton!\n" +
				"Spring creates a CGLIB proxy of the class.");

		 */
	}



}
