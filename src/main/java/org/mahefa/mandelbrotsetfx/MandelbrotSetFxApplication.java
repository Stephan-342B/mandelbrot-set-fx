package org.mahefa.mandelbrotsetfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MandelbrotSetFxApplication extends Application {

	private ConfigurableApplicationContext springContext;
	private Parent rootNode;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);

		Scene scene = new Scene(rootNode);

		stage.setTitle("Mandelbrot Set Fx");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void init() throws Exception {
		springContext = SpringApplication.run(MandelbrotSetFxApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		rootNode = fxmlLoader.load();
	}

	public void stop() {
		springContext.close();
	}
}
