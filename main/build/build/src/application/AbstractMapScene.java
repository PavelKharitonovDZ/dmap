package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import ru.dz.vita2d.data.ServerUnitType;
import ru.dz.vita2d.ui.EntityListWindow;

public abstract class AbstractMapScene implements IMapScene 
{	
	
	protected Stage primaryStage;
	protected Main main;

	public AbstractMapScene( Stage primaryStage, Main main ) {
		this.primaryStage = primaryStage;
		this.main = main;		
	}

	protected void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Версия системы");
		alert.setHeaderText("ОРВД Планшет Инженера");
	
		String s =
				"Версия программы: \t"+Defs.VERSION+"\n"+
				"Версия сервера:    \t\t"+main.rc.getServerVersion()+"\n"+
				"URL сервера:       \t\t"+main.rc.getServerURL()+"\n"+
				"Базовая ссылка:    \t\t"+main.getHostServices().getDocumentBase()+"\n"+ 
				"Пользователь:      \t\t"+main.rc.getLoggedInUser()+"\n"
				//+"Точка запуска:     \t\t"+main.getHostServices().getCodeBase()+"\n"
				;
	
		alert.setContentText(s);
		alert.show();
	}

	protected void fillMenu(MenuBar menuBar) {
		// File menu - new, save, exit
		Menu fileMenu = new Menu("Файл");
	
		MenuItem loginMenuItem = new MenuItem("Сменить пользователя");
		loginMenuItem.setOnAction(actionEvent -> main.logout());
	
		MenuItem exitMenuItem = new MenuItem("Выход (выключить планшет)");
		exitMenuItem.setOnAction(actionEvent -> main.requestShutdown());
		exitMenuItem.setAccelerator(KeyCombination.keyCombination("Alt+F4"));
	
	
		fileMenu.getItems().addAll( loginMenuItem,
				new SeparatorMenuItem(), exitMenuItem);
	
	
	
		Menu navMenu = new Menu("Навигация");
	
		MenuItem navHomeMap = new MenuItem("На общую карту");
		//navHomeMap.setOnAction(actionEvent -> setMapData(bigMapData));
		navHomeMap.setOnAction(actionEvent -> setMapData(main.ml.getRootMap()));
	
		Menu navMaps = new Menu("Карты");
		//navMaps.setOnAction(actionEvent -> setMapData(bigMapData));
		main.ml.fillMapsMenu( navMaps, this );
	
		MenuItem navOverview = new MenuItem("Обзор");
		navOverview.setOnAction(actionEvent -> setOverviewScale());
	
		navMenu.getItems().addAll( navHomeMap, navMaps, new SeparatorMenuItem(), navOverview );
	
		
		
		Menu dataMenu = new Menu("Данные");
	
		ServerUnitType.forEach(t -> {
			MenuItem dataItem = new MenuItem(t.getDisplayName());
			dataItem.setOnAction(actionEvent -> new EntityListWindow(t, main.rc, main.sc));
	
			dataMenu.getItems().add(dataItem);			
		});
		
		//dataMenu.Menu debugMenu = new Menu("Debug");
		//MenuItem d1 = new MenuItem("На общую карту");
		//d1.setOnAction(actionEvent -> setMapData(main.ml.getRootMap()));
	
		
		/*
	    Menu webMenu = new Menu("Web");
	    CheckMenuItem htmlMenuItem = new CheckMenuItem("HTML");
	    htmlMenuItem.setSelected(true);
	    webMenu.getItems().add(htmlMenuItem);
	
	    CheckMenuItem cssMenuItem = new CheckMenuItem("CSS");
	    cssMenuItem.setSelected(true);
	    webMenu.getItems().add(cssMenuItem);
	
	    Menu sqlMenu = new Menu("SQL");
	    ToggleGroup tGroup = new ToggleGroup();
	    RadioMenuItem mysqlItem = new RadioMenuItem("MySQL");
	    mysqlItem.setToggleGroup(tGroup);
	
	    RadioMenuItem oracleItem = new RadioMenuItem("Oracle");
	    oracleItem.setToggleGroup(tGroup);
	    oracleItem.setSelected(true);
	
	    sqlMenu.getItems().addAll(mysqlItem, oracleItem,
	        new SeparatorMenuItem());
	
	    Menu tutorialManeu = new Menu("Tutorial");
	    tutorialManeu.getItems().addAll(
	        new CheckMenuItem("Java"),
	        new CheckMenuItem("JavaFX"),
	        new CheckMenuItem("Swing"));
	
	    sqlMenu.getItems().add(tutorialManeu);
		 */
	
		Menu aboutMenu = new Menu("О системе");
	
		MenuItem version = new MenuItem("Версия");
		version.setOnAction(actionEvent -> showAbout());
	
		MenuItem aboutDz = new MenuItem("Digital Zone");
		aboutDz.setOnAction(actionEvent -> main.getHostServices().showDocument(Defs.HOME_URL));
	
		MenuItem aboutVita = new MenuItem("VitaSoft");
		aboutVita.setOnAction(actionEvent -> main.getHostServices().showDocument("vtsft.ru"));
	
		aboutMenu.getItems().addAll( version, new SeparatorMenuItem(), aboutDz, aboutVita );
	
		// --------------- Menu bar
	
		//menuBar.getMenus().addAll(fileMenu, webMenu, sqlMenu);
		menuBar.getMenus().addAll(fileMenu, navMenu, dataMenu, aboutMenu );
	}

}
